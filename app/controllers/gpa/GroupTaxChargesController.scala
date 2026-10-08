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
import services.gpa.GroupTaxChargesService
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController
import viewmodels.gpa.GroupTaxChargesViewModel
import views.html.gpa.GroupTaxChargesView

import javax.inject.Inject

class GroupTaxChargesController @Inject() (
                                                     override val messagesApi: MessagesApi,
                                                     identify: IdentifierAction,
                                                     val controllerComponents: MessagesControllerComponents,
                                                     view: GroupTaxChargesView,
                                                     service: GroupTaxChargesService
                                                   ) extends FrontendBaseController
  with I18nSupport {

  def onPageLoad: Action[AnyContent] = identify.async { implicit request =>

    val pGpaUtr              = 1L // TODO: This needs to comes from sessionDataRepository
    val pGppContractVersion  = 1
    val pStartIndex          = 0
    val pCount               = 10

    // TODO: Get taxRef + accPeriod from sessionDataRepositry
    service
      .getGpaGroupTaxCharges(pGpaUtr, pGppContractVersion, pStartIndex, pCount)
      .map { groupTaxChargesResponse =>
        val viewModel =
          GroupTaxChargesViewModel.toViewModel(groupTaxChargesResponse)
        Ok(view(viewModel))
      }
      .recover { case ex =>
        logger.error(s"Unexpected failure while retrieving group tax charges: ${ex.getMessage}")
        Redirect(JourneyRecoveryController.onPageLoad())
      }
  }
}
