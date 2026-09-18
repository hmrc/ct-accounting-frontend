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
import helpers.LatePaymentInterestViewModelHelper
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.when
import org.scalatestplus.mockito.MockitoSugar
import org.scalatestplus.mockito.MockitoSugar.mock
import play.api.Application
import play.api.i18n.Messages
import play.api.inject.bind
import play.api.test.FakeRequest
import play.api.test.Helpers.*
import services.LatePaymentInterestService
import uk.gov.hmrc.http.HeaderCarrier
import views.html.LatePaymentInterestStillAccruingView

import scala.concurrent.Future

class LatePaymentInterestStillAccruingControllerSpec
    extends SpecBase
    with MockitoSugar
    with LatePaymentInterestViewModelHelper {

  private trait Fixture {
    implicit val hc: HeaderCarrier              = HeaderCarrier()
    val mockService: LatePaymentInterestService = mock[LatePaymentInterestService]
    val application: Application                =
      applicationBuilder()
        .overrides(bind[LatePaymentInterestService].toInstance(mockService))
        .build()
    implicit val msgs: Messages                 = messages(application)
  }

  "LatePaymentInterestStillAccruingController" - {

    "must return OK and correct view for GET" in new Fixture {
      when(mockService.getInterestAccrualList(any(), any(), any())(any()))
        .thenReturn(Future.successful(interestAccrualMultipleObjects))

      running(application) {
        val request = FakeRequest(GET, routes.LatePaymentInterestStillAccruingController.onPageLoad().url)
        val result  = route(application, request).value
        val view    = application.injector.instanceOf[LatePaymentInterestStillAccruingView]

        status(result) mustEqual OK
        contentAsString(result) mustEqual
          view(eqvViewModelOfInterestAccrualMultipleObjects)(
            request,
            messages(application)
          ).toString
      }

    }

    "must redirect to JourneyRecoveryController when exception occurs from BE " in new Fixture {
      when(mockService.getInterestAccrualList(any(), any(), any())(any[HeaderCarrier]))
        .thenReturn(Future.failed(new RuntimeException("Boom")))

      running(application) {
        val request = FakeRequest(GET, routes.LatePaymentInterestStillAccruingController.onPageLoad().url)
        val result  = route(application, request).value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual routes.JourneyRecoveryController.onPageLoad().url
      }

    }

  }

}
