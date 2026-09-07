package connectors


import com.github.tomakehurst.wiremock.client.WireMock.*
import itutils.ApplicationWithWiremock
import models.DisplayNeeded
import org.scalatest.BeforeAndAfterEach
import org.scalatest.concurrent.{IntegrationPatience, ScalaFutures}
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec
import play.api.http.Status.{BAD_REQUEST, INTERNAL_SERVER_ERROR, NOT_FOUND, OK}
import uk.gov.hmrc.http.HeaderCarrier

class DisplayNeededConnectorISpec
  extends AnyWordSpec
    with Matchers
    with ScalaFutures
    with IntegrationPatience
    with ApplicationWithWiremock
    with BeforeAndAfterEach {

  implicit val hc: HeaderCarrier = HeaderCarrier()

  private val connector: DisplayNeededConnector = app.injector.instanceOf[DisplayNeededConnector]

  // TODO: add auth stub logic and relevant cases

  "getDisplayNeeded" should {

    def url(taxRef: Long, accPeriod: Long) =
      s"/corporation-tax/display-needed/$taxRef/$accPeriod"

    "return Display Needed with all flags set to false, populated from BE with status code OK" in {
      val response = DisplayNeeded(
        taxIsDisplayNeededFlag = false,
        interestIsDisplayNeededFlag = false,
        paymentIsDisplayNeededFlag = false,
        repayReallocIsDisplayNeededFlag = false
      )

      stubFor(
        get(urlPathEqualTo(url(10L, 1L)))
          .willReturn(
            aResponse()
              .withStatus(OK)
              .withBody(
                s"""
                   |{
                   |"taxIsDisplayNeededFlag":false,
                   |"interestIsDisplayNeededFlag":false,
                   |"paymentIsDisplayNeededFlag":false,
                   |"repayReallocIsDisplayNeededFlag":false
                   |}
                   |""".stripMargin
              )
          )
      )

      val result = connector.getDisplayNeeded(10L, 1L).futureValue
      result mustEqual response
    }

    "return Display Needed with all flags set to true, populated from BE with status code OK" in {
      val response = DisplayNeeded(
        taxIsDisplayNeededFlag = true,
        interestIsDisplayNeededFlag = true,
        paymentIsDisplayNeededFlag = true,
        repayReallocIsDisplayNeededFlag = true
      )

      stubFor(
        get(urlPathEqualTo(url(20L, 1L)))
          .willReturn(
            aResponse()
              .withStatus(OK)
              .withBody(
                s"""
                   |{
                   |"taxIsDisplayNeededFlag":true,
                   |"interestIsDisplayNeededFlag":true,
                   |"paymentIsDisplayNeededFlag":true,
                   |"repayReallocIsDisplayNeededFlag":true
                   |}
                   |""".stripMargin
              )
          )
      )

      val result = connector.getDisplayNeeded(20L, 1L).futureValue
      result mustEqual response
    }

    "return Display Needed with some flags set to false and true, populated from BE with status code OK" in {
      val response = DisplayNeeded(
        taxIsDisplayNeededFlag = true,
        interestIsDisplayNeededFlag = false,
        paymentIsDisplayNeededFlag = true,
        repayReallocIsDisplayNeededFlag = false
      )

      stubFor(
        get(urlPathEqualTo(url(30L, 1L)))
          .willReturn(
            aResponse()
              .withStatus(OK)
              .withBody(
                s"""
                   |{
                   |"taxIsDisplayNeededFlag":true,
                   |"interestIsDisplayNeededFlag":false,
                   |"paymentIsDisplayNeededFlag":true,
                   |"repayReallocIsDisplayNeededFlag":false
                   |}
                   |""".stripMargin
              )
          )
      )

      val result = connector.getDisplayNeeded(30L, 1L).futureValue
      result mustEqual response
    }

    "return INTERNAL_ERROR when BE failed" in {
      stubFor(
        get(urlPathEqualTo(url(999L, 1L)))
          .willReturn(
            aResponse()
              .withStatus(INTERNAL_SERVER_ERROR)
              .withBody("Error from downstream")
          )
      )

      val ex = intercept[Exception] {
        connector.getDisplayNeeded(999L, 1L).futureValue
      }
      ex.getMessage.toLowerCase must include("error from downstream")
    }

    "return 400 when BE returns BAD_REQUEST " in {
      stubFor(
        get(urlPathEqualTo(url(10L, 1L)))
          .willReturn(
            aResponse()
              .withStatus(BAD_REQUEST)
              .withBody("Invalid Request")
          )
      )

      val ex = intercept[Exception] {
        connector.getDisplayNeeded(10L, 1L).futureValue
      }
      ex.getMessage must include("Invalid Request")
    }

    "return 404 when BE returns NOT_FOUND " in {
      stubFor(
        get(urlPathEqualTo(url(10L, 1L)))
          .willReturn(
            aResponse()
              .withStatus(NOT_FOUND)
              .withBody("Not found")
          )
      )

      val ex = intercept[Exception] {
        connector.getDisplayNeeded(10L, 1L).futureValue
      }
      ex.getMessage must include("Not found")
    }
  }

}
