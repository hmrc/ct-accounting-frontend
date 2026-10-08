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
import views.ViewUtils.formatCurrency
import models.gpa.GpaGroupTaxCharges
import uk.gov.hmrc.govukfrontend.views.Aliases.Text
import uk.gov.hmrc.govukfrontend.views.viewmodels.summarylist.{Key, Value, SummaryListRow}

import java.time.LocalDate

case class GroupTaxRecordRow(
  participatingCompany: String,
  uniqueTaxpayerReference: Long,
  accountingPeriodEnding: LocalDate,
  taxLiability: BigDecimal,
  paymentAllocated: BigDecimal
) {
  val taxLiabilityAsString: String     = formatCurrency(taxLiability)
  val paymentAllocatedAsString: String = formatCurrency(paymentAllocated)
}

case class GroupTaxChargesViewModel(
                                     summaryListRows: Seq[SummaryListRow],
                                     groupTaxRecordsRows: Seq[GroupTaxRecordRow]
)

object GroupTaxChargesViewModel {
  def toViewModel(
    gpaGroupTaxCharges: GpaGroupTaxCharges
  )(implicit messages: Messages): GroupTaxChargesViewModel = {
    GroupTaxChargesViewModel(
      summaryListRows = Seq(
        SummaryListRow(key = Key(content = Text(messages("groupTaxCharges.summaryHeader.groupArrangementReference"))),
          value = Value(content = Text(gpaGroupTaxCharges.pGpaUtr2.toString)),
          classes = "",
          actions = None),
        SummaryListRow(key = Key(content = Text(messages("groupTaxCharges.summaryHeader.periodOfAccountEnding"))),
          value = Value(content = Text(gpaGroupTaxCharges.pGppEndDate.getOrElse(LocalDate.of(2026, 1, 1)).toString)),
          classes = "",
          actions = None),
        SummaryListRow(key = Key(content = Text(messages("groupTaxCharges.summaryHeader.groupPeriodOfAccountStatus"))),
          value = Value(content = Text(gpaGroupTaxCharges.pGppStatus.getOrElse(""))),
          classes = "",
          actions = None),
        SummaryListRow(key = Key(content = Text(messages("groupTaxCharges.summaryHeader.groupPaymentTotal"))),
          value = Value(content = Text(gpaGroupTaxCharges.pGppTotalGroupTax.getOrElse(0.00).toString)),
          classes = "",
          actions = None),
        SummaryListRow(key = Key(content = Text(messages("groupTaxCharges.summaryHeader.groupTaxTotal"))),
          value = Value(content = Text(gpaGroupTaxCharges.pGppTotalGroupTax.getOrElse(0.00).toString)),
          classes = "",
          actions = None),
      ),
      groupTaxRecordsRows = gpaGroupTaxCharges.pCurGroupTaxCharges.map { groupTaxCharge =>
        GroupTaxRecordRow(
          participatingCompany = groupTaxCharge.participatorName,
          uniqueTaxpayerReference = groupTaxCharge.participatorReference,
          accountingPeriodEnding = groupTaxCharge.participatorApEndDate,
          taxLiability = groupTaxCharge.participatorTaxCharge.getOrElse(0.00),
          paymentAllocated = groupTaxCharge.allocatedPayment.getOrElse(0.00)
        )
      }
    )
  }

}
