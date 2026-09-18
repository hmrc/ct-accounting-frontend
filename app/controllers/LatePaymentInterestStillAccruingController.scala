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
import play.api.Logging
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import services.LatePaymentInterestService
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController
import viewmodels.accountingPeriods.LatePaymentInterestRow
import views.html.accountingPeriods.LatePaymentInterestStillAccruingView

import javax.inject.Inject

class LatePaymentInterestStillAccruingController @Inject() (
  override val messagesApi: MessagesApi,
  identify: IdentifierAction,
  val controllerComponents: MessagesControllerComponents,
  view: LatePaymentInterestStillAccruingView,
  service: LatePaymentInterestService
) extends FrontendBaseController
    with I18nSupport
    with Logging {

  // TODO: - 1 :: integrate auth then its ready
  // TODO: - 2 :: read taxRef and accPeriod and interestType from the userSession
  private val taxRefFromSession: Long           = 1L
  private val accountingPeriodFromSession: Long = 1L
  private val interestType: String              = "IDE"

  def onPageLoad: Action[AnyContent] = identify.async { implicit request =>
    service
      .getInterestAccrualList(taxRefFromSession, accountingPeriodFromSession, interestType)
      .map { interestAccrualResponse =>
        val viewModel = LatePaymentInterestRow.toViewModel(interestAccrualResponse)
        Ok(view(viewModel))
      }
      .recover { case ex =>
        logger.error(s"Unexpected failure while retrieving interestAccrual: ${ex.getMessage}")
        Redirect(JourneyRecoveryController.onPageLoad())
      }
  }
}
