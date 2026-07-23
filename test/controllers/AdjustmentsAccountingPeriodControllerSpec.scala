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
import helpers.AdjustmentsDataHelper
import org.mockito.ArgumentMatchers.{any, eq as eqTo}
import services.AdjustmentsAccountingPeriodService
import org.mockito.Mockito.when
import org.scalatestplus.mockito.MockitoSugar
import play.api.inject.bind
import play.api.test.FakeRequest
import play.api.test.Helpers.*
import uk.gov.hmrc.http.HeaderCarrier
import views.html.AdjustmentsAccountingPeriodView

import scala.concurrent.Future

class AdjustmentsAccountingPeriodControllerSpec extends SpecBase with MockitoSugar with AdjustmentsDataHelper {

  private trait Setup {
    val mockService: AdjustmentsAccountingPeriodService = mock[AdjustmentsAccountingPeriodService]
    implicit val hc: HeaderCarrier                      = HeaderCarrier()

    val adjustmentsRoute = routes.AdjustmentsAccountingPeriodController.onPageLoad().url

    val application = applicationBuilder()
      .overrides(bind[AdjustmentsAccountingPeriodService].toInstance(mockService))
      .build()
  }

  "AdjustmentsAccountingPeriodController" - {

    "return 200 and a successful response with one item" in new Setup {
      when(mockService.getAdjustmentTransactions(eqTo(3060600983L), eqTo(4L))(any[HeaderCarrier]))
        .thenReturn(Future.successful(adjustmentTransactionsListWithOneItem))

      running(application) {
        val request = FakeRequest(GET, routes.AdjustmentsAccountingPeriodController.onPageLoad().url)
        val result  = route(application, request).value
        val view    = application.injector.instanceOf[AdjustmentsAccountingPeriodView]

        status(result) mustEqual OK
        contentAsString(result) mustEqual view(adjustmentsViewModelWithOneItem)(request, messages(application)).toString
      }
    }

    "return 200 and a successful response with multiple items" in new Setup {
      when(mockService.getAdjustmentTransactions(eqTo(3060600983L), eqTo(4L))(any[HeaderCarrier]))
        .thenReturn(Future.successful(adjustmentTransactionsListWithMultipleItems))

      running(application) {
        val request = FakeRequest(GET, routes.AdjustmentsAccountingPeriodController.onPageLoad().url)
        val result  = route(application, request).value
        val view    = application.injector.instanceOf[AdjustmentsAccountingPeriodView]

        status(result) mustEqual OK
        contentAsString(result) mustEqual view(adjustmentsViewModelWithMultipleItems)(
          request,
          messages(application)
        ).toString
      }
    }

    "return 303 and redirect to Journey recovery" in new Setup {
      when(mockService.getAdjustmentTransactions(eqTo(3060600983L), eqTo(4L))(any[HeaderCarrier]))
        .thenReturn(Future.failed(new RuntimeException("error")))

      running(application) {
        val request = FakeRequest(GET, routes.AdjustmentsAccountingPeriodController.onPageLoad().url)
        val result  = route(application, request).value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual controllers.routes.JourneyRecoveryController.onPageLoad().url
      }
    }
  }
}
