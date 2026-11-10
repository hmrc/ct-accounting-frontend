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

package services.payments

import connectors.payments.PaymentResultsConnector
import helpers.payments.PaymentResultsHelper
import models.payments.PaymentSearchResultResponse
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.{times, verify, when}
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.mockito.MockitoSugar
import play.api.mvc.ControllerComponents
import play.api.test.Helpers.stubControllerComponents
import uk.gov.hmrc.http.HeaderCarrier

import java.time.LocalDate
import scala.concurrent.{ExecutionContext, Future}

class PaymentResultsServiceSpec
    extends AnyWordSpec
    with Matchers
    with ScalaFutures
    with MockitoSugar
    with PaymentResultsHelper {

  private trait BaseSetup {
    implicit val hc: HeaderCarrier = HeaderCarrier()

    private val cc: ControllerComponents = stubControllerComponents()
    implicit val ec: ExecutionContext    = cc.executionContext

    val mockConnector: PaymentResultsConnector = mock[PaymentResultsConnector]
    val service                                = new PaymentResultsService(mockConnector)
    val taxRef: Long                           = 1111444444L;
    val fromDate: Option[LocalDate]            = Some(LocalDate.of(2005, 1, 1));
    val toDate: Option[LocalDate]              = Some(LocalDate.of(2008, 1, 1));
    val userEnteredPaymentAmount: BigDecimal   = 38000;

  }

  "PaymentResultsService.getPaymentSearchResult" should {

    "delegate to connector and successfully return PaymentSearchResultResponse" in new BaseSetup {

      when(mockConnector.getPaymentResults(any(), any(), any(), any())(any[HeaderCarrier]))
        .thenReturn(Future.successful(multiplePaymentSearchResult))

      val result: PaymentSearchResultResponse =
        service.getPaymentSearchResult(taxRef, userEnteredPaymentAmount, fromDate, toDate).futureValue

      result shouldBe multiplePaymentSearchResult

      verify(mockConnector).getPaymentResults(taxRef, userEnteredPaymentAmount, fromDate, toDate)

      verify(mockConnector, times(1)).getPaymentResults(taxRef, userEnteredPaymentAmount, fromDate, toDate)

    }

    "delegate to connector and successfully return an empty PaymentSearchResultResponse" in new BaseSetup {

      when(mockConnector.getPaymentResults(any(), any(), any(), any())(any[HeaderCarrier]))
        .thenReturn(Future.successful(emptyPaymentSearchResult))

      val result: PaymentSearchResultResponse =
        service.getPaymentSearchResult(taxRef, userEnteredPaymentAmount, fromDate, toDate).futureValue

      result shouldBe emptyPaymentSearchResult

      verify(mockConnector).getPaymentResults(taxRef, userEnteredPaymentAmount, fromDate, toDate)

      verify(mockConnector, times(1)).getPaymentResults(taxRef, userEnteredPaymentAmount, fromDate, toDate)

    }

    "propagate any errors or exceptions from connector" in new BaseSetup {

      when(mockConnector.getPaymentResults(any(), any(), any(), any())(any[HeaderCarrier]))
        .thenReturn(Future.failed(new RuntimeException("boom")))

      val ex: RuntimeException = intercept[RuntimeException] {
        service.getPaymentSearchResult(taxRef, userEnteredPaymentAmount, fromDate, toDate).futureValue
      }

      ex.getMessage should include("boom")

      verify(mockConnector, times(1)).getPaymentResults(taxRef, userEnteredPaymentAmount, fromDate, toDate)

    }

  }

}
