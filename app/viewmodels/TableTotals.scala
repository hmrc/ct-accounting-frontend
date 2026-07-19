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
import views.ViewUtils.formatCurrency

object TableTotals {
  def totalRow(label: String, total: BigDecimal, blankCells: Int): Seq[TableRow] =
    TableRow(
      content = Text(label),
      classes = "govuk-!-font-weight-bold") +: //TODO Should this be bold or a header?
      Seq.fill(blankCells)(TableRow(content = Text(""))) :+
      TableRow(
        content = Text(formatCurrency(total)),
        classes = "govuk-!-font-weight-bold")
}
