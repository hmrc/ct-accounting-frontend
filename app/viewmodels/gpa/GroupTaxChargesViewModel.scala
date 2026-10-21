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

import controllers.gpa.routes
import models.gpa.GpaGroupTaxCharges
import play.api.i18n.Messages
import play.api.mvc.Call
import uk.gov.hmrc.govukfrontend.views.Aliases.{Pagination, Text}
import uk.gov.hmrc.govukfrontend.views.viewmodels.summarylist.{Key, SummaryList}
import utils.PaginationHelper
import utils.gpa.StatusHelper
import viewmodels.Paginated
import viewmodels.govuk.summarylist.*
import views.ViewUtils.{formatCurrency, formatDate}

import java.time.LocalDate

case class GroupTaxRecordRow(
  participatingCompany: String,
  uniqueTaxpayerReference: Long,
  accountingPeriodEnding: LocalDate,
  taxLiability: AmountRow,
  paymentAllocated: AmountRow
)

case class GroupTaxChargesViewModel(
  summaryList: SummaryList,
  groupTaxRecordsRows: Seq[GroupTaxRecordRow],
  page: Paginated[GroupTaxRecordRow],
  pagination: Option[Pagination],
  paginationInfo: Option[String]
) {
  val totalTaxLiability: BigDecimal     = groupTaxRecordsRows.map(_.taxLiability.amount).sum
  val totalPaymentAllocated: BigDecimal = groupTaxRecordsRows.map(_.paymentAllocated.amount).sum
}

case class AmountRow(description: String, amount: BigDecimal, isLink: Boolean, href: Call) {
  val amountAsString: String = formatCurrency(amount)
}

object GroupTaxChargesViewModel {
  def toViewModel(
    gpaGroupTaxCharges: GpaGroupTaxCharges,
    requestedPage: Option[Int],
    pageUrl: Int => String
  )(implicit messages: Messages): GroupTaxChargesViewModel = {
    val rows = gpaGroupTaxCharges.pCurGroupTaxCharges.map { groupTaxCharge =>
      GroupTaxRecordRow(
        participatingCompany = groupTaxCharge.participatorName,
        uniqueTaxpayerReference = groupTaxCharge.participatorReference,
        accountingPeriodEnding = groupTaxCharge.participatorApEndDate,
        taxLiability = AmountRow(
          description = formatCurrency(groupTaxCharge.participatorTaxCharge.getOrElse(0.00)),
          amount = groupTaxCharge.participatorTaxCharge.getOrElse(0.00),
          isLink = false,
          href = routes.GroupTaxChargesController.onPageLoad(None)
        ),
        paymentAllocated = AmountRow(
          description = formatCurrency(groupTaxCharge.allocatedPayment.getOrElse(0.00)),
          amount = groupTaxCharge.allocatedPayment.getOrElse(0.00),
          isLink = true,
          href = routes.GroupTaxChargesController.onPageLoad(None)
        )
      )
    }

    val page = Paginated.alreadyPaginated(rows, requestedPage, gpaGroupTaxCharges.pTotalNumOfRecords.getOrElse(0))

    GroupTaxChargesViewModel(
      summaryList = SummaryListViewModel(rows =
        Seq(
          SummaryListRowViewModel(
            key = Key(content = Text(messages("groupTaxCharges.summaryHeader.groupArrangementReference"))),
            value = ValueViewModel(Text(gpaGroupTaxCharges.pGpaUtr2.toString))
          ),
          SummaryListRowViewModel(
            key = Key(content = Text(messages("groupTaxCharges.summaryHeader.periodOfAccountEnding"))),
            value = ValueViewModel(
              Text(formatDate(gpaGroupTaxCharges.pGppEndDate.getOrElse(LocalDate.of(2026, 1, 1)), messages.lang))
            )
          ),
          SummaryListRowViewModel(
            key = Key(content = Text(messages("groupTaxCharges.summaryHeader.groupPeriodOfAccountStatus"))),
            value = ValueViewModel(Text(messages(StatusHelper.getStatus(gpaGroupTaxCharges.pGppStatus.getOrElse("")))))
          ),
          SummaryListRowViewModel(
            key = Key(content = Text(messages("groupTaxCharges.summaryHeader.groupPaymentTotal"))),
            value = ValueViewModel(
              LinkViewModel.toHtmlContent(
                LinkViewModel(
                  text = formatCurrency(gpaGroupTaxCharges.pGppTotalGroupPayment.getOrElse(0.00)),
                  href = routes.GroupTaxChargesController.onPageLoad(None).url,
                  classes = "govuk-table__cell govuk-table__cell--numeric",
                  isLink = true
                )
              )
            )
          ),
          SummaryListRowViewModel(
            key = Key(content = Text(messages("groupTaxCharges.summaryHeader.groupTaxTotal"))),
            value = ValueViewModel(Text(formatCurrency(gpaGroupTaxCharges.pGppTotalGroupTax.getOrElse(0.00))))
          )
        )
      ),
      groupTaxRecordsRows = rows,
      page = page,
      pagination = PaginationHelper.toPagination(page, pageUrl),
      paginationInfo = PaginationHelper.paginationText(page)
    )
  }
}
