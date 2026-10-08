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

import play.api.mvc.Call
import views.ViewUtils.formatCurrency
import models.gpa.GroupSummaryDetailsResponse

import java.time.LocalDate

case class GroupPaymentArrangementViewModel(
  taxPayerReference: Long,
  periodOfAccountEnding: LocalDate,
  groupPayments: BigDecimal,
  groupTaxes: BigDecimal,
  status: String
) {
  val groupPaymentsAsString: String = formatCurrency(groupPayments)
  val groupTaxesAsString: String    = formatCurrency(groupTaxes)
}

case class GroupPaymentArrangementRow(description: String, amount: BigDecimal, isLink: Boolean, href: Call) {
  val amountAsString: String = formatCurrency(amount)
}

object GroupPaymentArrangementViewModel {
  def toViewModel(
    groupSummaryDetailsResponse: GroupSummaryDetailsResponse
  ): GroupPaymentArrangementViewModel =
    GroupPaymentArrangementViewModel(
      taxPayerReference = groupSummaryDetailsResponse.gpaReferenceNumberLst.head.taxpayerReference,
      periodOfAccountEnding = groupSummaryDetailsResponse.gpaGrpSummaryDetails.head.contractEndDate,
      groupPayments = groupSummaryDetailsResponse.gpaGrpSummaryDetails.head.groupPayment,
      groupTaxes = groupSummaryDetailsResponse.gpaGrpSummaryDetails.head.groupTaxCharge,
      status = groupSummaryDetailsResponse.gpaGrpSummaryDetails.head.contractStatus
    )

}
