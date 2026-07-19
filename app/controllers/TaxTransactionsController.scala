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

import connectors.TaxTransactionsConnector
import controllers.Execution.trampoline
import controllers.actions.*

import javax.inject.Inject
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController
import views.html.TaxTransactionsView

import java.time.LocalDate

class TaxTransactionsController @Inject() (
  override val messagesApi: MessagesApi,
  identify: IdentifierAction,
  val controllerComponents: MessagesControllerComponents,
  view: TaxTransactionsView,
  connector: TaxTransactionsConnector
) extends FrontendBaseController
    with I18nSupport {

  def onPageLoad: Action[AnyContent] = identify.async { implicit request =>

    val accountPeriod = LocalDate.of(2026, 1, 1) // TODO: This needs to comes from sessionDataRepository

    // TODO: Get taxRef + accPeriod from sessionDataRepositry
    connector.getTaxTransactions(1L, 1L).map { taxTransactionsResponse =>
      val total: BigDecimal = taxTransactionsResponse.taxTransactions.map(_.currentAmount).sum
      Ok(view(taxTransactionsResponse.taxTransactions, accountPeriod, total))
    }
  }
}
