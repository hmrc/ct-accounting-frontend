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
import play.api.i18n.Lang
import play.api.i18n.Messages
import views.ViewUtils.*
import models.AccountPositionResponse
import play.api.mvc.Call

import java.time.LocalDate

case class AccountingPeriodsViewModelRow(
  date: LocalDate,
  apAmount: BigDecimal,
  status: Option[String],
  href: Option [Call],
  isLink: Option[Boolean]
) {
  val dateAsString: String = formatDate(date, Lang.defaultLang)
  val apAmountAsString: String = formatCurrency(apAmount)
}

case class AccountingPeriodsViewModel(
  taxReference: Long,
  amountDue: Option[BigDecimal],
  rows: Seq[AccountingPeriodsViewModelRow]
)

object AccountingPeriodsViewModel {
  def toViewModel(
    amountDue: Option[BigDecimal],
    accountingPeriods: AccountPositionResponse,
    taxReference: Long
  )(implicit messages: Messages): AccountingPeriodsViewModel = {
    AccountingPeriodsViewModel(
      amountDue = amountDue,
      taxReference = taxReference,
      rows = accountingPeriods.apAmounts.map { value =>
        AccountingPeriodsViewModelRow(
          date = value.apEndDate.get, //TODO: DTR-6447 - Sort .get here and below
          apAmount = value.amountDueForAp.get,
          status = value.apStatus,
          href = Some(controllers.routes.AccountingPeriodOverviewController.onPageLoad()),
          isLink = Some(true)
        )
      }
    )
  }
}
