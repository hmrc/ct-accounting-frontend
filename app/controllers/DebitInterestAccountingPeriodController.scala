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
import play.api.Logging
import play.api.i18n.I18nSupport
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import services.DebitInterestService
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController
import controllers.routes.JourneyRecoveryController
import views.html.DebitInterestAccountingPeriodView

import java.time.LocalDate
import javax.inject.Inject
import scala.concurrent.ExecutionContext

class DebitInterestAccountingPeriodController @Inject() (
  val controllerComponents: MessagesControllerComponents,
  service: DebitInterestService,
  view: DebitInterestAccountingPeriodView,
  identify: IdentifierAction
)(implicit ec: ExecutionContext)
    extends FrontendBaseController
    with I18nSupport
    with Logging {

  def onPageLoad(): Action[AnyContent] = identify.async { implicit request =>
    val taxRef: Long         = 2L
    val accPeriod: Long      = 4L
    val accPeriodEndDate     = LocalDate.of(2026, 9, 30)
    val interestType: String = "IDB" // TODO: to be confirmed from Java::guys

    service
      .getDebitInterest(taxRef, accPeriod, interestType, accPeriodEndDate)
      .map(viewModel => Ok(view(viewModel)))
      .recover { case ex =>
        logger.error(s"[DebitInterestAccountingPeriodController][onPageLoad] - Unexpected failure: ${ex.getMessage}")
        Redirect(JourneyRecoveryController.onPageLoad())
      }
  }
}
