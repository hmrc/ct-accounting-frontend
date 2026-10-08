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
import helpers.gpa.{GroupPaymentArrangementHelper, GroupPaymentsHelper}
import org.mockito.ArgumentMatchers.{any, eq as eqTo}
import org.mockito.Mockito.when
import org.scalatestplus.mockito.MockitoSugar
import play.api.Application
import play.api.i18n.Messages
import play.api.inject.bind
import play.api.test.FakeRequest
import play.api.test.Helpers.*
import services.gpa.GroupPaymentsService
import uk.gov.hmrc.http.HeaderCarrier
import viewmodels.gpa.GroupPaymentArrangementViewModel
import views.html.gpa.GroupPaymentArrangementView

import scala.concurrent.Future

class GroupPaymentArrangementControllerSpec
    extends SpecBase
    with MockitoSugar
    with GroupPaymentArrangementHelper
    with GroupPaymentsHelper {

  private trait Fixture {
    implicit val hc: HeaderCarrier        = HeaderCarrier()
    val mockService: GroupPaymentsService = mock[GroupPaymentsService]
    val application: Application          =
      applicationBuilder()
        .overrides(bind[GroupPaymentsService].toInstance(mockService))
        .build()

    implicit val msgs: Messages = messages(application)
  }

  "GroupPaymentArrangementController" - {

    "must return OK, with Group Payment Arrangement correct view" in new Fixture {
      when(mockService.getGroupSummary(eqTo(1L), eqTo(1L))(any[HeaderCarrier]))
        .thenReturn(Future.successful(groupPaymentDetailsResponse))

      running(application) {
        val request   = FakeRequest(GET, routes.GroupPaymentArrangementController.onPageLoad().url)
        val result    = route(application, request).value
        val viewModel = GroupPaymentArrangementViewModel.toViewModel(groupPaymentDetailsResponse)
        val view      = application.injector.instanceOf[GroupPaymentArrangementView]

        status(result) mustEqual SEE_OTHER
        contentAsString(result) mustEqual
          view(viewModel)(
            request,
            messages(application)
          ).toString
      }
    }
  }
}
