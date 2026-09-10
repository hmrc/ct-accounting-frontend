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

import viewmodels.{DebitInterestRow, DebitInterestViewModel}

import java.time.LocalDate

trait DebitInterestHelper {

  val defaultViewModel: DebitInterestViewModel = DebitInterestViewModel(
    interest = None,
    rows = List(
      DebitInterestRow(
        unpaidAmount = BigDecimal(17.01),
        fromDate = LocalDate.of(2026, 1, 1),
        toDate = LocalDate.of(2026, 1, 1),
        noOfDays = 1,
        rate = BigDecimal(0.75),
        interestAmount = BigDecimal(99.11)
      ),
      DebitInterestRow(
        unpaidAmount = BigDecimal(7.01),
        fromDate = LocalDate.of(2025, 2, 1),
        toDate = LocalDate.of(2025, 2, 1),
        noOfDays = 11,
        rate = BigDecimal(0.15),
        interestAmount = BigDecimal(278.13)
      ),
      DebitInterestRow(
        unpaidAmount = BigDecimal(87.01),
        fromDate = LocalDate.of(2015, 2, 1),
        toDate = LocalDate.of(2015, 2, 1),
        noOfDays = 89,
        rate = BigDecimal(14.5),
        interestAmount = BigDecimal(798.83)
      )
    )
  )

}
