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

import controllers.Execution.trampoline
import controllers.actions.*
import controllers.routes.JourneyRecoveryController
import play.api.Logging
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import services.gpa.GroupPaymentService
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController
import utils.PaginationHelper
import viewmodels.gpa.GroupPaymentArrangementPageViewModel
import views.html.gpa.GroupPaymentArrangementsView

import javax.inject.Inject

class GroupPaymentController @Inject() (
  override val messagesApi: MessagesApi,
  identify: IdentifierAction,
  val controllerComponents: MessagesControllerComponents,
  view: GroupPaymentArrangementsView,
  service: GroupPaymentService
) extends FrontendBaseController
    with I18nSupport
    with Logging {

  // TODO Retrieve these values from Session Repository
  private val taxRef: Long           = 8L
  private val accPeriod: Int         = 1
  private val count: Int             = 10
  private val pageUrl: Int => String = p => routes.GroupPaymentController.onPageLoad(Some(p)).url

  def onPageLoad(paginationIndex: Option[Int]): Action[AnyContent] = identify.async { implicit request =>
    val startIndex: Int = PaginationHelper.startIndexCountHelper(paginationIndex, count)
    service
      .getViewModel(taxRef, accPeriod, startIndex, count)
      .map { viewModel =>
        val paginatedVm = GroupPaymentArrangementPageViewModel.toPageViewModel(viewModel, paginationIndex, pageUrl)
        Ok(view(paginatedVm))
      }
      .recover { case ex =>
        logger.error(s"Unexpected failure in Group Payment Arrangement: ${ex.getMessage}")
        Redirect(JourneyRecoveryController.onPageLoad())
      }
  }

}
