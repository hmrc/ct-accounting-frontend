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
import helpers.InterestAccrualListHelper
import itutils.ApplicationWithWiremock
import org.scalatest.BeforeAndAfterEach
import org.scalatest.concurrent.{IntegrationPatience, ScalaFutures}
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec
import play.api.http.Status.*
import uk.gov.hmrc.http.HeaderCarrier

class InterestAccrualListConnectorISpec
    extends AnyWordSpec
    with Matchers
    with ScalaFutures
    with IntegrationPatience
    with ApplicationWithWiremock
    with BeforeAndAfterEach
    with InterestAccrualListHelper {

  implicit val hc: HeaderCarrier = HeaderCarrier()

  private val connector: InterestAccrualListConnector = app.injector.instanceOf[InterestAccrualListConnector]

  "getInterestAccrualList" should {

    def url(taxRef: Long, accPeriod: Long, interestType: String) =
      s"/corporation-tax/interest-accrual-list/$taxRef/$accPeriod/$interestType"

    "return a successful an empty InterestAccrualList list from BE" in {
      stubFor(
        get(urlPathEqualTo(url(1L, 5L, "IDE")))
          .willReturn(
            aResponse()
              .withStatus(OK)
              .withBody(
                s"""{
                   |"interestAccruals":
                   |[]
                   |}""".stripMargin
              )
          )
      )

      val result = connector.getInterestAccrualList(1L, 5L, "IDE").futureValue
      result.interestAccruals must contain allElementsOf emptyInterestAccrualList.interestAccruals
    }

    "return a InterestAccrualList list with single item from BE" in {
      stubFor(
        get(urlPathEqualTo(url(1L, 5L, "ICR")))
          .willReturn(
            aResponse()
              .withStatus(OK)
              .withBody(
                s"""{
                   | "interestAccruals": [
                   | {
                   | "computationAmount": 10000.00,
                   | "interestAccrualFromDate": "2024-04-01",
                   | "interestAccrualToDate": "2024-06-30",
                   | "interestRate": 7.75,
                   | "interestAmount": 193.22,
                   | "apEndDate": "2024-03-31",
                   | "noOfDays": 91
                   |  }
                   | ]
                   |}""".stripMargin
              )
          )
      )

      val result = connector.getInterestAccrualList(1L, 5L, "ICR").futureValue
      result.interestAccruals must contain allElementsOf interestAccrualSingleObject.interestAccruals
    }

    "return an InterestAccrualList with multiple items from BE" in {
      stubFor(
        get(urlPathEqualTo(url(1L, 2L, "RIN")))
          .willReturn(
            aResponse()
              .withStatus(OK)
              .withBody(
                s"""{
                   |  "interestAccruals": [
                   |    {
                   |      "computationAmount": 10000.00,
                   |      "interestAccrualFromDate": "2024-04-01",
                   |      "interestAccrualToDate": "2024-06-30",
                   |      "interestRate": 7.75,
                   |      "interestAmount": 193.22,
                   |      "apEndDate": "2024-03-31",
                   |      "noOfDays": 91
                   |    },
                   |    {
                   |      "computationAmount": 10000.00,
                   |      "interestAccrualFromDate": "2024-07-01",
                   |      "interestAccrualToDate": "2024-09-30",
                   |      "interestRate": 8.25,
                   |      "interestAmount": 208.02,
                   |      "apEndDate": "2024-03-31",
                   |      "noOfDays": 92
                   |    }
                   |  ]
                   |}""".stripMargin
              )
          )
      )

      val result = connector.getInterestAccrualList(1L, 2L, "RIN").futureValue
      result.interestAccruals must contain allElementsOf interestAccrualMultipleObjects.interestAccruals
    }

    "return INTERNAL_ERROR when service failed" in {
      stubFor(
        get(urlPathEqualTo(url(1L, 2L, "ICR")))
          .willReturn(
            aResponse()
              .withStatus(INTERNAL_SERVER_ERROR)
              .withBody(
                s"""{
                   |error" : "Failed to retrieve InterestAccrualList from the BE"
                   |}""".stripMargin
              )
          )
      )

      val ex = intercept[Exception] {
        connector.getInterestAccrualList(1L, 2L, "ICR").futureValue
      }
      ex.getMessage.toLowerCase must include("error")
    }
  }
}
