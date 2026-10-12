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

import controllers.routes
import helpers.AccountPositionHelper
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec
import play.api.i18n.Messages
import play.api.test.Helpers.stubMessages
import viewmodels.accountingPeriods.AccountingPeriodsViewModel.toViewModel

import java.time.LocalDate

class AccountingPeriodsViewModelSpec extends AnyWordSpec with Matchers with AccountPositionHelper {

  implicit val messages: Messages = stubMessages()

  "AccountingPeriodsViewModel.toViewModel" should {
    "create the expected rows in AccountingPeriodsViewModel from AccountPosition" in {
      val viewModel = toViewModel(amountDue, accountPositionResponse, taxReference)

      viewModel.rows mustBe Seq(
        AccountingPeriodsViewModelRow(
          date = Some(LocalDate.of(2026, 1, 1)),
          amountDueForAp = 400.5,
          status = "accountingPeriods.status.description.LIVE",
          href = Some(routes.AccountingPeriodOverviewController.onPageLoad()),
          isLink = Some(true)
        ),
        AccountingPeriodsViewModelRow(
          date = Some(LocalDate.of(2025, 1, 1)),
          amountDueForAp = 123.50,
          status = "accountingPeriods.status.description.SUPERSEDED",
          href = Some(routes.AccountingPeriodOverviewController.onPageLoad()),
          isLink = Some(true)
        ),
        AccountingPeriodsViewModelRow(
          date = Some(LocalDate.of(2024, 1, 1)),
          amountDueForAp = 456.5,
          status = "accountingPeriods.status.description.EXEMPT",
          href = Some(routes.AccountingPeriodOverviewController.onPageLoad()),
          isLink = Some(true)
        ),
        AccountingPeriodsViewModelRow(
          date = Some(LocalDate.of(2023, 1, 1)),
          amountDueForAp = 789.5,
          status = "accountingPeriods.status.description.DORMANT",
          href = Some(routes.AccountingPeriodOverviewController.onPageLoad()),
          isLink = Some(true)
        )
      )
    }
  }
}
