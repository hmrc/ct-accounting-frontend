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

import controllers.gpa.routes
import viewmodels.gpa.{
  GroupPaymentArrangementPaymentRow, GroupPaymentArrangementViewModel, GroupPaymentArrangementViewRow
}

import java.time.LocalDate

trait GroupPaymentArrangementHelper {

  val accountingPeriodEndDate: LocalDate = LocalDate.of(2026, 1, 1)
  val taxPayerReference: Long            = 1L
  val accPeriod                          = 1L
  val groupPayments                      = -1536642.00
  val groupTaxes                         = 1536642.00
  val gpaStatus                          = "Open"

  val groupPaymentArrangementViewModel: GroupPaymentArrangementViewModel =
    GroupPaymentArrangementViewModel(
      taxPayerReference = taxPayerReference,
      rows = Seq(
        GroupPaymentArrangementViewRow(
          periodOfAccountEnding = LocalDate.of(2026, 1, 1),
          groupPayments = GroupPaymentArrangementPaymentRow(
            description = "£11.01",
            amount = 11.01,
            isLink = true,
            href = routes.GroupPaymentArrangementController.onPageLoad()
          ),
          groupTaxes = GroupPaymentArrangementPaymentRow(
            description = "£13.01",
            amount = 13.01,
            isLink = true,
            href = routes.GroupPaymentArrangementController.onPageLoad()
          ),
          status = "ACTIVE"
        ),
        GroupPaymentArrangementViewRow(
          periodOfAccountEnding = LocalDate.of(2026, 5, 4),
          groupPayments = GroupPaymentArrangementPaymentRow(
            description = "-£430.32",
            amount = -430.32,
            isLink = true,
            href = routes.GroupPaymentArrangementController.onPageLoad()
          ),
          groupTaxes = GroupPaymentArrangementPaymentRow(
            description = "-£700.53",
            amount = -700.53,
            isLink = true,
            href = routes.GroupPaymentArrangementController.onPageLoad()
          ),
          status = "CLOSED"
        ),
        GroupPaymentArrangementViewRow(
          periodOfAccountEnding = LocalDate.of(2024, 8, 12),
          groupPayments = GroupPaymentArrangementPaymentRow(
            description = "-£952.12",
            amount = -952.12,
            isLink = true,
            href = routes.GroupPaymentArrangementController.onPageLoad()
          ),
          groupTaxes = GroupPaymentArrangementPaymentRow(
            description = "-£691.76",
            amount = -691.76,
            isLink = true,
            href = routes.GroupPaymentArrangementController.onPageLoad()
          ),
          status = "CLOSED"
        )
      )
    )

}
