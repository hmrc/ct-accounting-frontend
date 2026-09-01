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

import models.{
  AccountingPeriodDetails, AccountingPeriodDetailsResponse, AccountingPeriods, AccountingPeriodsRowResponse
}
import play.api.i18n.Messages
import play.api.mvc.Call
import uk.gov.hmrc.http.HttpVerbs.GET
import viewmodels.{InterestRow, InterestViewModel}

import java.time.LocalDate

trait InterestViewModelHelper {

  val accountingPeriodDetailsResponseForAccruing: AccountingPeriodDetailsResponse    =
    AccountingPeriodDetailsResponse(accountingPeriodDetails =
      AccountingPeriodDetails(
        isApBalanced = true,
        lpiCalcFlag = true,
        crDbCalcFlag = true,
        creditInterestAmount = BigDecimal("0.00"),
        debitInterestAmount = BigDecimal("15.75"),
        latePaymentInterestAmount = BigDecimal("25.50"),
        repaymentInterestAmount = BigDecimal("10.00"),
        totalDerivedActualInterest = BigDecimal("41.25"),
        amountDueForAp = BigDecimal("2500.00")
      )
    )
  val accountingPeriodDetailsResponseForNotAccruing: AccountingPeriodDetailsResponse =
    AccountingPeriodDetailsResponse(accountingPeriodDetails =
      AccountingPeriodDetails(
        isApBalanced = true,
        lpiCalcFlag = false,
        crDbCalcFlag = false,
        creditInterestAmount = BigDecimal("100.00"),
        debitInterestAmount = BigDecimal("15.75"),
        latePaymentInterestAmount = BigDecimal("25.50"),
        repaymentInterestAmount = BigDecimal("12.00"),
        totalDerivedActualInterest = BigDecimal("41.25"),
        amountDueForAp = BigDecimal("2500.00")
      )
    )

