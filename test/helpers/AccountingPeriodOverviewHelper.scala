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

package helpers

import models.AccountingPeriodOverview
import play.api.i18n.Messages
import viewmodels.accountingPeriods.AccountingPeriodOverviewViewModel

import java.time.LocalDate

trait AccountingPeriodOverviewHelper {

  val accountingPeriodOverviewResponse: AccountingPeriodOverview =
    AccountingPeriodOverview(
      accountingPeriod = 1L,
      apStartDate = LocalDate.of(2025, 1, 1),
      apEndDate = LocalDate.of(2026, 1, 1),
      apStatus = "OPEN",
      taxChargePresent = true,
      clericalIntSig = true,
      creditDebitInterestInd = true,
      taxTotal = 100.00,
      interestTotal = 150.00,
      penaltyTotal = 100.00,
      payslipTotal = 300.00,
      repayReallocTotal = 250.00,
      adjustmentTotal = 155,
      clericalCalculationFlag = true,
      taxIsDisplayNeededFlag = true,
      interestIsDisplayNeededFlag = true,
      paymentIsDisplayNeededFlag = true,
      repayReallocIsDisplayNeededFlag = true
    )

  val accountingPeriodOverviewResponseNoDisplay: AccountingPeriodOverview =
    AccountingPeriodOverview(
      accountingPeriod = 1L,
      apStartDate = LocalDate.of(2025, 1, 1),
      apEndDate = LocalDate.of(2026, 1, 1),
      apStatus = "OPEN",
      taxChargePresent = true,
      clericalIntSig = true,
      creditDebitInterestInd = true,
      taxTotal = 0.00,
      interestTotal = 0.00,
      penaltyTotal = 100.00,
      payslipTotal = 0.00,
      repayReallocTotal = 0.00,
      adjustmentTotal = 155,
      clericalCalculationFlag = true,
      taxIsDisplayNeededFlag = false,
      interestIsDisplayNeededFlag = false,
      paymentIsDisplayNeededFlag = false,
      repayReallocIsDisplayNeededFlag = false
    )

  val accountingPeriodEndDate: LocalDate = LocalDate.of(2026, 1, 1)

  val taxReference: Long = 1L

  def viewModel(implicit messages: Messages): AccountingPeriodOverviewViewModel =
    AccountingPeriodOverviewViewModel.toViewModel(
      accountingPeriodEndDate,
      accountingPeriodOverviewResponse,
      taxReference
    )

  def viewModelNoDisplay(implicit messages: Messages): AccountingPeriodOverviewViewModel =
    AccountingPeriodOverviewViewModel.toViewModel(
      accountingPeriodEndDate,
      accountingPeriodOverviewResponseNoDisplay,
      taxReference
    )
}
