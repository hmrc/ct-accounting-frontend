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

package viewmodels.payments

import play.api.i18n.Messages
import views.ViewUtils.*
import models.payments.PaymentSearchResultResponse
import play.api.mvc.Call
import uk.gov.hmrc.govukfrontend.views.Aliases.Pagination
import utils.{PaginationHelper, PaymentsDescriptionHelper}
import viewmodels.Paginated

import java.time.LocalDate

case class PaymentResultsViewModelRow(
  date: Option[LocalDate],
  description: String,
  amount: BigDecimal,
  href: Option[Call],
  isLink: Option[Boolean]
)(implicit messages: Messages) {
  val dateAsString: String = date
    .map(formatDate(_, messages.lang))
    .getOrElse("")
  val amountString: String = formatCurrency(amount)
}

case class PaymentResultsViewModel(
  taxReference: Long,
  rows: Seq[PaymentResultsViewModelRow],
  page: Paginated[PaymentResultsViewModelRow],
  pagination: Option[Pagination],
  paginationInfo: Option[String],
  totalResultsCount: Int
)

object PaymentResultsViewModel {
  def toViewModel(
    paymentResults: PaymentSearchResultResponse,
    taxReference: Long,
    pageUrl: Int => String,
    requestedPage: Option[Int]
  )(implicit messages: Messages): PaymentResultsViewModel = {

    val paymentResultRows = paymentResults.paymentSearchResult.map { value =>
      PaymentResultsViewModelRow(
        date = value.effectiveDateOfPayment,
        description = PaymentsDescriptionHelper.getPaymentsDescription(value.paymentType),
        amount = value.amount,
        // TODO update to Payment Allocation Page DTR-6469
        href = Some(controllers.payments.routes.PaymentResultsController.onPageLoad()),
        isLink = Some(true)
      )
    }

    val page = Paginated.paginate(paymentResultRows, requestedPage)

    PaymentResultsViewModel(
      taxReference = taxReference,
      rows = page.items,
      page = page,
      pagination = PaginationHelper.toPagination(page, pageUrl),
      paginationInfo = PaginationHelper.paginationText(page),
      totalResultsCount = paymentResultRows.size
    )
  }
}
