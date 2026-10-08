package connectors.gpa


import com.github.tomakehurst.wiremock.client.WireMock.*
import helpers.gpa.GroupPaymentsHelper
import itutils.ApplicationWithWiremock
import models.gpa.GroupSummaryDetailsResponse
import org.scalatest.BeforeAndAfterEach
import org.scalatest.concurrent.{IntegrationPatience, ScalaFutures}
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec
import play.api.http.Status.*
import uk.gov.hmrc.http.HeaderCarrier

class GroupPaymentsConnectorISpec
  extends AnyWordSpec
    with Matchers
    with ScalaFutures
    with IntegrationPatience
    with ApplicationWithWiremock
    with BeforeAndAfterEach
    with GroupPaymentsHelper {

  implicit val hc: HeaderCarrier = HeaderCarrier()

  private val connector: GroupPaymentsConnector = app.injector.instanceOf[GroupPaymentsConnector]

  // TODO: add auth stub logic and relevant cases

  "getGroupSummary" should {

    def url(gpaUTR: Long, nomCompanyUTR: Long) =
      s"/corporation-tax/group-summary/$gpaUTR/$nomCompanyUTR"

    "return Group Summary Details with status code OK" in {
      stubFor(
        get(urlPathEqualTo(url(1L, 5L)))
          .willReturn(
            aResponse()
              .withStatus(OK)
              .withBody(
                s"""
                   |{
                   |"gpaGrpSummaryDetails": [ {
                   | "contractEndDate": "2026-01-07",
                   | "groupTaxCharge": -11.01,
                   | "groupPayment": -13.02,
                   | "groupPaymentRecordCount": 2,
                   | "contractStatus": "ACTIVE",
                   | "contractVersion": 2 } ],
                   | "gpaReferenceNumberLst": [ {
                   | "taxpayerReference": 112 } ],
                   | "nominatedCompanyName": "Some company name"
                   |}
                   |""".stripMargin
              )
          )
      )

      val result = connector.getGroupSummary(1L, 5L).futureValue
      result mustEqual groupPaymentDetailsResponse
    }

    "return INTERNAL_ERROR when BE failed" in {
      stubFor(
        get(urlPathEqualTo(url(1L, 5L)))
          .willReturn(
            aResponse()
              .withStatus(INTERNAL_SERVER_ERROR)
              .withBody("boom")
          )
      )

      val ex = intercept[Exception] {
        connector.getGroupSummary(1L, 5L).futureValue
      }
      ex.getMessage.toLowerCase must include("boom")
    }

    "return 400 when BE returns BAD_REQUEST " in {
      stubFor(
        get(urlPathEqualTo(url(1L, 5L)))
          .willReturn(
            aResponse()
              .withStatus(BAD_REQUEST)
              .withBody("Invalid Request")
          )
      )

      val ex = intercept[Exception] {
        connector.getGroupSummary(1L, 5L).futureValue
      }
      ex.getMessage must include("Invalid Request")
    }

    "return 404 when BE returns NOT_FOUND " in {
      stubFor(
        get(urlPathEqualTo(url(1L, 5L)))
          .willReturn(
            aResponse()
              .withStatus(NOT_FOUND)
              .withBody("Not found")
          )
      )

      val ex = intercept[Exception] {
        connector.getGroupSummary(1L, 5L).futureValue
      }
      ex.getMessage must include("Not found")
    }
  }

}