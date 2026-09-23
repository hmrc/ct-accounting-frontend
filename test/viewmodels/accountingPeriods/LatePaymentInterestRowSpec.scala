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

import helpers.InterestAccrualListHelper
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec
import viewmodels.accountingPeriods.LatePaymentInterestRow.toViewModel
import views.ViewUtils.formatCurrency

import java.time.LocalDate

class LatePaymentInterestRowSpec extends AnyWordSpec with Matchers with InterestAccrualListHelper {

  "LatePaymentInterestRow.toViewModel" should {
    "create the expected rows in LatePaymentInterestViewModel from InterestAccrualListWithInterestAccruedDays" in {
      val viewModel = toViewModel(interestAccrualMultipleObjects)

      viewModel.rows mustBe Seq(
        LatePaymentInterestRow(
          amountSubjectToInterest = BigDecimal(10000.00),
          fromDate = LocalDate.of(2024, 4, 1),
          toDate = LocalDate.of(2024, 6, 30),
          daysOverdue = 91,
          annualPercentageRate = BigDecimal(7.75),
          interestAccrued = BigDecimal("193.22")
        ),
        LatePaymentInterestRow(
          amountSubjectToInterest = BigDecimal(10000.00),
          fromDate = LocalDate.of(2024, 7, 1),
          toDate = LocalDate.of(2024, 9, 30),
          daysOverdue = 92,
          annualPercentageRate = BigDecimal(8.25),
          interestAccrued = BigDecimal("208.02")
        )
      )
    }

    "calculate the total and format correctly" in {
      val totalInterestAccrued = interestAccrualMultipleObjects.interestAccruals.map(_.interestAmount).sum
      val viewModel            = toViewModel(interestAccrualMultipleObjects)
      viewModel.total mustBe totalInterestAccrued
      viewModel.totalAsString mustBe formatCurrency(totalInterestAccrued)
    }

    "create the total row with six rows" in {
      val viewModel = toViewModel(interestAccrualMultipleObjects)
      val result    = viewModel.totalRow("label", "value")
      result.size mustBe 6
    }

  }
}
