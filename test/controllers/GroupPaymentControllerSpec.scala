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

package controllers

import base.SpecBase
import helpers.GroupPaymentHelper
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.when
import org.scalatestplus.mockito.MockitoSugar
import play.api.i18n.{Lang, Messages, MessagesApi, MessagesImpl}
import play.api.inject.bind
import play.api.test.FakeRequest
import play.api.test.Helpers.*
import services.GroupPaymentService
import uk.gov.hmrc.http.HeaderCarrier
import views.html.GroupPaymentArrangementsView
import scala.concurrent.Future

class GroupPaymentControllerSpec extends SpecBase with MockitoSugar with GroupPaymentHelper {

  implicit val hc: HeaderCarrier       = HeaderCarrier()
  val mockService: GroupPaymentService = mock[GroupPaymentService]

  val application = applicationBuilder()
    .overrides(bind[GroupPaymentService].toInstance(mockService))
    .build()

  implicit val messagesApi: MessagesApi = application.injector.instanceOf[MessagesApi]
  implicit val messages: Messages       = MessagesImpl(Lang.defaultLang, messagesApi)

  "GroupPaymentController" - {

    "must return OK on GET" in {

      when(mockService.getViewModel(any(), any(), any(), any())(any[HeaderCarrier]))
        .thenReturn(Future.successful(viewModel))

      val testApplication = applicationBuilder()
        .overrides(bind[GroupPaymentService].toInstance(mockService))
        .build()

      running(testApplication) {
        val request = FakeRequest(GET, routes.GroupPaymentController.onPageLoad().url)
        val result  = route(application, request).value
        val view    = application.injector.instanceOf[GroupPaymentArrangementsView]

        status(result) mustEqual OK
        contentAsString(result) mustEqual
          view(viewModel)(
            request,
            messages(application)
          ).toString
      }

    }

    "must redirect when exception from BE occurs" in {

      when(mockService.getViewModel(any(), any(), any(), any())(any[HeaderCarrier]))
        .thenReturn(Future.failed(RuntimeException("Error")))

      val testApplication = applicationBuilder()
        .overrides(bind[GroupPaymentService].toInstance(mockService))
        .build()

      running(testApplication) {
        val request = FakeRequest(GET, routes.GroupPaymentController.onPageLoad().url)
        val result  = route(application, request).value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual routes.JourneyRecoveryController.onPageLoad().url
      }

    }

  }
}
