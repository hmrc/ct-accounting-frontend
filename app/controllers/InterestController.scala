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
import helpers.InterestViewModelHelper
import play.api.Logging
import play.api.i18n.I18nSupport
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController
import viewmodels.InterestViewModel
import views.html.InterestView

import javax.inject.Inject
import scala.concurrent.ExecutionContext

class InterestController @Inject() (
  val controllerComponents: MessagesControllerComponents,
  identify: IdentifierAction,
  viewModelHelper: InterestViewModelHelper,
  view: InterestView
)(implicit ec: ExecutionContext)
    extends FrontendBaseController
    with I18nSupport
    with Logging {

  // TODO: - 1 :: integrate auth then its ready
  // TODO: - 2 :: read taxRef and accPeriod from the userSession
  private val taxRefFromSession: Long           = 3100L
  private val accountingPeriodFromSession: Long = 4L

  def onPageLoad(): Action[AnyContent] = identify.async { implicit request =>
    viewModelHelper
      .deriveInterestViewModel(taxRefFromSession, accountingPeriodFromSession)
      .map {
        case Right(vm)   => Ok(view(vm))
        case Left(error) =>
          logger.error(s"Unexpected failure while retrieving interest: $error")
          Redirect(JourneyRecoveryController.onPageLoad())
      }
      .recover { case ex =>
        logger.error(s"Unexpected failure while retrieving interest: ${ex.getMessage}")
        Redirect(JourneyRecoveryController.onPageLoad())
      }

  }

}
