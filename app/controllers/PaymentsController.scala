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

import connectors.PaymentsConnector
import controllers.Execution.trampoline
import controllers.actions.*
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController
import views.html.PaymentsView

import java.time.LocalDate
import javax.inject.Inject

class PaymentsController @Inject() (
  override val messagesApi: MessagesApi,
  identify: IdentifierAction,
  val controllerComponents: MessagesControllerComponents,
  view: PaymentsView,
  connector: PaymentsConnector
) extends FrontendBaseController
    with I18nSupport {

  def onPageLoad: Action[AnyContent] = identify.async { implicit request =>

    val accountPeriod = LocalDate.of(2026, 1, 1) // TODO: This needs to comes from sessionDataRepository

    // TODO: Get taxRef + accPeriod from sessionDataRepositry
    connector.getPayments(1L, 1L).map { paymentsResponse =>
      val total: BigDecimal = paymentsResponse.paymentTransactions.map(_.amount).sum
      Ok(view(paymentsResponse.paymentTransactions, accountPeriod, total))
    }
  }
}
