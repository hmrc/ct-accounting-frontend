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
import play.api.i18n.Messages
import views.ViewUtils.*
import models.AccountPositionResponse
import play.api.mvc.Call
import utils.AccountingPeriodsStatusHelper

import java.time.LocalDate

case class AccountingPeriodsViewModelRow(
  date: Option[LocalDate],
  amountDueForAp: BigDecimal,
  status: String,
  href: Option [Call],
  isLink: Option[Boolean]
)(implicit messages: Messages) {
  val dateAsString: String = date
    .map(formatDate(_, messages.lang))
    .getOrElse("")
  val apAmountAsString: String = formatCurrency(amountDueForAp)
}

case class AccountingPeriodsViewModel(
  taxReference: Long,
  amountDue: BigDecimal,
  displayTable: Boolean,
  rows: Seq[AccountingPeriodsViewModelRow]
)

object AccountingPeriodsViewModel {
  def toViewModel(
    amountDue: BigDecimal,
    accountingPeriods: AccountPositionResponse,
    taxReference: Long
  )(implicit messages: Messages): AccountingPeriodsViewModel = {

    val displayTable: Boolean = accountingPeriods.apAmounts.nonEmpty && amountDue.signum != 0

    AccountingPeriodsViewModel(
      amountDue = amountDue,
      taxReference = taxReference,
      displayTable = displayTable,
      rows = accountingPeriods.apAmounts.map { value =>
        AccountingPeriodsViewModelRow(
          date = value.apEndDate,
          amountDueForAp = value.amountDueForAp,
          status = AccountingPeriodsStatusHelper.getAccountPeriodsStatusDescription(value.apStatus),
          href = Some(controllers.routes.AccountingPeriodOverviewController.onPageLoad()),
          isLink = Some(true)
        )
      }
    )
  }
}
