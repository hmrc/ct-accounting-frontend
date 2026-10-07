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
  groupPaymentArrangementReference: Long,
  periodOfAccountEnding: LocalDate,
  groupPeriodOfAccountStatus: String,
  groupPaymentTotal: BigDecimal,
  groupTaxTotal: BigDecimal,
  rows: Seq[GroupTaxRecordRow]
) {
  val groupPaymentTotalAsString: String = formatCurrency(groupPaymentTotal)
  val groupTaxTotalAsString: String     = formatCurrency(groupTaxTotal)
}

object GroupTaxChargesViewModel {
  def toViewModel(
    gpaGroupTaxCharges: GpaGroupTaxCharges
  )(implicit messages: Messages): GroupTaxChargesViewModel = {
    GroupTaxChargesViewModel(
      groupPaymentArrangementReference = gpaGroupTaxCharges.pGpaUtr2,
      periodOfAccountEnding = gpaGroupTaxCharges.pGppEndDate.getOrElse(LocalDate.of(2026, 1, 1)),
      groupPeriodOfAccountStatus = gpaGroupTaxCharges.pGppStatus.getOrElse(""),
      groupPaymentTotal = gpaGroupTaxCharges.pGppTotalGroupPayment.getOrElse(0.00),
      groupTaxTotal = gpaGroupTaxCharges.pGppTotalGroupTax.getOrElse(0.00),
      rows = gpaGroupTaxCharges.pCurGroupTaxCharges.map { groupTaxCharge =>
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
