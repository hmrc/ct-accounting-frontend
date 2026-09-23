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
import helpers.RepaymentsReallocationsHelper
import org.mockito.ArgumentMatchers.{any, eq as eqTo}
import org.mockito.Mockito.when
import org.scalatestplus.mockito.MockitoSugar
import play.api.Application
import play.api.i18n.Messages
import play.api.inject.bind
import play.api.test.FakeRequest
import play.api.test.Helpers.*
import services.RepaymentsReallocationService
import uk.gov.hmrc.http.HeaderCarrier
import viewmodels.RepaymentReallocationSummaryViewModel
import views.html.RepaymentsReallocationsView

import java.time.LocalDate
import scala.concurrent.Future

class RepaymentsReallocationsControllerSpec extends SpecBase with MockitoSugar with RepaymentsReallocationsHelper {

  private trait Fixture {
    implicit val hc: HeaderCarrier                 = HeaderCarrier()
    val mockService: RepaymentsReallocationService = mock[RepaymentsReallocationService]
    val application: Application                   =
      applicationBuilder()
        .overrides(bind[RepaymentsReallocationService].toInstance(mockService))
        .build()

    implicit val msgs: Messages = messages(application)
  }

  "RepaymentsReallocationsController" - {

    "must return OK, with Reallocations From Summary, summary and correct view" in new Fixture {
      when(mockService.getRepayReallocationSummary(eqTo(1L), eqTo(1L))(any[HeaderCarrier]))
        .thenReturn(Future.successful(reallocationsFromSummary))

      running(application) {
        val request   = FakeRequest(GET, routes.RepaymentsReallocationsController.onPageLoad().url)
        val result    = route(application, request).value
        val viewModel = RepaymentReallocationSummaryViewModel.convertToViewModel(reallocationsFromSummary)
        val view      = application.injector.instanceOf[RepaymentsReallocationsView]

        status(result) mustEqual OK
        contentAsString(result) mustEqual
          view(viewModel, LocalDate.of(2026, 1, 1))(
            request,
            messages(application)
          ).toString
      }
    }

    "must return OK,with Reallocations To Summary, and correct view" in new Fixture {
      when(mockService.getRepayReallocationSummary(eqTo(1L), eqTo(1L))(any[HeaderCarrier]))
        .thenReturn(Future.successful(reallocationsToSummary))

      running(application) {
        val request   = FakeRequest(GET, routes.RepaymentsReallocationsController.onPageLoad().url)
        val result    = route(application, request).value
        val viewModel = RepaymentReallocationSummaryViewModel.convertToViewModel(reallocationsToSummary)
        val view      = application.injector.instanceOf[RepaymentsReallocationsView]

        status(result) mustEqual OK
        contentAsString(result) mustEqual
          view(viewModel, LocalDate.of(2026, 1, 1))(
            request,
            messages(application)
          ).toString
      }
    }

    "must return OK, with multiple summaries, showing both Reallocations To and From, and correct view" in new Fixture {
      when(mockService.getRepayReallocationSummary(eqTo(1L), eqTo(1L))(any[HeaderCarrier]))
        .thenReturn(Future.successful(multipleSummaries))

      running(application) {
        val request   = FakeRequest(GET, routes.RepaymentsReallocationsController.onPageLoad().url)
        val result    = route(application, request).value
        val viewModel = RepaymentReallocationSummaryViewModel.convertToViewModel(multipleSummaries)
        val view      = application.injector.instanceOf[RepaymentsReallocationsView]

        status(result) mustEqual OK
        contentAsString(result) mustEqual
          view(viewModel, LocalDate.of(2026, 1, 1))(
            request,
            messages(application)
          ).toString
      }
    }
  }

  "must redirect to JourneyRecoveryController when exception occurs from BE " in new Fixture {
    when(mockService.getRepayReallocationSummary(any(), any())(any[HeaderCarrier]))
      .thenReturn(Future.failed(new RuntimeException("Error while retrieving repayment reallocation summary")))

    running(application) {
      val request = FakeRequest(GET, routes.RepaymentsReallocationsController.onPageLoad().url)
      val result  = route(application, request).value

      status(result) mustEqual SEE_OTHER
      redirectLocation(result).value mustEqual routes.JourneyRecoveryController.onPageLoad().url
    }

  }

}
