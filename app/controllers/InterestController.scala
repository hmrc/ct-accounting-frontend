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
import services.InterestService
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController
import viewmodels.InterestViewModel
import views.html.InterestView

import javax.inject.Inject
import scala.concurrent.ExecutionContext

class InterestController @Inject() (
  val controllerComponents: MessagesControllerComponents,
  identify: IdentifierAction,
  service: InterestService,
  view: InterestView
)(implicit ec: ExecutionContext)
    extends FrontendBaseController
    with I18nSupport
    with Logging {

  // TODO: - 1 :: integrate auth then its ready
  // TODO: - 2 :: read taxRef and accPeriod from the userSession

  def onPageLoad(): Action[AnyContent] = identify.async { implicit request =>
    service.getAccountingPeriodResponse(3100L, 4L).map { response =>
      logger.info(
        s"[InterestController][onPageLoad] - successfully retrieved AccountingPeriodResponse"
      )
      val vm = InterestViewModel.toViewModel(response)
      Ok(view(vm))
    } recover { case ex =>
      logger.error(s"[InterestController][onPageLoad] - Unexpected failure: ${ex.getMessage}")
      Redirect(JourneyRecoveryController.onPageLoad())
    }
  }
}
