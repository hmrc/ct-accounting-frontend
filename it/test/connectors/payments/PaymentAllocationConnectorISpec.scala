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

package connectors.payments

import com.github.tomakehurst.wiremock.client.WireMock.*
import helpers.payments.PaymentAllocationHelper
import itutils.ApplicationWithWiremock
import org.scalatest.BeforeAndAfterEach
import org.scalatest.concurrent.{IntegrationPatience, ScalaFutures}
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec
import play.api.http.Status.*
import uk.gov.hmrc.http.HeaderCarrier

import java.time.LocalDate

class PaymentAllocationConnectorISpec
  extends AnyWordSpec
    with Matchers
    with ScalaFutures
    with IntegrationPatience
    with ApplicationWithWiremock
    with BeforeAndAfterEach
    with PaymentAllocationHelper {

  implicit val hc: HeaderCarrier = HeaderCarrier()

  private val connector: PaymentAllocationConnector = app.injector.instanceOf[PaymentAllocationConnector]

  "getPaymentType" should {

    def url(taxRef: Long, fromDate: LocalDate, toDate: LocalDate, userEnteredPaymentAmount: BigDecimal) =
      s"/corporation-tax/determine-payment-allocation/$taxRef?fromDate=$fromDate&toDate=$toDate&amount=$userEnteredPaymentAmount"

    "return a payment allocation list with single item from BE" in {
      stubFor(
        get(urlEqualTo(url(1L, LocalDate.of(2024, 8, 31), LocalDate.of(2025, 8, 31), BigDecimal(1000))))
          .willReturn(
            aResponse()
              .withStatus(OK)
              .withBody(
                s"""[
                   |  {
                   |    "paymentAllocation": [
                   |      {
                   |        "accountingPeriod": 10,
                   |        "apEndDate": "2024-08-31",
                   |        "amount": 10.0,
                   |        "effectiveDateOfPayment": "2024-08-31"
                   |      }
                   |    ]
                   |  }
                   |]""".stripMargin
              )
          )
      )

      val result = connector.getPaymentType(1L, Some(LocalDate.of(2024, 8, 31)), Some(LocalDate.of(2025, 8, 31)), BigDecimal(1000)).futureValue
      result mustBe paymentAllocation
    }

    "return a payment allocation list with multiple items from BE" in {
      stubFor(
        get(urlEqualTo(url(1L, LocalDate.of(2024, 8, 31), LocalDate.of(2025, 8, 31), BigDecimal(1000))))
          .willReturn(
            aResponse()
              .withStatus(OK)
              .withBody(
                s"""[
                   |  {
                   |    "paymentAllocation": [
                   |      {
                   |        "accountingPeriod": 10,
                   |        "apEndDate": "2024-08-31",
                   |        "amount": 10.0,
                   |        "effectiveDateOfPayment": "2024-08-31"
                   |      }
                   |    ]
                   |  },
                   |  {
                   |    "paymentAllocation": [
                   |      {
                   |        "accountingPeriod": 10,
                   |        "apEndDate": "2024-08-31",
                   |        "amount": 10.0,
                   |        "effectiveDateOfPayment": "2024-08-31"
                   |      }
                   |    ]
                   |  }
                   |]""".stripMargin
              )
          )
      )

      val result = connector.getPaymentType(1L, Some(LocalDate.of(2024, 8, 31)), Some(LocalDate.of(2025, 8, 31)), BigDecimal(1000)).futureValue
      result mustBe nonPaginatedPaymentAllocation
    }

    "return INTERNAL_ERROR when service failed" in {
      stubFor(
        get(urlEqualTo(url(1L, LocalDate.of(2024, 8, 31), LocalDate.of(2025, 8, 31), BigDecimal(1000))))
          .willReturn(
            aResponse()
              .withStatus(INTERNAL_SERVER_ERROR)
              .withBody(
                s"""{
                   |error" : "Failed to retrieve payment allocation from the BE"
                   |}""".stripMargin
              )
          )
      )

      val ex = intercept[Exception] {
        connector.getPaymentType(1L, Some(LocalDate.of(2024, 8, 31)), Some(LocalDate.of(2025, 8, 31)), BigDecimal(1000)).futureValue
      }
      
      ex.getMessage.toLowerCase must include("error")
    }
  }
}
