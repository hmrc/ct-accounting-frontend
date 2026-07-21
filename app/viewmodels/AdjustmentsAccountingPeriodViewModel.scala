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

package viewmodels

import uk.gov.hmrc.govukfrontend.views.Aliases.TableRow
import uk.gov.hmrc.govukfrontend.views.viewmodels.content.Text
import play.api.i18n.Messages
import models.AdjustmentTransactionsList
import models.AdjustmentTransactionType.*
import utils.Formatters.*

case class AdjustmentsAccountingPeriodViewModelRow(
                                                    description: String,
                                                    amount: BigDecimal
                                                  ) {

  val amountAsString: String = toCurrency(amount)

}

case class AdjustmentsAccountingPeriodViewModel(rows: List[AdjustmentsAccountingPeriodViewModelRow]) {

  val total: BigDecimal = rows.map(_.amount).sum

  val totalAsString: String = toCurrency(total)

  def totalRow(total: String, label: String): Seq[TableRow] =
    Seq(TableRow(content = Text(label), classes = "govuk-!-font-weight-bold"),
      TableRow(content = Text(total), classes = "govuk-!-font-weight-bold govuk-table__cell govuk-table__cell--numeric")
      )
}

object AdjustmentsAccountingPeriodViewModel {

  def convertToViewModel(
                          adjustmentTransactions: AdjustmentTransactionsList
                        )(implicit messages: Messages): AdjustmentsAccountingPeriodViewModel =
    AdjustmentsAccountingPeriodViewModel(
      rows = adjustmentTransactions.adjustmentTransactionsList.map { adjustment =>
        AdjustmentsAccountingPeriodViewModelRow(
          description = asString(adjustment.`type`),
          amount = adjustment.amount
        )
      }
    )
}
