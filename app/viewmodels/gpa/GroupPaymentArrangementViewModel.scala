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
import play.api.mvc.Call
import views.ViewUtils.formatCurrency
import models.gpa.GroupSummaryDetailsResponse
import play.api.i18n.Messages

import java.time.LocalDate

case class GroupPaymentArrangementViewModel(
  taxPayerReference: Long,
  rows: Seq[GroupPaymentArrangementViewRow]
)

case class GroupPaymentArrangementViewRow(
  periodOfAccountEnding: LocalDate,
  groupPayments: GroupPaymentArrangementPaymentRow,
  groupTaxes: GroupPaymentArrangementPaymentRow,
  status: String
)

case class GroupPaymentArrangementPaymentRow(description: String, amount: BigDecimal, isLink: Boolean, href: Call) {
  val amountAsString: String = formatCurrency(amount)
}

object GroupPaymentArrangementViewModel {
  def toViewModel(
    groupSummaryDetailsResponse: GroupSummaryDetailsResponse
  )(implicit messages: Messages): GroupPaymentArrangementViewModel =
    GroupPaymentArrangementViewModel(
      taxPayerReference = groupSummaryDetailsResponse.gpaReferenceNumberLst.head.taxpayerReference,
      rows = groupSummaryDetailsResponse.gpaGrpSummaryDetails.map { groupSummaryDetailsResponse =>
        GroupPaymentArrangementViewRow(
          periodOfAccountEnding = groupSummaryDetailsResponse.contractEndDate,
          groupPayments = GroupPaymentArrangementPaymentRow(
            description = formatCurrency(groupSummaryDetailsResponse.groupPayment),
            amount = groupSummaryDetailsResponse.groupPayment,
            isLink = true,
            href = routes.GroupPaymentArrangementController.onPageLoad()
          ),
          groupTaxes = GroupPaymentArrangementPaymentRow(
            description = formatCurrency(groupSummaryDetailsResponse.groupTaxCharge),
            amount = groupSummaryDetailsResponse.groupTaxCharge,
            isLink = true,
            href = routes.GroupPaymentArrangementController.onPageLoad()
          ),
          status = groupSummaryDetailsResponse.contractStatus
        )
      }
    )

}
