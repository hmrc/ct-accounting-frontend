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
import models.AccountingPeriodDetails
import org.scalatest.BeforeAndAfterEach
import org.scalatest.concurrent.{IntegrationPatience, ScalaFutures}
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec
import play.api.http.Status.{BAD_REQUEST, INTERNAL_SERVER_ERROR, NOT_FOUND, OK}
import uk.gov.hmrc.http.HeaderCarrier

class InterestCorporationTaxConnectorISpec
    extends AnyWordSpec
    with Matchers
    with ScalaFutures
    with IntegrationPatience
    with ApplicationWithWiremock
    with BeforeAndAfterEach {

  implicit val hc: HeaderCarrier = HeaderCarrier()

  private val connector: InterestCorporationTaxConnector = app.injector.instanceOf[InterestCorporationTaxConnector]

  // TODO: add auth stub logic and relevant cases

  "getAccountingPeriodResponse" should {

    def url(taxRef: Long, accPeriod: Long) =
      s"/corporation-tax/accounting-period-details/$taxRef/$accPeriod"

    "return AccountingPeriodDetails with all fields populated from BE with status code OK" in {
      val response = AccountingPeriodDetails(
          isApBalanced = false,
          lpiCalcFlag = false,
          crDbCalcFlag = true,
          creditInterestAmount = BigDecimal("0.00"),
          debitInterestAmount = BigDecimal("15.75"),
          latePaymentInterestAmount = BigDecimal("25.50"),
          repaymentInterestAmount = BigDecimal("0.00"),
          totalDerivedActualInterest = BigDecimal("41.25"),
          amountDueForAp = BigDecimal("2500.00")
        )
      stubFor(
        get(urlPathEqualTo(url(1L, 5L)))
          .willReturn(
            aResponse()
              .withStatus(OK)
              .withBody(
                s"""
                   |{
                   |"isApBalanced": false,
                   |"lpiCalcFlag": false,
                   |"crDbCalcFlag": true,
                   |"creditInterestAmount": 0.00,
                   |"debitInterestAmount": 15.75,
                   |"latePaymentInterestAmount": 25.50,
                   |"repaymentInterestAmount": 0.00,
                   |"totalDerivedActualInterest": 41.25,
                   |"amountDueForAp": 2500.00
                   |}
                   |""".stripMargin
              )
          )
      )

      val result = connector.getAccountingPeriodResponse(1L, 5L).futureValue
      result mustEqual response
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
        connector.getAccountingPeriodResponse(1L, 5L).futureValue
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
        connector.getAccountingPeriodResponse(1L, 5L).futureValue
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
        connector.getAccountingPeriodResponse(1L, 5L).futureValue
      }
      ex.getMessage must include("Not found")
    }
  }

}
