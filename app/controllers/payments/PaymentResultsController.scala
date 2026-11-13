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

package controllers.payments

import controllers.actions.*
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController
import views.html.payments.PaymentResultsView
import viewmodels.payments.PaymentResultsViewModel
import services.payments.PaymentResultsService
import play.api.Logging
import views.ViewUtils.{formatCurrency, formatDate}

import java.time.LocalDate
import javax.inject.Inject
import scala.concurrent.ExecutionContext

class PaymentResultsController @Inject() (
  override val messagesApi: MessagesApi,
  identify: IdentifierAction,
  val controllerComponents: MessagesControllerComponents,
  service: PaymentResultsService,
  view: PaymentResultsView
)(implicit ec: ExecutionContext)
    extends FrontendBaseController
    with I18nSupport
    with Logging {

  def onPageLoad(page: Option[Int]): Action[AnyContent] = identify.async { implicit request =>

    // TODO update to pull values from session
    val taxRef                      = 1111444444L
    val userEnteredPaymentAmount    = 38000
    val fromDate: Option[LocalDate] = Some(LocalDate.of(2006, 1, 1))
    val toDate: Option[LocalDate]   = Some(LocalDate.of(2008, 1, 1))

    val userEnteredPaymentAmountString: String = formatCurrency(userEnteredPaymentAmount)
    val fromDateStringOpt: Option[String]      = fromDate
      .map(formatDate(_, messagesApi.preferred(request).lang))
    val toDateStringOpt: Option[String]        = toDate
      .map(formatDate(_, messagesApi.preferred(request).lang))

    service
      .getPaymentSearchResult(taxRef, userEnteredPaymentAmount, fromDate, toDate)
      .map { response =>
        val viewModel = PaymentResultsViewModel.toViewModel(
          paymentResults = response,
          taxReference = taxRef,
          pageUrl = pageNumber => routes.PaymentResultsController.onPageLoad(Some(pageNumber)).url,
          requestedPage = page
        )
        val listCount = viewModel.totalResultsCount

        Ok(view(viewModel, userEnteredPaymentAmountString, listCount, fromDateStringOpt, toDateStringOpt))
      }
      .recover { case ex =>
        logger.error(s"Unexpected failure while retrieving payment search results: ${ex.getMessage}")
        Redirect(controllers.routes.JourneyRecoveryController.onPageLoad())
      }
  }
}
