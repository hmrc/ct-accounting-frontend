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
import play.api.i18n.Messages
import viewmodels.{PenaltiesAccountingPeriodViewModel, PenaltiesAccountingPeriodViewModelRow}

import java.time.LocalDate

trait PenaltiesDataHelper {

  val penaltyItems: PenaltiesResponse          =
    PenaltiesResponse(
      List(
        PenaltyTransactionItem(penaltyDate = LocalDate.of(2025, 5, 1), `type` = FX, postingAmount = BigDecimal(100.13)),
        PenaltyTransactionItem(penaltyDate = LocalDate.of(2021, 3, 7), `type` = TG, postingAmount = BigDecimal(27.19))
      )
    )
  val penaltyItemsForTypeFX: PenaltiesResponse =
    PenaltiesResponse(
      List(
        PenaltyTransactionItem(penaltyDate = LocalDate.of(2025, 5, 1), `type` = FX, postingAmount = BigDecimal(100.13))
      )
    )
  val penaltyItemsForTypeFT: PenaltiesResponse =
    PenaltiesResponse(
      List(
        PenaltyTransactionItem(penaltyDate = LocalDate.of(2025, 5, 1), `type` = FT, postingAmount = BigDecimal(100.13))
      )
    )
  val penaltyItemsForTypeTG: PenaltiesResponse =
    PenaltiesResponse(
      List(
        PenaltyTransactionItem(penaltyDate = LocalDate.of(2025, 5, 1), `type` = TG, postingAmount = BigDecimal(100.13))
      )
    )
  val penaltyItemsForTypeTR: PenaltiesResponse =
    PenaltiesResponse(
      List(
        PenaltyTransactionItem(penaltyDate = LocalDate.of(2025, 5, 1), `type` = TR, postingAmount = BigDecimal(100.13))
      )
    )

  def penaltiesViewModelForFX(implicit messages: Messages): List[PenaltiesAccountingPeriodViewModelRow] = List(
    PenaltiesAccountingPeriodViewModelRow(
      date = LocalDate.of(2025, 5, 1),
      description = messages("penaltiesAccountingPeriod.description.FX"),
      amount = BigDecimal(100.13)
    )
  )
  def penaltiesViewModelForFT(implicit messages: Messages): List[PenaltiesAccountingPeriodViewModelRow] = List(
    PenaltiesAccountingPeriodViewModelRow(
      date = LocalDate.of(2025, 5, 1),
      description = messages("penaltiesAccountingPeriod.description.FT"),
      amount = BigDecimal(100.13)
    )
  )
  def penaltiesViewModelForTR(implicit messages: Messages): List[PenaltiesAccountingPeriodViewModelRow] = List(
    PenaltiesAccountingPeriodViewModelRow(
      date = LocalDate.of(2025, 5, 1),
      description = messages("penaltiesAccountingPeriod.description.TR"),
      amount = BigDecimal(100.13)
    )
  )
  def penaltiesViewModelForTG(implicit messages: Messages): List[PenaltiesAccountingPeriodViewModelRow] = List(
    PenaltiesAccountingPeriodViewModelRow(
      date = LocalDate.of(2025, 5, 1),
      description = messages("penaltiesAccountingPeriod.description.TG"),
      amount = BigDecimal(100.13)
    )
  )

  def penaltiesViewModelTwoRows(implicit messages: Messages): List[PenaltiesAccountingPeriodViewModelRow] = List(
    PenaltiesAccountingPeriodViewModelRow(
      date = LocalDate.of(2025, 5, 1),
      description = messages("penaltiesAccountingPeriod.description.FX"),
      amount = BigDecimal(100.13)
    ),
    PenaltiesAccountingPeriodViewModelRow(
      date = LocalDate.of(2021, 3, 7),
      description = messages("penaltiesAccountingPeriod.description.FT"),
      amount = BigDecimal(27.19)
    )
  )

  def penaltiesViewModelSingleRow(implicit messages: Messages): List[PenaltiesAccountingPeriodViewModelRow] = List(
    PenaltiesAccountingPeriodViewModelRow(
      date = LocalDate.of(2025, 5, 1),
      description = messages("penaltiesAccountingPeriod.description.FT"),
      amount = BigDecimal(100.13)
    )
  )
  def penaltiesViewModelSingleRowForTaxGearedPenalty(implicit
    messages: Messages
  ): List[PenaltiesAccountingPeriodViewModelRow] = List(
    PenaltiesAccountingPeriodViewModelRow(
      date = LocalDate.of(2025, 5, 1),
      description = messages("penaltiesAccountingPeriod.description.TG"),
      amount = BigDecimal(100.13)
    )
  )
  def penaltiesViewModelSingleRowForTaxRelatedPenalty(implicit
    messages: Messages
  ): List[PenaltiesAccountingPeriodViewModelRow] = List(
    PenaltiesAccountingPeriodViewModelRow(
      date = LocalDate.of(2025, 5, 1),
      description = messages("penaltiesAccountingPeriod.description.TR"),
      amount = BigDecimal(100.13)
    )
  )

  def viewModelWithTwoRows(implicit messages: Messages): PenaltiesAccountingPeriodViewModel   =
    PenaltiesAccountingPeriodViewModel(
      accountingPeriodEnd = LocalDate.of(2025, 5, 1),
      penaltiesViewModelTwoRows
    )
  def viewModelWithSingleRow(implicit messages: Messages): PenaltiesAccountingPeriodViewModel =
    PenaltiesAccountingPeriodViewModel(
      accountingPeriodEnd = LocalDate.of(2025, 5, 1),
      penaltiesViewModelSingleRow
    )
  val viewModelWithNoRows: PenaltiesAccountingPeriodViewModel                                 =
    PenaltiesAccountingPeriodViewModel(accountingPeriodEnd = LocalDate.of(2025, 5, 1), List.empty)

}
