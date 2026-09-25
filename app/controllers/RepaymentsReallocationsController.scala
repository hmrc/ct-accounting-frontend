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

import controllers.actions.IdentifierAction
import controllers.routes.JourneyRecoveryController
import play.api.Logging
import play.api.i18n.I18nSupport
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import services.RepaymentsReallocationService
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController
import viewmodels.accountingPeriods.RepaymentReallocationSummaryViewModel
import views.html.accountingPeriods.RepaymentsReallocationsView

import java.time.LocalDate
import javax.inject.Inject
import scala.concurrent.ExecutionContext

class RepaymentsReallocationsController @Inject() (
  val controllerComponents: MessagesControllerComponents,
  identify: IdentifierAction,
  service: RepaymentsReallocationService,
  view: RepaymentsReallocationsView
)(implicit ec: ExecutionContext)
    extends FrontendBaseController
    with I18nSupport
    with Logging {

  // TODO: read taxRef and accPeriod from the userSession

  def onPageLoad: Action[AnyContent] = identify.async { implicit request =>

    val accountPeriod = LocalDate.of(2026, 1, 1) // TODO: This needs to comes from sessionDataRepository

    // TODO: Get taxRef + accPeriod from sessionDataRepositry
    service.getRepayReallocationSummary(1L, 1L).map { summaryResponse =>
      val viewModel = RepaymentReallocationSummaryViewModel.convertToViewModel(summaryResponse)
      Ok(view(viewModel, accountPeriod))
    } recover { case ex =>
      logger.error(s"Unexpected failure while retrieving Repayments Reallocations Summaries: ${ex.getMessage}")
      Redirect(JourneyRecoveryController.onPageLoad())
    }
  }

}
