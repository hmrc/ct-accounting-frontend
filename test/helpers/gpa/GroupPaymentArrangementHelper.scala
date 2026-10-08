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

import viewmodels.gpa.GroupPaymentArrangementViewModel

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
      periodOfAccountEnding = accountingPeriodEndDate,
      groupPayments = groupPayments,
      groupTaxes = groupTaxes,
      status = gpaStatus
    )

}
