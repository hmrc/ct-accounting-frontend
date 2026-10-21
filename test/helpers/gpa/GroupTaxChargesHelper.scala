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

package helpers.gpa

import models.gpa.{GpaGroupTaxCharges, ParticipatorDetails}
import play.api.i18n.Messages
import viewmodels.gpa.GroupTaxChargesViewModel

import java.time.LocalDate

trait GroupTaxChargesHelper {

  val gpaGroupTaxChargesWithEmptyList: GpaGroupTaxCharges = GpaGroupTaxCharges(
    Some(LocalDate.of(2026, 3, 31)),
    Some(BigDecimal(0)),
    Some(BigDecimal(0)),
    Some("O"),
    Some(LocalDate.of(2026, 1, 1)),
    Some("E"),
    12345678901L,
    Some(0),
    Some(0),
    List.empty
  )

  val gpaGroupTaxChargesWithTwoParticipators: GpaGroupTaxCharges = GpaGroupTaxCharges(
    Some(LocalDate.of(2026, 3, 31)),
    Some(BigDecimal(125000.50)),
    Some(BigDecimal(31250.25)),
    Some("L"),
    Some(LocalDate.of(2026, 1, 1)),
    Some("E"),
    23456789012L,
    Some(2),
    Some(2),
    List(
      ParticipatorDetails(
        "Example Holdings Ltd",
        34567890123L,
        LocalDate.of(2026, 3, 31),
        Some(BigDecimal(18750.125)),
        "Y",
        2026001L,
        1L,
        Some(BigDecimal(62500.25)),
        1
      ),
      ParticipatorDetails(
        "Example Trading Ltd",
        45678901234L,
        LocalDate.of(2026, 3, 31),
        Some(BigDecimal(12500.125)),
        "Y",
        2026001L,
        1L,
        Some(BigDecimal(62500.25)),
        1
      )
    )
  )

  val gpaGroupTaxChargesWithFourParticipators: GpaGroupTaxCharges = GpaGroupTaxCharges(
    Some(LocalDate.of(2026, 3, 31)),
    Some(BigDecimal(250000.75)),
    Some(BigDecimal(62500.375)),
    Some("T"),
    Some(LocalDate.of(2026, 1, 1)),
    Some("E"),
    34567890123L,
    Some(4),
    Some(4),
    List(
      ParticipatorDetails(
        "Alpha Holdings Ltd",
        45678901234L,
        LocalDate.of(2026, 3, 31),
        Some(BigDecimal(18750.125)),
        "Y",
        2026001L,
        1L,
        Some(BigDecimal(62500.25)),
        1
      ),
      ParticipatorDetails(
        "Beta Trading Ltd",
        56789012345L,
        LocalDate.of(2026, 3, 31),
        Some(BigDecimal(12500.125)),
        "Y",
        2026001L,
        1L,
        Some(BigDecimal(62500.25)),
        1
      ),
      ParticipatorDetails(
        "Gamma Services Ltd",
        67890123456L,
        LocalDate.of(2026, 3, 31),
        Some(BigDecimal(15625.250)),
        "Y",
        2026001L,
        1L,
        Some(BigDecimal(62500.125)),
        1
      ),
      ParticipatorDetails(
        "Delta Group Ltd",
        78901234567L,
        LocalDate.of(2026, 3, 31),
        Some(BigDecimal(15624.875)),
        "Y",
        2026001L,
        1L,
        Some(BigDecimal(62500.125)),
        1
      )
    )
  )

  def returnGroupTaxChargesViewModel(
    taxCharges: GpaGroupTaxCharges,
    requestedPage: Option[Int],
    pageUrl: Int => String
  )(implicit messages: Messages): GroupTaxChargesViewModel =
    GroupTaxChargesViewModel.toViewModel(taxCharges, requestedPage, pageUrl)

}
