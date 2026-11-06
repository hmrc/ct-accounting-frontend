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

import controllers.Execution.trampoline
import controllers.actions.*
import controllers.routes.JourneyRecoveryController
import play.api.i18n.Lang.logger
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import services.payments.PaymentsOnAccountService
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController
import viewmodels.payments.PaymentsOnAccountViewModel
import views.html.payments.PaymentsOnAccountView
import utils.PaginationHelper.startIndexCountHelper

import javax.inject.Inject

class PaymentsOnAccountController @Inject() (
                                              override val messagesApi: MessagesApi,
                                              identify: IdentifierAction,
                                              val controllerComponents: MessagesControllerComponents,
                                              view: PaymentsOnAccountView,
                                              service: PaymentsOnAccountService
                                            ) extends FrontendBaseController
  with I18nSupport {

  def onPageLoad(page: Option[Int]): Action[AnyContent] = identify.async { implicit request =>

    // TODO update to retrieve from session
    val taxRef = 5L
    val pCount = 10
    val pStartIndex = startIndexCountHelper(page, pCount)

    service
      .getPaymentsOnAccount(taxRef, pStartIndex, pCount)
      .map { response =>
        val viewModel = PaymentsOnAccountViewModel.toViewModel(
          response,
          page,
          pageUrl = pageNumber => routes.PaymentsOnAccountController.onPageLoad(Some(pageNumber)).url
        )
        Ok(view(viewModel))
      }
      .recover { case ex =>
        logger.error(s"Unexpected failure while retrieving payments on account: ${ex.getMessage}")
        Redirect(JourneyRecoveryController.onPageLoad())
      }
  }
}
