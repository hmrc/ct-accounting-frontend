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
import helpers.payments.PaymentResultsHelper
import itutils.ApplicationWithWiremock
import org.scalatest.BeforeAndAfterEach
import org.scalatest.concurrent.{IntegrationPatience, ScalaFutures}
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec
import play.api.http.Status.*
import play.api.libs.json.Json
import uk.gov.hmrc.http.HeaderCarrier

import java.time.LocalDate

class PaymentResultsConnectorIsSpec
    extends AnyWordSpec
    with Matchers
    with ScalaFutures
    with IntegrationPatience
    with ApplicationWithWiremock
    with BeforeAndAfterEach
    with PaymentResultsHelper {

  implicit val hc: HeaderCarrier = HeaderCarrier()

  private val connector: PaymentResultsConnector = app.injector.instanceOf[PaymentResultsConnector]

  private val taxRef: Long         = 1111444444L;
  private val fromDate: Option[LocalDate] = Some(LocalDate.of(2005, 1, 1));
  private val toDate: Option[LocalDate]      = Some(LocalDate.of(2008, 1, 1));
  private val userEnteredPaymentAmount: BigDecimal = 38000;

  "getPaymentResults" should {

    def url(taxRef: Long, userEnteredPaymentAmount: BigDecimal, fromDate: Option[LocalDate], toDate: Option[LocalDate]) =
      s"/corporation-tax/payment-search-result/$taxRef?fromDate=2005-01-01&toDate=2008-01-01&amount=$userEnteredPaymentAmount"

    "return Payment Results with status code OK" in {
      stubFor(
        get(urlEqualTo(url(taxRef,userEnteredPaymentAmount, fromDate, toDate)))
          .willReturn(
            aResponse()
              .withStatus(OK)
              .withBody(Json.stringify(Json.toJson(multiplePaymentSearchResult)))
          )
      )

      val result = connector.getPaymentResults(taxRef,userEnteredPaymentAmount, fromDate, toDate).futureValue
      result mustEqual multiplePaymentSearchResult

    }

    "return empty Payment Results with status code OK" in {
      stubFor(
        get(urlEqualTo(url(taxRef, userEnteredPaymentAmount, fromDate, toDate)))
          .willReturn(
            aResponse()
              .withStatus(OK)
              .withBody(Json.stringify(Json.toJson(emptyPaymentSearchResult)))
          )
      )

      val result = connector.getPaymentResults(taxRef, userEnteredPaymentAmount, fromDate, toDate).futureValue
      result mustEqual emptyPaymentSearchResult

    }

    "return INTERNAL_ERROR when BE failed" in {
      stubFor(
        get(urlEqualTo(url(taxRef,userEnteredPaymentAmount, fromDate, toDate)))
          .willReturn(
            aResponse()
              .withStatus(INTERNAL_SERVER_ERROR)
              .withBody("Error")
          )
      )

      val ex = intercept[Exception] {
        connector.getPaymentResults(taxRef,userEnteredPaymentAmount, fromDate, toDate).futureValue
      }
      ex.getMessage.toLowerCase must include("error")
    }

    "return 400 when BE returns BAD_REQUEST " in {
      stubFor(
          get(urlEqualTo(url(taxRef,userEnteredPaymentAmount, fromDate, toDate)))
            .willReturn(
              aResponse()
                .withStatus(BAD_REQUEST)
              .withBody("Invalid Request")
          )
      )

      val ex = intercept[Exception] {
        connector.getPaymentResults(taxRef,userEnteredPaymentAmount, fromDate, toDate).futureValue
      }
      ex.getMessage must include("Invalid Request")
    }

    "return 404 when BE returns NOT_FOUND " in {
      stubFor(
        get(urlEqualTo(url(taxRef,userEnteredPaymentAmount, fromDate, toDate)))
          .willReturn(
            aResponse()
              .withStatus(NOT_FOUND)
              .withBody("Not found")
          )
      )

      val ex = intercept[Exception] {
        connector.getPaymentResults(taxRef,userEnteredPaymentAmount, fromDate, toDate).futureValue
      }
      ex.getMessage must include("Not found")
    }
  }
}