  val accountingPeriodsWithFalseClericalIntSigAndCreditDebitInterest: AccountingPeriods = AccountingPeriods(
    accountingPeriods = List(
      AccountingPeriodsRowResponse(
        accountingPeriod = BigDecimal(345L),
        apStartDate = LocalDate.of(2023, 4, 1),
        apEndDate = LocalDate.of(2024, 3, 31),
        apStatus = "O",
        taxChargePresent = true,
        clericalIntSig = false,
        creditDebitInterestInd = false,
        taxTotal = BigDecimal(1500.50),
        interestTotal = BigDecimal(25.75),
        penaltyTotal = BigDecimal(0.00),
        payslipTotal = BigDecimal(1000.00),
        repayReallocTotal = BigDecimal(50.25),
        adjustmentTotal = BigDecimal(10.00)
      ),
      AccountingPeriodsRowResponse(
        accountingPeriod = BigDecimal(2),
        apStartDate = LocalDate.of(2022, 4, 1),
        apEndDate = LocalDate.of(2023, 3, 31),
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
  val accountingPeriodsWithNoMatchingAccountingPeriods: AccountingPeriods               = AccountingPeriods(
    accountingPeriods = List(
      AccountingPeriodsRowResponse(
        accountingPeriod = BigDecimal(112L),
        apStartDate = LocalDate.of(2023, 4, 1),
        apEndDate = LocalDate.of(2024, 3, 31),
        apStatus = "O",
        taxChargePresent = true,
        clericalIntSig = false,
        creditDebitInterestInd = false,
        taxTotal = BigDecimal(1500.50),
        interestTotal = BigDecimal(25.75),
        penaltyTotal = BigDecimal(0.00),
        payslipTotal = BigDecimal(1000.00),
        repayReallocTotal = BigDecimal(50.25),
        adjustmentTotal = BigDecimal(10.00)
      ),
      AccountingPeriodsRowResponse(
        accountingPeriod = BigDecimal(2),
        apStartDate = LocalDate.of(2022, 4, 1),
        apEndDate = LocalDate.of(2023, 3, 31),
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
  val accountingPeriodsForTrueClericalIntSig: AccountingPeriods                         = AccountingPeriods(
    accountingPeriods = List(
      AccountingPeriodsRowResponse(
        accountingPeriod = BigDecimal(345L),
        apStartDate = LocalDate.of(2023, 4, 1),
        apEndDate = LocalDate.of(2024, 3, 31),
        apStatus = "O",
        taxChargePresent = true,
        clericalIntSig = true,
        creditDebitInterestInd = false,
        taxTotal = BigDecimal(1500.50),
        interestTotal = BigDecimal(25.75),
        penaltyTotal = BigDecimal(0.00),
        payslipTotal = BigDecimal(1000.00),
        repayReallocTotal = BigDecimal(50.25),
        adjustmentTotal = BigDecimal(10.00)
      ),
      AccountingPeriodsRowResponse(
        accountingPeriod = BigDecimal(2),
        apStartDate = LocalDate.of(2022, 4, 1),
        apEndDate = LocalDate.of(2023, 3, 31),
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
  val accountingPeriodsForTrueCreditDebitInterestInd: AccountingPeriods                 = AccountingPeriods(
    accountingPeriods = List(
      AccountingPeriodsRowResponse(
        accountingPeriod = BigDecimal(345L),
        apStartDate = LocalDate.of(2023, 4, 1),
        apEndDate = LocalDate.of(2024, 3, 31),
        apStatus = "O",
        taxChargePresent = true,
        clericalIntSig = true,
        creditDebitInterestInd = false,
        taxTotal = BigDecimal(1500.50),
        interestTotal = BigDecimal(25.75),
        penaltyTotal = BigDecimal(0.00),
        payslipTotal = BigDecimal(1000.00),
        repayReallocTotal = BigDecimal(50.25),
        adjustmentTotal = BigDecimal(10.00)
      ),
      AccountingPeriodsRowResponse(
        accountingPeriod = BigDecimal(2),
        apStartDate = LocalDate.of(2022, 4, 1),
        apEndDate = LocalDate.of(2023, 3, 31),
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

  val dummyCall: Call = Call(GET, "/")

  def interestViewModelForAccruing(implicit messages: Messages): InterestViewModel                                = InterestViewModel(
    Seq(
      InterestRow(
        description = messages("interest.table.latePayment.accruing"),
        amount = BigDecimal("25.50"),
        isLink = false,
        href = dummyCall
      ),
      InterestRow(
        description = messages("interest.table.repaymentInterest"),
        amount = BigDecimal("10.00"),
        isLink = true,
        href = dummyCall
      ),
      InterestRow(
        description = messages("interest.table.debitInterest.accruing"),
        amount = BigDecimal("15.75"),
        isLink = false,
        href = dummyCall
      ),
      InterestRow(
        description = messages("interest.table.creditInterest.accruing"),
        amount = BigDecimal("0.00"),
        isLink = false,
        href = dummyCall
      )
    )
  )
  def interestViewModelForAccruingWithRepaymentRowNotHyperLink(implicit messages: Messages): InterestViewModel    =
    InterestViewModel(
      Seq(
        InterestRow(
          description = messages("interest.table.latePayment.accruing"),
          amount = BigDecimal("25.50"),
          isLink = false,
          href = dummyCall
        ),
        InterestRow(
          description = messages("interest.table.repaymentInterest"),
          amount = BigDecimal("10.00"),
          isLink = false,
          href = dummyCall
        ),
        InterestRow(
          description = messages("interest.table.debitInterest.accruing"),
          amount = BigDecimal("15.75"),
          isLink = false,
          href = dummyCall
        ),
        InterestRow(
          description = messages("interest.table.creditInterest.accruing"),
          amount = BigDecimal("0.00"),
          isLink = false,
          href = dummyCall
        )
      )
    )
  def interestViewModelForNotAccruingWithRepaymentRowNotHyperLink(implicit messages: Messages): InterestViewModel =
    InterestViewModel(
      Seq(
        InterestRow(
          description = messages("interest.table.latePayment"),
          amount = BigDecimal("25.50"),
          isLink = true,
          href = dummyCall
        ),
        InterestRow(
          description = messages("interest.table.repaymentInterest"),
          amount = BigDecimal("12.00"),
          isLink = false,
          href = dummyCall
        ),
        InterestRow(
          description = messages("interest.table.debitInterest"),
          amount = BigDecimal("15.75"),
          isLink = true,
          href = dummyCall
        ),
        InterestRow(
          description = messages("interest.table.creditInterest"),
          amount = BigDecimal("100.00"),
          isLink = true,
          href = dummyCall
        )
      )
    )

}
