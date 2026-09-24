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

import helpers.RepaymentsReallocationsHelper
import org.scalatest.matchers.must.Matchers
import org.scalatest.matchers.should.Matchers.should
import org.scalatest.wordspec.AnyWordSpec
import play.api.i18n.Messages
import play.api.mvc.Call
import play.api.test.Helpers.stubMessages
import views.ViewUtils.formatCurrency

import java.time.LocalDate

class RepaymentReallocationSummaryViewModelSpec extends AnyWordSpec with Matchers with RepaymentsReallocationsHelper {

  implicit val messages: Messages              = stubMessages()
  val repaymentReallocationsSummaryRoute: Call = controllers.routes.RepaymentsReallocationsController.onPageLoad()

  "RepaymentReallocationSummaryViewModel.toViewModel" should {

    "create the expected rows when retrieving Reallocations From Summary transactions" in {
      val viewModel = RepaymentReallocationSummaryViewModel.convertToViewModel(reallocationsFromSummary)

      viewModel.rows mustBe Seq(
        RepaymentReallocationSummaryViewModelRow(
          transactionDate = Some(LocalDate.of(2008, 10, 2)),
          description =
            messages("repaymentReallocations.description.rfr", Some(LocalDate.of(2008, 10, 2)), messages.lang),
          amount = Some(BigDecimal("-56280")),
          accountingPeriodEndDate = Some(LocalDate.of(2003, 12, 20)),
          taxpayerReference = Some("8754000057")
        )
      )
    }

    "create the expected rows when retrieving Reallocations To Summary transactions" in {
      val viewModel = RepaymentReallocationSummaryViewModel.convertToViewModel(reallocationsToSummary)

      viewModel.rows mustBe Seq(
        RepaymentReallocationSummaryViewModelRow(
          transactionDate = Some(LocalDate.of(2007, 1, 5)),
          description =
            messages("repaymentReallocations.description.rto", Some(LocalDate.of(2007, 1, 5)), messages.lang),
          amount = Some(BigDecimal("56280")),
          accountingPeriodEndDate = Some(LocalDate.of(2003, 12, 31)),
          taxpayerReference = Some("8754000057")
        )
      )
    }

    "create the expected rows when retrieving multiple summaries transactions" in {
      val viewModel = RepaymentReallocationSummaryViewModel.convertToViewModel(multipleSummaries)

      viewModel.rows mustBe Seq(
        RepaymentReallocationSummaryViewModelRow(
          transactionDate = Some(LocalDate.of(2008, 10, 2)),
          description =
            messages("repaymentReallocations.description.rfr", Some(LocalDate.of(2008, 10, 2)), messages.lang),
          amount = Some(BigDecimal("-56280")),
          accountingPeriodEndDate = Some(LocalDate.of(2003, 12, 20)),
          taxpayerReference = Some("8754000057")
        ),
        RepaymentReallocationSummaryViewModelRow(
          transactionDate = Some(LocalDate.of(2007, 1, 5)),
          description =
            messages("repaymentReallocations.description.rto", Some(LocalDate.of(2007, 1, 5)), messages.lang),
          amount = Some(BigDecimal("56280")),
          accountingPeriodEndDate = Some(LocalDate.of(2003, 12, 31)),
          taxpayerReference = Some("8754000057")
        )
      )
    }

    "calculate the total correctly" in {
      val viewModel = RepaymentReallocationSummaryViewModel.convertToViewModel(multipleSummariesWithNewTotal)

      viewModel.total mustBe BigDecimal("72675.71")
    }

    "format the total correctly" in {
      val viewModel = RepaymentReallocationSummaryViewModel.convertToViewModel(multipleSummariesWithNewTotal)

      viewModel.totalAsString mustBe formatCurrency(BigDecimal("72675.71"))
    }
    "format each row amount correctly" in {
      val viewModel = RepaymentReallocationSummaryViewModel.convertToViewModel(multipleSummariesWithNewTotal)

      viewModel.rows.map(_.amountAsString) mustBe Seq(
        formatCurrency(BigDecimal("6500.25")),
        formatCurrency(BigDecimal("27000.39")),
        formatCurrency(BigDecimal("-3214.05")),
        formatCurrency(BigDecimal("42389.12"))
      )
    }
  }
}
