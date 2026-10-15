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
import controllers.routes
import helpers.gpa.GpaDetailsGroupPaymentHelper
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.when
import org.scalatestplus.mockito.MockitoSugar
import play.api.Application
import play.api.i18n.Messages
import play.api.inject.bind
import play.api.test.FakeRequest
import play.api.test.Helpers.*
import services.gpa.GpaDetailsGroupPaymentService
import uk.gov.hmrc.http.HeaderCarrier
import viewmodels.gpa.GpaDetailsGroupPaymentArrangementPageViewModel
import views.html.gpa.GpaDetailsGroupPaymentArrangementsView

import scala.concurrent.Future

class GpaDetailsGroupPaymentControllerSpec extends SpecBase with MockitoSugar with GpaDetailsGroupPaymentHelper {

  private trait Fixture {
    implicit val hc: HeaderCarrier                 = HeaderCarrier()
    val mockService: GpaDetailsGroupPaymentService = mock[GpaDetailsGroupPaymentService]

    val application: Application = applicationBuilder()
      .overrides(bind[GpaDetailsGroupPaymentService].toInstance(mockService))
      .build()

    val pageUrl: Int => String       = page => s"/group-payment-arrangement?page=$page"
    val paginationIndex: Option[Int] = Some(1)

    implicit val msgs: Messages = messages(application)
  }

  "GpaDetailsGroupPaymentController" - {
    "must return OK on GET with correct view " in new Fixture {
      when(mockService.getViewModel(any(), any(), any(), any())(any[HeaderCarrier], any[Messages]))
        .thenReturn(Future.successful(viewModelResponseRfrReallocation))

      val pageViewModel: GpaDetailsGroupPaymentArrangementPageViewModel =
        returnGroupPaymentPageViewModel(viewModelResponseRfrReallocation, paginationIndex, pageUrl)

      running(application) {
        val request =
          FakeRequest(GET, controllers.gpa.routes.GpaDetailsGroupPaymentController.onPageLoad(paginationIndex).url)
        val result  = route(application, request).value
        val view    = application.injector.instanceOf[GpaDetailsGroupPaymentArrangementsView]

        status(result) mustEqual OK
        contentAsString(result) mustEqual
          view(pageViewModel)(
            request,
            messages(application)
          ).toString
      }

    }

    "must redirect to Journey Recovery Controller when exception from BE occurs" in new Fixture {
      when(mockService.getViewModel(any(), any(), any(), any())(any[HeaderCarrier], any[Messages]))
        .thenReturn(Future.failed(RuntimeException("Error")))

      running(application) {
        val request =
          FakeRequest(GET, controllers.gpa.routes.GpaDetailsGroupPaymentController.onPageLoad(paginationIndex).url)
        val result  = route(application, request).value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual routes.JourneyRecoveryController.onPageLoad().url
      }

    }

  }
}
