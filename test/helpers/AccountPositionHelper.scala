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

import models.{AccountPositionResponse, ApAmountRecord}
import play.api.i18n.Messages
import viewmodels.accountingPeriods.AccountingPeriodsViewModel

import java.time.LocalDate

trait AccountPositionHelper {

  val apAmounts: List[ApAmountRecord] =
    List(
      ApAmountRecord(
        accountingPeriod = 52L,
        apEndDate = Some(LocalDate.of(2026, 1, 1)),
        amountDueForAp = 400.50,
        apStatus = Some("L")
      ),
      ApAmountRecord(
        accountingPeriod = 53L,
        apEndDate = Some(LocalDate.of(2025, 1, 1)),
        amountDueForAp = 123.50,
        apStatus = Some("S")
      ),
      ApAmountRecord(
        accountingPeriod = 54L,
        apEndDate = Some(LocalDate.of(2024, 1, 1)),
        amountDueForAp = 456.50,
        apStatus = Some("E")
      ),
      ApAmountRecord(
        accountingPeriod = 55L,
        apEndDate = Some(LocalDate.of(2023, 1, 1)),
        amountDueForAp = 789.50,
        apStatus = Some("D")
      )
    )

  val accountPositionResponse: AccountPositionResponse =
    AccountPositionResponse(
      amountDue = 15.50,
      asOnDate = Some(LocalDate.of(2025, 1, 1)),
      gpaLinkFlag = true,
      taxpayerList = List(""),
      apAmounts = apAmounts,
      doesCompanyExist = true
    )

  val accountPositionResponseNoAccounts: AccountPositionResponse =
    AccountPositionResponse(
      amountDue = 0,
      asOnDate = Some(LocalDate.of(2025, 1, 1)),
      gpaLinkFlag = true,
      taxpayerList = List(""),
      apAmounts = List.empty,
      doesCompanyExist = true
    )

  val taxReference: Long = 1L

  val amountDue: BigDecimal = 15.50

  def viewModel(implicit messages: Messages): AccountingPeriodsViewModel =
    AccountingPeriodsViewModel.toViewModel(
      accountPositionResponse.amountDue,
      accountPositionResponse,
      taxReference
    )

  def viewModelNoAccounts(implicit messages: Messages): AccountingPeriodsViewModel =
    AccountingPeriodsViewModel.toViewModel(
      accountPositionResponseNoAccounts.amountDue,
      accountPositionResponseNoAccounts,
      taxReference
    )

}
