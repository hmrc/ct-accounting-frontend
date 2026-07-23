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

import models.{AccountingPeriodDetails, AccountingPeriodResponse}
import play.api.mvc.Call
import uk.gov.hmrc.http.HttpVerbs.GET
import viewmodels.{InterestRow, InterestViewModel}

trait AccountingPeriodResponseHelper {

  val accountingPeriodResponse: AccountingPeriodResponse = AccountingPeriodResponse(
    AccountingPeriodDetails(
      isApBalanced = false,
      lpiCalcFlag = false,
      crDbCalcFlag = true,
      creditInterestAmount = BigDecimal("0.00"),
      debitInterestAmount = BigDecimal("15.75"),
      latePaymentInterestAmount = BigDecimal("25.50"),
      repaymentInterestAmount = BigDecimal("0.00"),
      totalDerivedActualInterest = BigDecimal("41.25"),
      amountDueForAp = BigDecimal("2500.00")
    )
  )

  val dummyCall: Call = Call(GET, "/")

  val accountingResponseEquivalentViewModel: InterestViewModel = InterestViewModel(
    Seq(
      InterestRow(
        description = "Late payment interest(still accruing)",
        amount = BigDecimal("25.50"),
        href = dummyCall
      ),
      InterestRow(
        description = "Repayment interest",
        amount = BigDecimal("0.00"),
        href = dummyCall
      ),
      InterestRow(
        description = "Debit interest",
        amount = BigDecimal("15.75"),
        href = dummyCall
      ),
      InterestRow(
        description = "Credit Interest",
        amount = BigDecimal("0.00"),
        href = dummyCall
      )
    )
  )

}
