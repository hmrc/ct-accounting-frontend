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

package controllers.payments

import controllers.actions.IdentifierAction
import controllers.routes.JourneyRecoveryController
import play.api.Logging
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import services.payments.PaymentAllocationService
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController
import viewmodels.payments.PaymentAllocationViewModel
import views.html.payments.PaymentAllocationView

import java.time.LocalDate
import javax.inject.Inject
import scala.concurrent.ExecutionContext

class PaymentAllocationController @Inject() (
  val controllerComponents: MessagesControllerComponents,
  override val messagesApi: MessagesApi,
  identify: IdentifierAction,
  paymentAllocationService: PaymentAllocationService,
  view: PaymentAllocationView
)(implicit ec: ExecutionContext)
    extends FrontendBaseController
    with I18nSupport
    with Logging {

  val userEnteredPaymentAmount = 38000

  def onPageLoad(page: Option[Int]): Action[AnyContent] = identify.async { implicit request =>
    paymentAllocationService
      .getPaymentType(4L, Some(LocalDate.of(2024, 9, 30)), Some(LocalDate.of(2025, 9, 30)), userEnteredPaymentAmount)
      .map {
        // TODO Values to come from session data
        paymentAllocation =>
          logger.info(
            s"Successfully retrieved payment type for payment allocation"
          )
          val vm = PaymentAllocationViewModel.convertToViewModel(
            paymentAllocation,
            pageUrl = pageNumber => routes.PaymentAllocationController.onPageLoad(Some(pageNumber)).url,
            page
          )
          Ok(view(vm))
      } recover { case ex =>
      logger.error(s"Unexpected failure: ${ex.getMessage}")
      Redirect(JourneyRecoveryController.onPageLoad())
    }
  }
}
