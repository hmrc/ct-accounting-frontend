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
import play.api.i18n.I18nSupport
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import services.PenaltiesService
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController
import views.html.PenaltiesAccountingPeriodView

import javax.inject.Inject
import scala.concurrent.ExecutionContext

// TODO: - 1 :: integrate auth then its ready
// TODO: - 2 :: read taxRef and accPeriod from the userSession
class PenaltiesAccountingPeriodController @Inject() (
  val controllerComponents: MessagesControllerComponents,
  identify: IdentifierAction,
  service: PenaltiesService,
  view: PenaltiesAccountingPeriodView
)(implicit ec: ExecutionContext)
    extends FrontendBaseController
    with I18nSupport {

  def onPageLoad(): Action[AnyContent] = identify.async { implicit request =>
    val taxRef: Long    = 1L
    val accPeriod: Long = 1L // Also pass accountingPeriodAsLocalDate to construct model
    for {
      viewModel <- service.getViewModel(taxRef, accPeriod)
    } yield Ok(view(viewModel))

  }

}
