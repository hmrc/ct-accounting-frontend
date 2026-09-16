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

import models.{InterestAccrualListWithInterestAccruedDays, InterestAccrualWithInterestAccruedDays}

import java.time.LocalDate

trait InterestAccrualListHelper {

  val emptyInterestAccrualList: InterestAccrualListWithInterestAccruedDays =
    InterestAccrualListWithInterestAccruedDays(interestAccruals = List.empty)

  val interestAccrualMultipleObjects: InterestAccrualListWithInterestAccruedDays =
    InterestAccrualListWithInterestAccruedDays(
      interestAccruals = List(
        InterestAccrualWithInterestAccruedDays(
          computationAmount = BigDecimal(10000.00),
          interestAccrualFromDate = LocalDate.of(2024, 4, 1),
          interestAccrualToDate = LocalDate.of(2024, 6, 30),
          interestRate = BigDecimal(7.75),
          interestAmount = BigDecimal(193.22),
          apEndDate = LocalDate.of(2024, 3, 31),
          noOfDays = 91
        ),
        InterestAccrualWithInterestAccruedDays(
          computationAmount = BigDecimal(10000.00),
          interestAccrualFromDate = LocalDate.of(2024, 7, 1),
          interestAccrualToDate = LocalDate.of(2024, 9, 30),
          interestRate = BigDecimal(8.25),
          interestAmount = BigDecimal(208.02),
          apEndDate = LocalDate.of(2024, 3, 31),
          noOfDays = 92
        )
      )
    )

  val interestAccrualSingleObject: InterestAccrualListWithInterestAccruedDays =
    InterestAccrualListWithInterestAccruedDays(
      interestAccruals = List(
        InterestAccrualWithInterestAccruedDays(
          computationAmount = BigDecimal("10000.00"),
          interestAccrualFromDate = LocalDate.of(2024, 4, 1),
          interestAccrualToDate = LocalDate.of(2024, 6, 30),
          interestRate = BigDecimal("7.75"),
          interestAmount = BigDecimal("193.22"),
          apEndDate = LocalDate.of(2024, 3, 31),
          noOfDays = 91
        )
      )
    )

}
