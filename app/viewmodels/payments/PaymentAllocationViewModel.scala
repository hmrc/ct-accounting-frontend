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

import uk.gov.hmrc.govukfrontend.views.Aliases.Pagination
import models.AdjustmentTransactionType.*
import models.payments.PaymentAllocation
import play.api.i18n.Messages
import play.api.mvc.Call
import utils.PaginationHelper
import viewmodels.Paginated
import views.ViewUtils.*

case class PaymentAllocationViewModel(
  rows: Seq[PaymentAllocationViewModelRow],
  page: Paginated[PaymentAllocationViewModelRow],
  pagination: Option[Pagination],
  paginationInfo: Option[String]
)

case class PaymentAllocationViewModelRow(
  paymentAllocationDate: String,
  accountingPeriodEnding: String,
  amount: String,
  isLink: Boolean,
  href: Option[Call]
)

object PaymentAllocationViewModel {

  def convertToViewModel(
    paymentAllocation: List[PaymentAllocation],
    pageUrl: Int => String,
    requestedPage: Option[Int]
  )(implicit messages: Messages): PaymentAllocationViewModel = {

    val paymentAllocationRows = paymentAllocation.flatMap { paymentAlloc =>
      paymentAlloc.paymentAllocation.map { p =>
        PaymentAllocationViewModelRow(
          paymentAllocationDate = formatDate(p.effectiveDateOfPayment, messages.lang),
          accountingPeriodEnding = formatDate(p.apEndDate, messages.lang),
          amount = formatCurrency(p.amount),
          isLink = true,
          href = Some(controllers.routes.AccountingPeriodOverviewController.onPageLoad())
        )
      }
    }

    val page = Paginated.paginate(paymentAllocationRows, requestedPage)

    PaymentAllocationViewModel(
      rows = page.items,
      page = Paginated.paginate(paymentAllocationRows, requestedPage),
      pagination = PaginationHelper.toPagination(page, pageUrl),
      paginationInfo = PaginationHelper.paginationText(page)
    )
  }
}
