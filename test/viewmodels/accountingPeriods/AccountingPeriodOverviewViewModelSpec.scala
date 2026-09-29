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

import helpers.AccountingPeriodOverviewHelper
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec
import viewmodels.accountingPeriods.AccountingPeriodOverviewViewModel.toViewModel
import controllers.routes
import play.api.i18n.Messages
import play.api.test.Helpers.stubMessages


class AccountingPeriodOverviewViewModelSpec extends AnyWordSpec with Matchers with AccountingPeriodOverviewHelper {

  implicit val messages: Messages = stubMessages()

  "AccountingPeriodOverviewRow.toViewModel" should {
    "create the expected rows in AccountingPeriodOverviewViewModel from AccountingPeriodOverview" in {
      val viewModel = toViewModel(accountingPeriodEndDate, accountingPeriodOverviewResponse, taxReference)

      viewModel.rows mustBe Seq(
        AccountingPeriodOverviewRow(
          description = messages("accountingPeriodOverview.taxes"),
          amount = BigDecimal(100.00),
          isLink = Some(true),
          href = Some(routes.TaxTransactionsController.onPageLoad())
        ),
        AccountingPeriodOverviewRow(
          description = messages("accountingPeriodOverview.interest"),
          amount = BigDecimal(150.00),
          isLink = Some(true),
          href = Some(routes.InterestController.onPageLoad())
        ),
        AccountingPeriodOverviewRow(
          description = messages("accountingPeriodOverview.penalties"),
          amount = BigDecimal(100.00),
          isLink = Some(true),
          href = Some(routes.PenaltiesAccountingPeriodController.onPageLoad())
        ),
        AccountingPeriodOverviewRow(
          description = messages("accountingPeriodOverview.subtotal"),
          amount = BigDecimal(350.00),
          isLink = None,
          href = None,
          classes = "govuk-!-font-weight-bold"
        ),
        AccountingPeriodOverviewRow(
          description = messages("accountingPeriodOverview.payments"),
          amount = BigDecimal(300.00),
          isLink = Some(true),
          href = Some(routes.PaymentsController.onPageLoad())
        ),
        AccountingPeriodOverviewRow(
          description = messages("accountingPeriodOverview.repayments"),
          amount = BigDecimal(250.00),
          isLink = Some(true),
          href = Some(routes.RepaymentsReallocationsController.onPageLoad())
        ),
        AccountingPeriodOverviewRow(
          description = messages("accountingPeriodOverview.adjustments"),
          amount = BigDecimal(155.00),
          isLink = Some(true),
          href = Some(routes.AdjustmentsAccountingPeriodController.onPageLoad())
        )
      )
    }

    "create the expected rows in AccountingPeriodOverviewViewModel from AccountingPeriodOverview when all display needed flags are false" in {
      val viewModel = toViewModel(accountingPeriodEndDate, accountingPeriodOverviewResponseNoDisplay, taxReference)

      viewModel.rows mustBe Seq(
        AccountingPeriodOverviewRow(
          description = messages("accountingPeriodOverview.penalties"),
          amount = BigDecimal(100.00),
          isLink = Some(true),
          href = Some(routes.PenaltiesAccountingPeriodController.onPageLoad())
        ),
        AccountingPeriodOverviewRow(
          description = messages("accountingPeriodOverview.subtotal"),
          amount = BigDecimal(100.00),
          isLink = None,
          href = None,
          classes = "govuk-!-font-weight-bold"
        ),
        AccountingPeriodOverviewRow(
          description = messages("accountingPeriodOverview.adjustments"),
          amount = BigDecimal(155.00),
          isLink = Some(true),
          href = Some(routes.AdjustmentsAccountingPeriodController.onPageLoad())
        )
      )
    }

    "calculate the total" in {
      val total =
        accountingPeriodOverviewResponse.taxTotal + accountingPeriodOverviewResponse.interestTotal + accountingPeriodOverviewResponse.penaltyTotal + accountingPeriodOverviewResponse.payslipTotal + accountingPeriodOverviewResponse.repayReallocTotal + accountingPeriodOverviewResponse.adjustmentTotal

      val viewModel = toViewModel(accountingPeriodEndDate, accountingPeriodOverviewResponse, taxReference)
      viewModel.total mustBe total

    }

    "create the total row with two rows" in {
      val viewModel = toViewModel(accountingPeriodEndDate, accountingPeriodOverviewResponse, taxReference)
      val result    = viewModel.totalRow("label")

      result.size mustBe 2
    }

  }
}
