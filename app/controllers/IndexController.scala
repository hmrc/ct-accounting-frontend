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

import connectors.PenaltiesConnector
import play.api.i18n.I18nSupport
import play.api.libs.json.Json
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController

import javax.inject.Inject
import scala.concurrent.ExecutionContext

class IndexController @Inject()(
                                 val controllerComponents: MessagesControllerComponents,
                                 val penaltiesConnector: PenaltiesConnector
                               )(implicit ec: ExecutionContext) extends FrontendBaseController
  with I18nSupport {

  def onPageLoad(): Action[AnyContent] = Action.async { implicit request =>
    // curl http://localhost:6992/rds-datacache-proxy/corporation-tax/penalty-transactions/8754000131/9
    penaltiesConnector.getPenaltyTransactionList(taxRef = 8754000131L, accPeriod = 9L)
      .map(res =>
        Ok( Json.toJson(res).toString )
      )
  }
}
