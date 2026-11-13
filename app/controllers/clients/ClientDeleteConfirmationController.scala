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

package controllers.clients

import controllers.actions.*
import play.api.i18n.I18nSupport
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController
import views.html.clients.ClientDeletedView

import javax.inject.Inject

class ClientDeleteConfirmationController @Inject() (
  val controllerComponents: MessagesControllerComponents,
  identify: IdentifierAction,
  view: ClientDeletedView
) extends FrontendBaseController
    with I18nSupport {
  private val ValidClientId = "^[A-Za-z0-9 &'._/-]{1,100}$".r

  // TODO Assumed to get the client id in QS
  def onPageLoad(clientId: String): Action[AnyContent] = identify { implicit request =>
    clientId.trim match {
      case id @ ValidClientId() => Ok(view(id))
      // TODO redirect to client list page
      // case _ => Redirect(ClientDeletedController.clientListUrl)
    }

  }

}
