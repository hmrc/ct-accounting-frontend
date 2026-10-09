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
import helpers.AccountingPeriodOverviewHelper
import itutils.ApplicationWithWiremock
import org.scalatest.BeforeAndAfterEach
import org.scalatest.concurrent.{IntegrationPatience, ScalaFutures}
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec
import play.api.http.Status.*
import uk.gov.hmrc.http.HeaderCarrier

class AccountingPeriodOverviewConnectorISpec
  extends AnyWordSpec
    with Matchers
    with ScalaFutures
    with IntegrationPatience
    with ApplicationWithWiremock
    with BeforeAndAfterEach
    with AccountingPeriodOverviewHelper {

  implicit val hc: HeaderCarrier = HeaderCarrier()

  private val connector: AccountingPeriodOverviewConnector =
    app.injector.instanceOf[AccountingPeriodOverviewConnector]

  "getAccountingPeriodOverview" should {

    def url(taxRef: Long, accPeriod: Long) =
      s"/corporation-tax/accounting-period-overview/$taxRef/$accPeriod"

    "return an AccountingPeriodOverview from BE" in {
      stubFor(
        get(urlPathEqualTo(url(1L, 1L)))
          .willReturn(
            aResponse()
              .withStatus(OK)
              .withHeader("Content-Type", "application/json")
              .withBody(
                s"""{
                   |"accountingPeriod": 1,
                   |"apStartDate": "2025-01-01",
                   |"apEndDate": "2026-01-01",
                   |"apStatus": "OPEN",
                   |"taxChargePresent": true,
                   |"clericalIntSig": true,
                   |"creditDebitInterestInd": true,
                   |"taxTotal": 100.00,
                   |"interestTotal": 150.00,
                   |"penaltyTotal": 100.00,
                   |"payslipTotal": 300.00,
                   |"repayReallocTotal": 250.00,
                   |"adjustmentTotal": 155,
                   |"clericalCalculationFlag": true,
                   |"taxIsDisplayNeededFlag": true,
                   |"interestIsDisplayNeededFlag": true,
                   |"paymentIsDisplayNeededFlag": true,
                   |"repayReallocIsDisplayNeededFlag": true
                   |}""".stripMargin
              )
          )
      )

      val result =
        connector.getAccountingPeriodOverview(1L, 1L).futureValue

      result mustBe accountingPeriodOverviewResponse
    }

    "return INTERNAL_ERROR when BE failed" in {
      stubFor(
        get(urlPathEqualTo(url(1L, 1L)))
          .willReturn(
            aResponse()
              .withStatus(INTERNAL_SERVER_ERROR)
              .withBody(
                s"""{
                   |"error": "Failed to retrieve accounting period overview"
                   |}""".stripMargin
              )
          )
      )

      val ex = intercept[Exception] {
        connector.getAccountingPeriodOverview(1L, 1L).futureValue
      }
      ex.getMessage.toLowerCase must include("error")
    }
  }
}
