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

import models.{AccountingPeriods, AccountingPeriodsRowResponse}

import java.time.LocalDate

trait AccountingPeriodsHelper {

  val emptyAccountingPeriods: AccountingPeriods = AccountingPeriods(accountingPeriods = List.empty)

  val accountingPeriodsWithOneItem: AccountingPeriods = AccountingPeriods(
    accountingPeriods = List(
      AccountingPeriodsRowResponse(
        accountingPeriod = BigDecimal(1),
        apStartDate = LocalDate.parse("2023-04-01"),
        apEndDate = LocalDate.parse("2024-03-31"),
        apStatus = "O",
        taxChargePresent = true,
        clericalIntSig = false,
        creditDebitInterestInd = true,
        taxTotal = BigDecimal(1500.50),
        interestTotal = BigDecimal(25.75),
        penaltyTotal = BigDecimal(0.00),
        payslipTotal = BigDecimal(1000.00),
        repayReallocTotal = BigDecimal(50.25),
        adjustmentTotal = BigDecimal(10.00)
      )
    )
  )

  val accountingPeriodsMultipleItems: AccountingPeriods = AccountingPeriods(
    accountingPeriods = List(
      AccountingPeriodsRowResponse(
        accountingPeriod = BigDecimal(1),
        apStartDate = LocalDate.of(2023,4,1),
        apEndDate = LocalDate.of(2024,3,31),
        apStatus = "O",
        taxChargePresent = true,
        clericalIntSig = false,
        creditDebitInterestInd = true,
        taxTotal = BigDecimal(1500.50),
        interestTotal = BigDecimal(25.75),
        penaltyTotal = BigDecimal(0.00),
        payslipTotal = BigDecimal(1000.00),
        repayReallocTotal = BigDecimal(50.25),
        adjustmentTotal = BigDecimal(10.00)
      ),
      AccountingPeriodsRowResponse(
        accountingPeriod = BigDecimal(2),
        apStartDate = LocalDate.of(2022,4,1),
        apEndDate = LocalDate.of(2023,3,31),
        apStatus = "C",
        taxChargePresent = false,
        clericalIntSig = true,
        creditDebitInterestInd = false,
        taxTotal = BigDecimal(2200.00),
        interestTotal = BigDecimal(0.00),
        penaltyTotal = BigDecimal(100.00),
        payslipTotal = BigDecimal(2000.00),
        repayReallocTotal = BigDecimal(0.00),
        adjustmentTotal = BigDecimal(100.00)
      )
    )
  )

}
