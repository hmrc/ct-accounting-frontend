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

import play.api.i18n.Lang
import uk.gov.hmrc.govukfrontend.views.Aliases.TableRow
import uk.gov.hmrc.govukfrontend.views.viewmodels.content.Text
import utils.DateTimeFormats

import java.time.LocalDate

// TODO: use generic Utils from PR to be merged
object Formatters {
  // Port from PR to be merged / re-wirte after
  def formatDate(date: LocalDate): String =
    date.format(DateTimeFormats.dateTimeFormat()(Lang.defaultLang))

  def formatCurrency(amount: BigDecimal): String = {
    val formatter = java.text.NumberFormat.getCurrencyInstance(java.util.Locale.UK)
    if (amount.signum < 0) s"-${formatter.format(amount.abs)}" else formatter.format(amount)
  }
}

case class PenaltiesAccountingPeriodViewModelRow(date: LocalDate, description: String, amount: BigDecimal) {
  import Formatters.*

  val dateAsString: String   = formatDate(date)
  val amountAsString: String = formatCurrency(amount)
}

case class PenaltiesAccountingPeriodViewModel(
  accountingPeriodEnd: LocalDate,
  rows: List[PenaltiesAccountingPeriodViewModelRow]
) {

  import Formatters.*

  val accountingPeriodEndAsString: String = formatDate(accountingPeriodEnd)

  val total: BigDecimal = rows.map(_.amount).sum

  val totalAsString: String = formatCurrency(total)

  def totalRow(label: String, total: String, blankCells: Int): Seq[TableRow] =
    TableRow(content = Text(label), classes = "govuk-!-font-weight-bold") +:
      Seq.fill(blankCells)(TableRow(content = Text(""))) :+
      TableRow(content = Text(total), classes = "govuk-!-font-weight-bold")

}
