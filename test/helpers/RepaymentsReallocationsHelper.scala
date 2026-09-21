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

import models.{RepayReallocationSummary, RepayReallocationSummaryDetails}

import java.time.LocalDate

trait RepaymentsReallocationsHelper {
  val realloctionsFromSummary: RepayReallocationSummary = RepayReallocationSummary(
    transactions = List(
      RepayReallocationSummaryDetails(
        transactionDate = Some(LocalDate.of(2008, 10, 2)),
        `type` = Some("RFR"),
        amount = Some(BigDecimal("-56280")),
        accountingPeriodEndDate = Some(LocalDate.of(2003, 12, 20)),
        taxpayerReference = Some("8754000057")
      )
    )
  )

  val realloctionsToSummary: RepayReallocationSummary = RepayReallocationSummary(
    transactions = List(
      RepayReallocationSummaryDetails(
        transactionDate = Some(LocalDate.of(2007, 1, 5)),
        `type` = Some("RTO"),
        amount = Some(BigDecimal("56280")),
        accountingPeriodEndDate = Some(LocalDate.of(2003, 12, 31)),
        taxpayerReference = Some("8754000057")
      )
    )
  )

  val multipleSummaries: RepayReallocationSummary = RepayReallocationSummary(
    transactions = List(
      RepayReallocationSummaryDetails(
        transactionDate = Some(LocalDate.of(2008, 10, 2)),
        `type` = Some("RFR"),
        amount = Some(BigDecimal("-56280")),
        accountingPeriodEndDate = Some(LocalDate.of(2003, 12, 20)),
        taxpayerReference = Some("8754000057")
      ),
      RepayReallocationSummaryDetails(
        transactionDate = Some(LocalDate.of(2007, 1, 5)),
        `type` = Some("RTO"),
        amount = Some(BigDecimal("56280")),
        accountingPeriodEndDate = Some(LocalDate.of(2003, 12, 31)),
        taxpayerReference = Some("8754000057")
      )
    )
  )

  val summaryEmptyList: RepayReallocationSummary = RepayReallocationSummary(
    transactions = List.empty
  )

}
