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

import controllers.Execution.trampoline
import controllers.actions.*
import controllers.routes.JourneyRecoveryController
import play.api.i18n.Lang.logger
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import services.GroupPaymentService
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController
import views.html.GroupPaymentArrangementsView
import javax.inject.Inject


class GroupPaymentController @Inject() (
  override val messagesApi: MessagesApi,
  identify: IdentifierAction,
  val controllerComponents: MessagesControllerComponents,
  view: GroupPaymentArrangementsView,
  service: GroupPaymentService
) extends FrontendBaseController
    with I18nSupport {

  def onPageLoad: Action[AnyContent] = identify.async { implicit request =>
    val taxRef          = 1111444444L // 2L
    val accPeriod: Long = 1
    // Pagination params
    val startIndex: Int = 0
    val count: Int      = 20

    service
      .getViewModel(taxRef, accPeriod: Long, startIndex, count)
      .map { viewModel =>
        Ok(view(viewModel))
      }
      .recover { case ex =>
        logger.error(s"Unexpected failure in Group Payment Arrangement: ${ex.getMessage}")
        Redirect(JourneyRecoveryController.onPageLoad())
      }
  }

}
