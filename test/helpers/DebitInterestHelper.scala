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

import models.{InterestAccrual, InterestAccrualList}
import viewmodels.{DebitInterestRow, DebitInterestViewModel}

import java.time.LocalDate

trait DebitInterestHelper {

  val defaultDebitInterestList: List[DebitInterestRow] = List(
    DebitInterestRow(
      unpaidAmount = BigDecimal(17.01),
      fromDate = LocalDate.of(2026, 1, 1),
      toDate = LocalDate.of(2026, 12, 12),
      noOfDays = 0,
      rate = BigDecimal(0.75),
      interestAmount = BigDecimal(99.11)
    )
  )

  val defaultRecord = InterestAccrualList(
    interestAccruals = List(
      InterestAccrual(
        computationAmount = BigDecimal(17.01),
        interestAccrualFromDate = LocalDate.of(2026, 1, 1),
        interestAccrualToDate = LocalDate.of(2026, 12, 12),
        interestRate = BigDecimal(0.75),
        interestAmount = BigDecimal(99.11),
        apEndDate = LocalDate.of(2026, 12, 31)
      )
    )
  )

  val defaultViewModel: DebitInterestViewModel = DebitInterestViewModel(
    accPeriodEndDate = LocalDate.of(2026, 1, 1),
    interest = None,
    rows = defaultDebitInterestList
  )

}
