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

import play.api.i18n.Messages
import play.api.mvc.Call
import views.ViewUtils.{formatCurrency, formatDate}
import models.gpa.GpaGroupTaxCharges
import uk.gov.hmrc.govukfrontend.views.Aliases.Text
import controllers.gpa.routes
import uk.gov.hmrc.govukfrontend.views.viewmodels.summarylist.{Key, SummaryList}
import viewmodels.govuk.summarylist.*

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
                                     groupTaxRecordsRows: Seq[GroupTaxRecordRow]
)

case class AmountRow(description: String, amount: BigDecimal, isLink: Boolean, href: Call) {
  val amountAsString: String = formatCurrency(amount)
}

object GroupTaxChargesViewModel {
  def toViewModel(
    gpaGroupTaxCharges: GpaGroupTaxCharges
  )(implicit messages: Messages): GroupTaxChargesViewModel = {
    GroupTaxChargesViewModel(
      summaryList = SummaryListViewModel(rows =
        Seq(
          SummaryListRowViewModel(
            key = Key(content = Text(messages("groupTaxCharges.summaryHeader.groupArrangementReference"))),
            value = ValueViewModel(Text(gpaGroupTaxCharges.pGpaUtr2.toString))
          ),
          SummaryListRowViewModel(
            key = Key(content = Text(messages("groupTaxCharges.summaryHeader.periodOfAccountEnding"))),
            value = ValueViewModel(Text(formatDate(gpaGroupTaxCharges.pGppEndDate.getOrElse(LocalDate.of(2026, 1, 1)), messages.lang)))
          ),
          SummaryListRowViewModel(
            key = Key(content = Text(messages("groupTaxCharges.summaryHeader.groupPeriodOfAccountStatus"))),
            value = ValueViewModel(Text(gpaGroupTaxCharges.pGppStatus.getOrElse("")))
          ),
          SummaryListRowViewModel(
            key = Key(content = Text(messages("groupTaxCharges.summaryHeader.groupPaymentTotal"))),
            value = ValueViewModel(Text(formatCurrency(gpaGroupTaxCharges.pGppTotalGroupPayment.getOrElse(0.00))))
          ),
          SummaryListRowViewModel(
            key = Key(content = Text(messages("groupTaxCharges.summaryHeader.groupTaxTotal"))),
            value = ValueViewModel(Text(formatCurrency(gpaGroupTaxCharges.pGppTotalGroupTax.getOrElse(0.00))))
          ),
        )
      ),
      groupTaxRecordsRows = gpaGroupTaxCharges.pCurGroupTaxCharges.map { groupTaxCharge =>
        GroupTaxRecordRow(
          participatingCompany = groupTaxCharge.participatorName,
          uniqueTaxpayerReference = groupTaxCharge.participatorReference,
          accountingPeriodEnding = groupTaxCharge.participatorApEndDate,
          taxLiability = AmountRow(
            description = formatCurrency(groupTaxCharge.participatorTaxCharge.getOrElse(0.00)),
            amount = groupTaxCharge.participatorTaxCharge.getOrElse(0.00),
            isLink = false,
            href = routes.GroupTaxChargesController.onPageLoad()
          ),
          paymentAllocated = AmountRow(
            description = formatCurrency(groupTaxCharge.allocatedPayment.getOrElse(0.00)),
            amount = groupTaxCharge.allocatedPayment.getOrElse(0.00),
            isLink = true,
            href = routes.GroupTaxChargesController.onPageLoad()
          )
        )
      }
    )
  }

}