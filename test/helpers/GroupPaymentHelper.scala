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

import models.{GpaPaymentsDetailsResponse, GpaPaymentsRecord}

import java.time.LocalDate

trait GroupPaymentHelper {
  val response = GpaPaymentsDetailsResponse(
    gpaPayments = List(
      GpaPaymentsRecord(
        displayDate = Some(LocalDate.of(2025, 1, 1)),
        total = BigDecimal(17.01),
        tablename = Some("Payslip"),
        paymentType = Some("BGP"),
        repaymentType = None,
        targetTaxpayerReference = None,
        targetApNo = Some(7),
        targetApEndDate = None,
        contractEndDate = None,
        participatorPresent = Some(true)
      )
    ),
    totalNumOfRecords = Some(17),
    gppEndDate = Some(LocalDate.of(2026, 1, 1)),
    gppTotalGroupPayment = BigDecimal(1125017),
    gppTotalGroupTax = BigDecimal(1125012),
    gppStatus = "C",
    gppCni = None,
    gppApportionmentMethod = Some("METHOD")
  )
}
