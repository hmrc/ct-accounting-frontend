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
import org.mockito.ArgumentMatchers.{any, eq as eqTo}
import org.mockito.Mockito.when
import org.scalatestplus.mockito.MockitoSugar
import play.api.inject.bind
import play.api.test.FakeRequest
import play.api.test.Helpers.*
import connectors.InterestAccrualListConnector
import uk.gov.hmrc.http.HeaderCarrier
import views.html.accountingPeriods.CreditInterestView
import helpers.CreditInterestHelper

import scala.concurrent.Future

class CreditInterestControllerSpec extends SpecBase with MockitoSugar with CreditInterestHelper {
  implicit val hc: HeaderCarrier                  = HeaderCarrier()
  val mockConnector: InterestAccrualListConnector = mock[InterestAccrualListConnector]

  // TODO: hardcoded value in the controller until it's wired up to session data

  "CreditInterest Controller" - {

    "must return OK and the correct credit interest view for a GET when interest type equals RIN" in {

      when(mockConnector.getInterestAccrualList(eqTo(1L), eqTo(1L), eqTo("ICR"))(any[HeaderCarrier]))
        .thenReturn(Future.successful(interestAccrualMultipleObjects))

      val application = applicationBuilder()
        .overrides(bind[InterestAccrualListConnector].toInstance(mockConnector))
        .build()

      running(application) {
        val request = FakeRequest(GET, routes.CreditInterestController.onPageLoad().url)
        val result  = route(application, request).value
        val view    = application.injector.instanceOf[CreditInterestView]

        status(result) mustEqual OK
        contentAsString(result) mustEqual
          view(eqvViewModelOfInterestAccrualMultipleObjects)(
            request,
            messages(application)
          ).toString
      }
    }

    "must redirect when exception from BE occurs" in {

      when(mockConnector.getInterestAccrualList(eqTo(1L), eqTo(1L), eqTo("ICR"))(any[HeaderCarrier]))
        .thenReturn(Future.failed(RuntimeException("Error")))

      val application = applicationBuilder()
        .overrides(bind[InterestAccrualListConnector].toInstance(mockConnector))
        .build()

      running(application) {
        val request = FakeRequest(GET, routes.CreditInterestController.onPageLoad().url)
        val result  = route(application, request).value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual routes.JourneyRecoveryController.onPageLoad().url
      }

    }
  }
}
