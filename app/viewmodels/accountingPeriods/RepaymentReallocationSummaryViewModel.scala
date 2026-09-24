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

package viewmodels.accountingPeriods

import models.RepayReallocationSummary
import play.api.i18n.Messages
import uk.gov.hmrc.govukfrontend.views.Aliases.TableRow
import uk.gov.hmrc.govukfrontend.views.viewmodels.content.Text
import utils.RepaymentsReallocationsDescriptionHelper
import views.ViewUtils.formatCurrency

import java.time.LocalDate

case class RepaymentReallocationSummaryViewModelRow(
  transactionDate: Option[LocalDate],
  description: String,
  amount: Option[BigDecimal],
  accountingPeriodEndDate: Option[LocalDate],
  taxpayerReference: Option[String]
) {

  val amountAsString: String = formatCurrency(amount.getOrElse(0))

}

case class RepaymentReallocationSummaryViewModel(rows: List[RepaymentReallocationSummaryViewModelRow]) {

  val total: BigDecimal     = rows.flatMap(_.amount).sum
  val totalAsString: String = formatCurrency(total)

  def totalRow(total: String, label: String): Seq[TableRow] =
    Seq(
      TableRow(content = Text(label), classes = "govuk-!-font-weight-bold"),
      TableRow(content = Text(total), classes = "govuk-!-font-weight-bold govuk-table__cell govuk-table__cell--numeric")
    )

}

object RepaymentReallocationSummaryViewModel {

  def convertToViewModel(
    repaymentReallocationSummary: RepayReallocationSummary
  )(implicit messages: Messages): RepaymentReallocationSummaryViewModel =
    RepaymentReallocationSummaryViewModel(
      rows = repaymentReallocationSummary.transactions.map { transaction =>
        RepaymentReallocationSummaryViewModelRow(
          transactionDate = transaction.transactionDate,
          description = RepaymentsReallocationsDescriptionHelper.getDescription(transaction),
          amount = transaction.amount,
          accountingPeriodEndDate = transaction.accountingPeriodEndDate,
          taxpayerReference = transaction.taxpayerReference
        )
      }
    )
}
