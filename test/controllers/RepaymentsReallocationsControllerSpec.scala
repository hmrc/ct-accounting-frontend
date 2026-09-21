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
import play.api.i18n.Messages
import play.api.inject.bind
import play.api.test.FakeRequest
import play.api.test.Helpers.*
import play.api.Application
import uk.gov.hmrc.http.HeaderCarrier
import services.RepaymentsReallocationService
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
        .thenReturn(Future.successful(realloctionsFromSummary))

      running(application) {
        val request = FakeRequest(GET, routes.RepaymentsReallocationsController.onPageLoad().url)
        val result  = route(application, request).value
        val view    = application.injector.instanceOf[RepaymentsReallocationsView]

        val total = realloctionsFromSummary.transactions.flatMap(_.amount).sum

        status(result) mustEqual OK
        contentAsString(result) mustEqual
          view(realloctionsFromSummary, LocalDate.of(2026, 1, 1), total)(
            request,
            messages(application)
          ).toString
      }
    }

    "must return OK,with Reallocations To Summary, and correct view" in new Fixture {
      when(mockService.getRepayReallocationSummary(eqTo(1L), eqTo(1L))(any[HeaderCarrier]))
        .thenReturn(Future.successful(realloctionsToSummary))

      running(application) {
        val request = FakeRequest(GET, routes.RepaymentsReallocationsController.onPageLoad().url)
        val result  = route(application, request).value
        val view    = application.injector.instanceOf[RepaymentsReallocationsView]

        val total = realloctionsToSummary.transactions.flatMap(_.amount).sum

        status(result) mustEqual OK
        contentAsString(result) mustEqual
          view(realloctionsToSummary, LocalDate.of(2026, 1, 1), total)(
            request,
            messages(application)
          ).toString
      }
    }

    "must return OK, with multiple summaries, showing both Reallocations To and From, and correct view" in new Fixture {
      when(mockService.getRepayReallocationSummary(eqTo(1L), eqTo(1L))(any[HeaderCarrier]))
        .thenReturn(Future.successful(multipleSummaries))

      running(application) {
        val request = FakeRequest(GET, routes.RepaymentsReallocationsController.onPageLoad().url)
        val result  = route(application, request).value
        val view    = application.injector.instanceOf[RepaymentsReallocationsView]

        val total = multipleSummaries.transactions.flatMap(_.amount).sum

        status(result) mustEqual OK
        contentAsString(result) mustEqual
          view(multipleSummaries, LocalDate.of(2026, 1, 1), total)(
            request,
            messages(application)
          ).toString
      }
    }
  }

}
