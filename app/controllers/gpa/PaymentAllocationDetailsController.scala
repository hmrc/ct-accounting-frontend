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

import controllers.actions.IdentifierAction
import controllers.routes.JourneyRecoveryController
import play.api.Logging
import play.api.i18n.I18nSupport
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import services.gpa.PaymentAllocationDetailsService
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController
import viewmodels.gpa.PaymentAllocationDetailsViewModel
import views.html.gpa.PaymentAllocationDetailsView

import javax.inject.Inject
import scala.concurrent.ExecutionContext

class PaymentAllocationDetailsController @Inject()(
  val controllerComponents: MessagesControllerComponents,
  identify: IdentifierAction,
  paymentAllocationDetailsService: PaymentAllocationDetailsService,
  view: PaymentAllocationDetailsView
)(implicit ec: ExecutionContext)
    extends FrontendBaseController
    with I18nSupport
    with Logging {

  def onPageLoad(): Action[AnyContent] = identify.async { implicit request =>
    paymentAllocationDetailsService.getGPAPaymentAllocationDetail(1L, 2L, 3L, 4L, 0, 10).map { paymentAllocationDetails =>
      logger.info(
        s"Successfully retrieved payment allocation details"
      )
      val vm = PaymentAllocationDetailsViewModel.convertToViewModel(paymentAllocationDetails)
      Ok(view(vm))
    } recover { case ex =>
      logger.error(s"Unexpected failure: ${ex.getMessage}")
      Redirect(JourneyRecoveryController.onPageLoad())
    }
  }
}
