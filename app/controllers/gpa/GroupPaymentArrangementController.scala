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
import play.api.i18n.Lang.logger
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import services.AccountingPeriodOverviewService
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController
import viewmodels.gpa.GroupPaymentArrangementViewModel
import views.html.gpa.GroupPaymentArrangementView

import java.time.LocalDate
import javax.inject.Inject

class GroupPaymentArrangementController @Inject() (
  override val messagesApi: MessagesApi,
  identify: IdentifierAction,
  val controllerComponents: MessagesControllerComponents,
  view: GroupPaymentArrangementView,
  service: AccountingPeriodOverviewService
) extends FrontendBaseController
    with I18nSupport {

  def onPageLoad: Action[AnyContent] = identify.async { implicit request =>

    val accountPeriodEndDate = LocalDate.of(2026, 1, 1) // TODO: This needs to comes from sessionDataRepository
    val referenceNumber      = "933636936A00104A"
    val taxRef               = 1L
    val accPeriod            = 1L
    val groupPayments        = -1536642.00
    val groupTaxes           = 1536642.00
    val gpaStatus            = "Open"

    // TODO: Get taxRef + accPeriod from sessionDataRepositry. Use different service!
    service
      .getAccountingPeriodOverview(taxRef, accPeriod, accountPeriodEndDate)
      .map { accountingPeriodOverviewResponse =>
        val viewModel =
          GroupPaymentArrangementViewModel.toViewModel(referenceNumber,accountPeriodEndDate, groupPayments, groupTaxes, gpaStatus)
        Ok(view(viewModel))
      }
      .recover { case ex =>
        logger.error(s"Unexpected failure while retrieving group payment arrangement: ${ex.getMessage}")
        Redirect(JourneyRecoveryController.onPageLoad())
      }
  }
}
