package connectors

import com.github.tomakehurst.wiremock.client.WireMock.*
import itutils.ApplicationWithWiremock
import models.{RepayReallocationSummary, RepayReallocationSummaryDetails}
import org.scalatest.BeforeAndAfterEach
import org.scalatest.concurrent.{IntegrationPatience, ScalaFutures}
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec
import play.api.http.Status.{INTERNAL_SERVER_ERROR, OK}
import uk.gov.hmrc.http.HeaderCarrier
import helpers.RepaymentsReallocationsHelper

import java.time.LocalDate

class RepaymentsReallocationsConnectorISpec
    extends AnyWordSpec
    with Matchers
    with ScalaFutures
    with IntegrationPatience
    with ApplicationWithWiremock
    with BeforeAndAfterEach 
    with RepaymentsReallocationsHelper {

  implicit val hc: HeaderCarrier = HeaderCarrier()

  private val connector: RepaymentsReallocationsConnector = app.injector.instanceOf[RepaymentsReallocationsConnector]

  // TODO: add auth stub logic and relevant cases

  "getRepayReallocationSummary" should {

    def url(taxRef: Long, accPeriod: Long) =
      s"/corporation-tax/repayments-and-reallocations/$taxRef/$accPeriod"

    "return RepayReallocationSummary, get Repayments with status code OK" in {
      val response = repaymentSummary

      stubFor(
        get(urlPathEqualTo(url(1L, 5L)))
          .willReturn(
            aResponse()
              .withStatus(OK)
              .withBody(
                s"""{
                   |"transactions":
                   |[
                   |  {
                   |  "transactionDate":"2008-10-02",
                   |  "type":"RFR",
                   |  "amount":-56280,
                   |  "accountingPeriodEndDate":"2003-12-20",
                   |  "taxpayerReference":"8754000057"
                   |  }
                   |]}""".stripMargin
              )
          )
      )

      val result = connector.getRepayReallocationSummary(1L, 5L).futureValue
      result mustEqual response
    }

    "return RepayReallocationSummary, get Reallocations with status code OK" in {
      val response = reallocationSummary

      stubFor(
        get(urlPathEqualTo(url(1L, 5L)))
          .willReturn(
            aResponse()
              .withStatus(OK)
              .withBody(
                s"""{
                   |"transactions":
                   |[
                   |  {
                   |  "transactionDate":"2007-01-05",
                   |  "type":"RTO",
                   |  "amount":56280,
                   |  "accountingPeriodEndDate":"2003-12-31",
                   |  "taxpayerReference":"8754000057"
                   |  }
                   |]}""".stripMargin
              )
          )
      )

      val result = connector.getRepayReallocationSummary(1L, 5L).futureValue
      result mustEqual response
    }

    "return RepayReallocationSummary, get Repayments and Reallocations with status code OK" in {
      val response = multipleSummaries

      stubFor(
        get(urlPathEqualTo(url(1L, 5L)))
          .willReturn(
            aResponse()
              .withStatus(OK)
              .withBody(
                s"""{
                   |"transactions":
                   |[  {
                   |  "transactionDate":"2008-10-02",
                   |  "type":"RFR",
                   |  "amount":-56280,
                   |  "accountingPeriodEndDate":"2003-12-20",
                   |  "taxpayerReference":"8754000057"
                   |  },
                   |  {
                   |  "transactionDate":"2007-01-05",
                   |  "type":"RTO",
                   |  "amount":56280,
                   |  "accountingPeriodEndDate":"2003-12-31",
                   |  "taxpayerReference":"8754000057"
                   |  }
                   |]}""".stripMargin
              )
          )
      )

      val result = connector.getRepayReallocationSummary(1L, 5L).futureValue
      result mustEqual response
    }

    "return INTERNAL_ERROR when BE failed" in {
      stubFor(
        get(urlPathEqualTo(url(1L, 5L)))
          .willReturn(
            aResponse()
              .withStatus(INTERNAL_SERVER_ERROR)
              .withBody(
                s"""{
                   |error" : "Error while retrieving repayment reallocation summary"
                   |}""".stripMargin
              )
          )
      )

      val ex = intercept[Exception] {
        connector.getRepayReallocationSummary(1L, 5L).futureValue
      }
      ex.getMessage.toLowerCase must include("error while retrieving repayment reallocation summary")
    }
  }

}
