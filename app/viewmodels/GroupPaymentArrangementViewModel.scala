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

import play.api.i18n.Messages
import uk.gov.hmrc.govukfrontend.views.Aliases.Text
import uk.gov.hmrc.govukfrontend.views.viewmodels.summarylist.SummaryList
import viewmodels.govuk.summarylist.*
import viewmodels.implicits.*
import views.ViewUtils.{formatCurrency, formatDate}

import java.time.LocalDate

case class GroupPaymentArrangementViewModel(
  arrangementReference: String,
  accountEnding: LocalDate,
  accountStatus: String,
  paymentTotal: BigDecimal,
  taxTotal: BigDecimal,
  records: List[GroupPaymentRecord]
) {
  def summary(implicit messages: Messages): SummaryList = SummaryListViewModel(
    rows = Seq(
      SummaryListRowViewModel(
        key = "groupPaymentsArrangements.ArrangementReference",
        value = ValueViewModel(Text(this.arrangementReference))
      ),
      SummaryListRowViewModel(
        key = "groupPaymentsArrangements.PeriodAccountEnding",
        value = ValueViewModel(Text(formatDate(this.accountEnding, messages.lang)))
      ),
      SummaryListRowViewModel(
        key = "groupPaymentsArrangements.PeriodAccountStatus",
        value = ValueViewModel(Text(this.accountStatus))
      ),
      SummaryListRowViewModel(
        key = "groupPaymentsArrangements.PaymentTotal",
        value = ValueViewModel(Text(formatCurrency(paymentTotal)))
      ),
      SummaryListRowViewModel(
        key = "groupPaymentsArrangements.TaxTotal",
        value = ValueViewModel(Text(formatCurrency(taxTotal)))
      )
    )
  )
}

case class GroupPaymentRecord(date: LocalDate, description: String, amount: BigDecimal)
