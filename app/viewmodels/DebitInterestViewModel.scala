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

import uk.gov.hmrc.govukfrontend.views.viewmodels.content.Text
import uk.gov.hmrc.govukfrontend.views.viewmodels.table.TableRow
import views.ViewUtils.formatCurrency

import java.time.LocalDate

final case class OutstandingDebitInterest(
                                           interestType: String,
                                           totalAmount: BigDecimal
                                         )

final case class DebitInterestRow(
                                   unpaidAmount: BigDecimal,
                                   fromDate: LocalDate,
                                   toDate: LocalDate,
                                   noOfDays: Int,
                                   rate: BigDecimal,
                                   interestAmount: BigDecimal
                                 )

final case class DebitInterestViewModel(
                                         interest: Option[OutstandingDebitInterest],
                                         rows: List[DebitInterestRow]
                                       ) {

  val total: BigDecimal = rows.map(_.unpaidAmount).sum

  val totalAsString: String = formatCurrency(total)

  def totalRow(total: String, label: String): Seq[TableRow] =
    Seq(
      TableRow(content = Text(label), classes = "govuk-!-font-weight-bold"),
      TableRow(content = Text(""), classes = "govuk-!-font-weight-bold"),
      TableRow(content = Text(""), classes = "govuk-!-font-weight-bold"),
      TableRow(content = Text(""), classes = "govuk-!-font-weight-bold"),
      TableRow(content = Text(""), classes = "govuk-!-font-weight-bold"),
      TableRow(content = Text(total), classes = "govuk-!-font-weight-bold govuk-table__cell govuk-table__cell--numeric")
    )
}
