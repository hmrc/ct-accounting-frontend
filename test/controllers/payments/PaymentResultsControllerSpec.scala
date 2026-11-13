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

package controllers.gpa

import base.SpecBase
import controllers.payments.routes
import helpers.payments.PaymentResultsHelper
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.when
import org.scalatestplus.mockito.MockitoSugar
import play.api.i18n.Messages
import play.api.inject.bind
import play.api.test.FakeRequest
import play.api.test.Helpers.*
import services.payments.PaymentResultsService
import uk.gov.hmrc.http.HeaderCarrier
import views.html.payments.PaymentResultsView

import scala.concurrent.Future

class PaymentResultsControllerSpec extends SpecBase with MockitoSugar with PaymentResultsHelper {

  private trait Setup {
    val mockService: PaymentResultsService = mock[PaymentResultsService]
    implicit val hc: HeaderCarrier         = HeaderCarrier()

    val application = applicationBuilder()
      .overrides(bind[PaymentResultsService].toInstance(mockService))
      .build()

    val taxRef                            = 1111444444L
    val userEnteredPaymentAmountString    = "£38,000.00"
    val fromDateStringOpt: Option[String] = Some("01 Jan 2006")
    val toDateStringOpt: Option[String]   = Some("01 Jan 2008")
    val page                              = 2
    val pageUrl: Int => String            =
      pageNumber => routes.PaymentResultsController.onPageLoad(Some(pageNumber)).url

    implicit val msgs: Messages = messages(application)
  }

  "PaymentResultsController" - {

    "return 200 and a successful response for payment results" in new Setup {
      when(
        mockService.getPaymentSearchResult(any(), any(), any(), any())(
          any[HeaderCarrier]
        )
      )
        .thenReturn(Future.successful(multiplePaymentSearchResult))

      running(application) {
        val request = FakeRequest(GET, routes.PaymentResultsController.onPageLoad(None).url)
        val result  = route(application, request).value
        val view    = application.injector.instanceOf[PaymentResultsView]
        val vm      = paymentSearchResultToVM(multiplePaymentSearchResult, taxRef, pageUrl, Some(1))(msgs)

        status(result) mustEqual OK
        routes.PaymentResultsController
          .onPageLoad(None)
          .url mustEqual "/ct-accounting/find-your-payment/payment-results"
        contentAsString(result) mustEqual view(
          vm,
          userEnteredPaymentAmountString,
          15,
          fromDateStringOpt,
          toDateStringOpt
        )(request, messages(application)).toString
      }
    }

    "return 200 and a successful response for payment results when page number given" in new Setup {
      when(
        mockService.getPaymentSearchResult(any(), any(), any(), any())(
          any[HeaderCarrier]
        )
      )
        .thenReturn(Future.successful(multiplePaymentSearchResult))

      running(application) {
        val request = FakeRequest(GET, routes.PaymentResultsController.onPageLoad(Some(2)).url)
        val result  = route(application, request).value
        val view    = application.injector.instanceOf[PaymentResultsView]
        val vm      = paymentSearchResultToVM(multiplePaymentSearchResult, taxRef, pageUrl, Some(2))(msgs)

        status(result) mustEqual OK
        request.uri mustEqual "/ct-accounting/find-your-payment/payment-results?page=2"
        contentAsString(result) mustEqual view(
          vm,
          userEnteredPaymentAmountString,
          15,
          fromDateStringOpt,
          toDateStringOpt
        )(request, messages(application)).toString
      }
    }

    "return 303 and redirect to Journey recovery" in new Setup {
      when(
        mockService.getPaymentSearchResult(any(), any(), any(), any())(
          any[HeaderCarrier]
        )
      )
        .thenReturn(Future.failed(new RuntimeException("error")))

      running(application) {
        val request = FakeRequest(GET, routes.PaymentResultsController.onPageLoad(None).url)
        val result  = route(application, request).value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual controllers.routes.JourneyRecoveryController.onPageLoad().url
      }
    }
  }
}
