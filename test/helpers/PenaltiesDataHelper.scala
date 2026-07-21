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

import models.PenaltyTransactionType.*
import models.{PenaltiesResponse, PenaltyTransactionItem}
import viewmodels.{PenaltiesAccountingPeriodViewModel, PenaltiesAccountingPeriodViewModelRow}

import java.time.LocalDate

trait PenaltiesDataHelper {

  val penaltyItems =
    PenaltiesResponse(
      List(
        PenaltyTransactionItem(penaltyDate = LocalDate.of(2025, 5, 1), `type` = FX, postingAmount = BigDecimal(100.13)),
        PenaltyTransactionItem(penaltyDate = LocalDate.of(2021, 3, 7), `type` = TG, postingAmount = BigDecimal(27.19))
      )
    )

  val penaltiesViewModelTwoRows: List[PenaltiesAccountingPeriodViewModelRow] = List(
    PenaltiesAccountingPeriodViewModelRow(
      date = LocalDate.of(2025, 5, 1),
      description = "Fixed rate penalty",
      amount = BigDecimal(100.13)
    ),
    PenaltiesAccountingPeriodViewModelRow(
      date = LocalDate.of(2021, 3, 7),
      description = "Tax geared penalty",
      amount = BigDecimal(27.19)
    )
  )

  val penaltiesViewModelSingleRow: List[PenaltiesAccountingPeriodViewModelRow] = List(
    PenaltiesAccountingPeriodViewModelRow(
      date = LocalDate.of(2025, 5, 1),
      description = "Fixed rate penalty",
      amount = BigDecimal(100.13)
    )
  )

  val viewModelWithTwoRows: PenaltiesAccountingPeriodViewModel   = PenaltiesAccountingPeriodViewModel(
    accountingPeriodEnd = LocalDate.of(2025, 5, 1),
    penaltiesViewModelTwoRows
  )
  val viewModelWithSingleRow: PenaltiesAccountingPeriodViewModel = PenaltiesAccountingPeriodViewModel(
    accountingPeriodEnd = LocalDate.of(2025, 5, 1),
    penaltiesViewModelSingleRow
  )
  val viewModelWithNoRows: PenaltiesAccountingPeriodViewModel    =
    PenaltiesAccountingPeriodViewModel(accountingPeriodEnd = LocalDate.of(2025, 5, 1), List.empty)

}
