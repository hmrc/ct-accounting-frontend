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

import models.AdjustmentTransactionType.{N, O, P}
import models.{AdjustmentTransactions, AdjustmentTransactionsList}
import viewmodels.{AdjustmentsAccountingPeriodViewModel, AdjustmentsAccountingPeriodViewModelRow}

trait AdjustmentsDataHelper {

  val emptyAdjustmentTransactionsList: AdjustmentTransactionsList =
    AdjustmentTransactionsList(adjustmentTransactionsList = List.empty)

  val emptyAdjustmentsViewModel: AdjustmentsAccountingPeriodViewModel =
    AdjustmentsAccountingPeriodViewModel(rows = List.empty)

  val adjustmentTransactionsListWithOneItem: AdjustmentTransactionsList =
    AdjustmentTransactionsList(
      List(
        AdjustmentTransactions(
          amount = BigDecimal(50.00),
          `type` = N
        )
      )
    )

  val adjustmentsViewModelWithOneItem: AdjustmentsAccountingPeriodViewModel =
    AdjustmentsAccountingPeriodViewModel(
      List(
        AdjustmentsAccountingPeriodViewModelRow(
          description = "Not currently being pursued",
          amount = BigDecimal(50.00)
        )
      )
    )

  val adjustmentTransactionsListWithMultipleItems: AdjustmentTransactionsList =
    AdjustmentTransactionsList(
      List(
        AdjustmentTransactions(
          amount = BigDecimal(60.00),
          `type` = O
        ),
        AdjustmentTransactions(
          amount = BigDecimal(190.00),
          `type` = P
        )
      )
    )

  val adjustmentsViewModelWithMultipleItems: AdjustmentsAccountingPeriodViewModel =
    AdjustmentsAccountingPeriodViewModel(
      List(
        AdjustmentsAccountingPeriodViewModelRow(
          description = "Permanent overpayment",
          amount = BigDecimal(60.00)
        ),
        AdjustmentsAccountingPeriodViewModelRow(
          description = "Postponement",
          amount = BigDecimal(190.00)
        )
      )
    )
}
