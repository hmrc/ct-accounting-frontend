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
import helpers.AccountingPeriodsHelper
import itutils.ApplicationWithWiremock
import org.scalatest.BeforeAndAfterEach
import org.scalatest.concurrent.{IntegrationPatience, ScalaFutures}
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec
import play.api.http.Status.*
import uk.gov.hmrc.http.HeaderCarrier

class AccountingPeriodsConnectorISpec
    extends AnyWordSpec
    with Matchers
    with ScalaFutures
    with IntegrationPatience
    with ApplicationWithWiremock
    with BeforeAndAfterEach
    with AccountingPeriodsHelper {

  implicit val hc: HeaderCarrier = HeaderCarrier()

  private val connector: AccountingPeriodsConnector = app.injector.instanceOf[AccountingPeriodsConnector]

  "getAccountingPeriods" should {

    def url(taxRef: Long) =
      s"/corporation-tax/accounting-periods/$taxRef"

    "return a successful an empty AccountingPeriods list from BE" in {
      stubFor(
        get(urlPathEqualTo(url(1L)))
          .willReturn(
            aResponse()
              .withStatus(OK)
              .withBody(
                s"""{
                   |"accountingPeriods":
                   |[
                   |]}""".stripMargin
              )
          )
      )

      val result = connector.getAccountingPeriods(1L).futureValue
      result.accountingPeriods must contain allElementsOf emptyAccountingPeriods.accountingPeriods
    }

    "return an AccountingPeriod list with single item from BE" in {
      stubFor(
        get(urlPathEqualTo(url(1L)))
          .willReturn(
            aResponse()
              .withStatus(OK)
              .withBody(
                s"""{
                   |"accountingPeriods": [
                   |{
                   |"accountingPeriod": 1,
                   |"apStartDate": "2023-04-01",
                   |"apEndDate": "2024-03-31",
                   |"apStatus": "O",
                   |"taxChargePresent": true,
                   |"clericalIntSig": false,
                   |"creditDebitInterestInd": true,
                   |"taxTotal": 1500.50,
                   |"interestTotal": 25.75,
                   |"penaltyTotal": 0.00,
                   |"payslipTotal": 1000.00,
                   |"repayReallocTotal": 50.25,
                   |"adjustmentTotal": 10.00
                   |}
                   | ]
                   |}
                   |""".stripMargin
              )
          )
      )

      val result = connector.getAccountingPeriods(1L).futureValue
      result.accountingPeriods must contain allElementsOf accountingPeriodsWithOneItem.accountingPeriods
    }

    "return an AccountingPeriods list with multiple items from BE" in {
      stubFor(
        get(urlPathEqualTo(url(1L)))
          .willReturn(
            aResponse()
              .withStatus(OK)
              .withBody(
                s"""{
                   | "accountingPeriods": [
                    {
                   | "accountingPeriod": 1,
                   | "apStartDate": "2023-04-01",
                   | "apEndDate": "2024-03-31",
                   | "apStatus": "O",
                   | "taxChargePresent": true,
                   | "clericalIntSig": false,
                   | "creditDebitInterestInd": true,
                   | "taxTotal": 1500.50,
                   | "interestTotal": 25.75,
                   | "penaltyTotal": 0.00,
                   | "payslipTotal": 1000.00,
                   | "repayReallocTotal": 50.25,
                   | "adjustmentTotal": 10.00
                   | },
                   |{
                   |"accountingPeriod": 2,
                   |"apStartDate": "2022-04-01",
                   |"apEndDate": "2023-03-31",
                   |"apStatus": "C",
                   |"taxChargePresent": false,
                   |"clericalIntSig": true,
                   |"creditDebitInterestInd": false,
                   |"taxTotal": 2200.00,
                   |"interestTotal": 0.00,
                   |"penaltyTotal": 100.00,
                   |"payslipTotal": 2000.00,
                   |"repayReallocTotal": 0.00,
                   |"adjustmentTotal": 100.00
                  }
                ]
                   |}""".stripMargin
              )
          )
      )

      val result = connector.getAccountingPeriods(1L).futureValue
      result.accountingPeriods must contain allElementsOf accountingPeriodsMultipleItems.accountingPeriods
    }

    "return INTERNAL_ERROR when service failed" in {
      stubFor(
        get(urlPathEqualTo(url(1)))
          .willReturn(
            aResponse()
              .withStatus(INTERNAL_SERVER_ERROR)
              .withBody(
                s"""{
                   |error" : "Failed to retrieve adjustment transactions from the BE"
                   |}""".stripMargin
              )
          )
      )

      val ex = intercept[Exception] {
        connector.getAccountingPeriods(1L).futureValue
      }
      ex.getMessage.toLowerCase must include("error")
    }
  }
}
