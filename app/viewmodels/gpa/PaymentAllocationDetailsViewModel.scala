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

import models.AdjustmentTransactionType.*
import models.gpa.PaymentAllocationDetails
import play.api.i18n.Messages
import play.api.mvc.Call
import uk.gov.hmrc.govukfrontend.views.Aliases.TableRow
import uk.gov.hmrc.govukfrontend.views.viewmodels.content.Text
import views.ViewUtils.*

case class PaymentAllocationDetailsViewModelRow(
  rowName: String,
  content: String,
  isLink: Boolean,
  href: Option[Call]
)

case class PaymentAllocationDetailsViewModel(rows: List[PaymentAllocationDetailsViewModelRow]) {

  def totalRow(total: String, label: String): Seq[TableRow] =
    Seq(
      TableRow(content = Text(label), classes = "govuk-!-font-weight-bold"),
      TableRow(content = Text(total), classes = "govuk-!-font-weight-bold govuk-table__cell govuk-table__cell--numeric")
    )
}

object PaymentAllocationDetailsViewModel {

  def convertToViewModel(
    paymentAllocationDetails: PaymentAllocationDetails
  )(implicit messages: Messages): PaymentAllocationDetailsViewModel =
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
      )
    )

  private def isHyperLink(amount: BigDecimal): Boolean =
    amount != BigDecimal(0.00)
}
