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

package viewmodels.gpa

import uk.gov.hmrc.govukfrontend.views.Aliases.Pagination
import models.AdjustmentTransactionType.*
import models.gpa.PaymentAllocationDetails
import play.api.i18n.Messages
import play.api.mvc.Call
import utils.PaginationHelper
import viewmodels.Paginated
import views.ViewUtils.*

case class PaymentAllocationDetailsViewModelRow(
  rowName: String,
  content: String,
  isLink: Boolean,
  href: Option[Call]
)

case class AllocationDetailsViewModelRow(
  paymentDate: String,
  allocatedPaymentAmount: String
)

case class PaymentAllocationDetailsViewModel(
  rows: List[PaymentAllocationDetailsViewModelRow],
  allocRows: List[AllocationDetailsViewModelRow],
  page: Paginated[AllocationDetailsViewModelRow],
  pagination: Option[Pagination],
  paginationInfo: Option[String]
)

object PaymentAllocationDetailsViewModel {

  def convertToViewModel(
    paymentAllocationDetails: PaymentAllocationDetails,
    pageUrl: Int => String,
    requestedPage: Option[Int]
  )(implicit messages: Messages): PaymentAllocationDetailsViewModel = {

    val allocRows = paymentAllocationDetails.allocationDetails.map { allocDetails =>
      AllocationDetailsViewModelRow(
        paymentDate = formatDate(allocDetails.effectivePaymentDate, messages.lang),
        allocatedPaymentAmount = formatCurrency(allocDetails.paymentAmount)
      )
    }

    val page = Paginated.alreadyPaginated(allocRows, requestedPage, paymentAllocationDetails.totalNumOfRecords.toInt)

    PaymentAllocationDetailsViewModel(
      rows = List(
        PaymentAllocationDetailsViewModelRow(
          rowName = messages("paymentAllocationDetails.gpaReference"),
          content = paymentAllocationDetails.gpaUtr.toString(),
          isLink = false,
          href = None
        ),
        PaymentAllocationDetailsViewModelRow(
          rowName = messages("paymentAllocationDetails.participatingCompany"),
          content = paymentAllocationDetails.participatingCompanyDesc,
          isLink = false,
          href = None
        ),
        PaymentAllocationDetailsViewModelRow(
          rowName = messages("paymentAllocationDetails.accountingPeriodEnding"),
          content = formatDate(paymentAllocationDetails.gppEndDate, messages.lang),
          isLink = false,
          href = None
        ),
        PaymentAllocationDetailsViewModelRow(
          rowName = messages("paymentAllocationDetails.periodAccountEnding"),
          content = formatDate(paymentAllocationDetails.participatorAccPeriodEnd, messages.lang),
          isLink = false,
          href = None
        ),
        PaymentAllocationDetailsViewModelRow(
          rowName = messages("paymentAllocationDetails.groupPeriodAccountStatus"),
          content = paymentAllocationDetails.gppStatus,
          isLink = false,
          href = None
        ),
        PaymentAllocationDetailsViewModelRow(
          rowName = messages("paymentAllocationDetails.taxCharge"),
          content = formatCurrency(paymentAllocationDetails.participatorTaxCharge),
          isLink = false,
          href = None
        ),
        PaymentAllocationDetailsViewModelRow(
          rowName = messages("paymentAllocationDetails.paymentAllocatedBy"),
          content = paymentAllocationDetails.gppApportionmentMethod,
          isLink = false,
          href = None
        ),
        PaymentAllocationDetailsViewModelRow(
          rowName = messages("paymentAllocationDetails.paymentAllocated"),
          content = formatCurrency(paymentAllocationDetails.participatorAllocPayments),
          isLink = false,
          href = None
        ),
        PaymentAllocationDetailsViewModelRow(
          rowName = messages("paymentAllocationDetails.groupTaxTotal"),
          content = formatCurrency(paymentAllocationDetails.gppTotalGroupTax),
          isLink = isHyperLink(paymentAllocationDetails.gppTotalGroupTax),
          href = None // TODO Change to group taxes when implemented
        )
      ),
      allocRows = allocRows,
      page = page,
      pagination = PaginationHelper.toPagination(page, pageUrl),
      paginationInfo = PaginationHelper.paginationText(page)
    )
  }

  private def isHyperLink(amount: BigDecimal): Boolean =
    amount != BigDecimal(0.00)
}
