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
import helpers.gpa.GroupTaxChargesHelper
import org.mockito.ArgumentMatchers.{any, eq as eqTo}
import org.mockito.Mockito.when
import org.scalatestplus.mockito.MockitoSugar
import play.api.Application
import play.api.i18n.Messages
import play.api.inject.bind
import play.api.test.FakeRequest
import play.api.test.Helpers.*
import services.gpa.GroupTaxChargesService
import uk.gov.hmrc.http.HeaderCarrier
import viewmodels.gpa.GroupTaxChargesViewModel
import views.html.gpa.GroupTaxChargesView

import scala.concurrent.Future

class GroupTaxChargesControllerSpec extends SpecBase with MockitoSugar with GroupTaxChargesHelper {

  private trait Fixture {
    implicit val hc: HeaderCarrier          = HeaderCarrier()
    val mockService: GroupTaxChargesService = mock[GroupTaxChargesService]
    val application: Application            =
      applicationBuilder()
        .overrides(bind[GroupTaxChargesService].toInstance(mockService))
        .build()

    implicit val msgs: Messages = messages(application)
  }

  "GroupTaxChargesController" - {

    "must return OK, with Group Tax Charges correct view" in new Fixture {
      when(mockService.getGpaGroupTaxCharges(eqTo(1L), eqTo(1), eqTo(0), eqTo(10))(any[HeaderCarrier]))
        .thenReturn(Future.successful(gpaGroupTaxChargesWithTwoParticipators))

      running(application) {
        val request   = FakeRequest(GET, routes.GroupTaxChargesController.onPageLoad(None).url)
        val result    = route(application, request).value
        val viewModel = GroupTaxChargesViewModel.toViewModel(
          gpaGroupTaxChargesWithTwoParticipators,
          Some(0),
          pageUrl = pageNumber => routes.GroupTaxChargesController.onPageLoad(Some(0)).url
        )
        val view      = application.injector.instanceOf[GroupTaxChargesView]

        status(result) mustEqual OK
        contentAsString(result) mustEqual
          view(viewModel)(
            request,
            messages(application)
          ).toString
      }
    }

    "must redirect when exception occurs from BE occurs" in new Fixture {
      when(mockService.getGpaGroupTaxCharges(eqTo(1L), eqTo(1), eqTo(0), eqTo(10))(any[HeaderCarrier]))
        .thenReturn(Future.failed(new RuntimeException("Error")))

      running(application) {
        val request = FakeRequest(GET, routes.GroupTaxChargesController.onPageLoad(None).url)
        val result  = route(application, request).value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual controllers.routes.JourneyRecoveryController.onPageLoad().url
      }
    }

  }
}
