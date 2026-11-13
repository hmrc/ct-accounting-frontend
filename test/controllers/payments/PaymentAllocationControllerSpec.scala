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

package controllers.payments

import base.SpecBase
import helpers.payments.PaymentAllocationHelper
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.when
import org.scalatestplus.mockito.MockitoSugar
import play.api.i18n.Messages
import play.api.inject.bind
import play.api.test.FakeRequest
import play.api.test.Helpers.*
import services.payments.PaymentAllocationService
import uk.gov.hmrc.http.HeaderCarrier
import views.html.payments.PaymentAllocationView

import scala.concurrent.Future

class PaymentAllocationControllerSpec extends SpecBase with MockitoSugar with PaymentAllocationHelper {

  private trait Setup {
    val mockService: PaymentAllocationService = mock[PaymentAllocationService]
    implicit val hc: HeaderCarrier            = HeaderCarrier()

    val application = applicationBuilder()
      .overrides(bind[PaymentAllocationService].toInstance(mockService))
      .build()

    val page                   = 1
    val pageUrl: Int => String = page =>
      s"/ct-accounting/find-your-payment/payment-results/payment-allocation?page=$page"

    implicit val msgs: Messages = messages(application)
  }

  "PaymentAllocationController" - {

    "return 200 and a successful response for payment allocation" in new Setup {
      when(
        mockService.getPaymentType(any(), any(), any(), any())(
          any[HeaderCarrier]
        )
      )
        .thenReturn(Future.successful(paymentAllocation))

      running(application) {
        val request = FakeRequest(GET, routes.PaymentAllocationController.onPageLoad(None).url)
        val result  = route(application, request).value
        val view    = application.injector.instanceOf[PaymentAllocationView]
        val vm      = paymentAllocationToVM(paymentAllocation, pageUrl)(msgs)

        status(result) mustEqual OK
        contentAsString(result) mustEqual view(vm)(request, messages(application)).toString
      }
    }

    "return 303 and redirect to Journey recovery" in new Setup {
      when(
        mockService.getPaymentType(any(), any(), any(), any())(
          any[HeaderCarrier]
        )
      )
        .thenReturn(Future.failed(new RuntimeException("error")))

      running(application) {
        val request = FakeRequest(GET, routes.PaymentAllocationController.onPageLoad(None).url)
        val result  = route(application, request).value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual controllers.routes.JourneyRecoveryController.onPageLoad().url
      }
    }
  }
}
