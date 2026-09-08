/*
 * Copyright 2026 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
import helpers.DisplayNeededHelper

class DisplayNeededConnectorISpec
  extends AnyWordSpec
    with Matchers
    with ScalaFutures
    with IntegrationPatience
    with ApplicationWithWiremock
    with BeforeAndAfterEach
    with DisplayNeededHelper
    {

  implicit val hc: HeaderCarrier = HeaderCarrier()

  private val connector: DisplayNeededConnector = app.injector.instanceOf[DisplayNeededConnector]

  // TODO: add auth stub logic and relevant cases

  "getDisplayNeeded" should {

    def url(taxRef: Long, accPeriod: Long) =
      s"/corporation-tax/display-needed/$taxRef/$accPeriod"

    "return Display Needed with all flags set to false, populated from BE with status code OK" in {
      val response = displayNeededAllFalse

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
      val response = displayNeededAllTrue

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
      val response = displayNeededMixed

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
