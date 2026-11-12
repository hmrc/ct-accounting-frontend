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

package helpers.payments

import models.payments.{PaymentsOnAccountResponse, PaymentsOnAccountRow}
import java.time.LocalDate

trait PaymentsOnAccountResponseHelper {

  val paymentsOnAccountEmptyList: PaymentsOnAccountResponse = PaymentsOnAccountResponse(
    paymentsOnAccountRows = List.empty
  )

  val paymentsOnAccountSingleList: PaymentsOnAccountResponse = PaymentsOnAccountResponse(
    paymentsOnAccountRows = List(
      PaymentsOnAccountRow(
        accPeriod = 1,
        effectiveDateOfPayment = LocalDate.of(2025, 7, 14),
        apEndDate = Some(LocalDate.of(2026, 3, 31)),
        paymentType = "BAC",
        postingAmount = 12500.00,
        isPartPayment = false
      )
    )
  )

  val paymentsOnAccountMultipleList: PaymentsOnAccountResponse = PaymentsOnAccountResponse(
    paymentsOnAccountRows = List(
      PaymentsOnAccountRow(
        accPeriod = 1,
        effectiveDateOfPayment = LocalDate.of(2025, 7, 14),
        apEndDate = Some(LocalDate.of(2026, 3, 31)),
        paymentType = "BAC",
        postingAmount = 12500.00,
        isPartPayment = false
      ),
      PaymentsOnAccountRow(
        accPeriod = 1,
        effectiveDateOfPayment = LocalDate.of(2025, 10, 14),
        apEndDate = Some(LocalDate.of(2026, 3, 31)),
        paymentType = "BAC",
        postingAmount = 6250.50,
        isPartPayment = true
      ),
      PaymentsOnAccountRow(
        accPeriod = 1,
        effectiveDateOfPayment = LocalDate.of(2025, 11, 9),
        apEndDate = Some(LocalDate.of(2026, 3, 31)),
        paymentType = "BAC",
        postingAmount = 4504.00,
        isPartPayment = false
      ),
      PaymentsOnAccountRow(
        accPeriod = 1,
        effectiveDateOfPayment = LocalDate.of(2024, 11, 14),
        apEndDate = Some(LocalDate.of(2024, 3, 31)),
        paymentType = "BAC",
        postingAmount = 8423.00,
        isPartPayment = true
      ),
      PaymentsOnAccountRow(
        accPeriod = 1,
        effectiveDateOfPayment = LocalDate.of(2025, 7, 14),
        apEndDate = Some(LocalDate.of(2026, 3, 31)),
        paymentType = "BAC",
        postingAmount = 12500.00,
        isPartPayment = false
      ),
      PaymentsOnAccountRow(
        accPeriod = 1,
        effectiveDateOfPayment = LocalDate.of(2025, 7, 14),
        apEndDate = Some(LocalDate.of(2026, 3, 31)),
        paymentType = "BAC",
        postingAmount = 12500.00,
        isPartPayment = true
      )
    )
  )

  val paymentsOnAccountPaginatedList: PaymentsOnAccountResponse = PaymentsOnAccountResponse(
    paymentsOnAccountRows = List(
      PaymentsOnAccountRow(
        accPeriod = 1,
        effectiveDateOfPayment = LocalDate.of(2025, 7, 14),
        apEndDate = Some(LocalDate.of(2026, 3, 31)),
        paymentType = "BAC",
        postingAmount = 12500.00,
        isPartPayment = false
      ),
      PaymentsOnAccountRow(
        accPeriod = 1,
        effectiveDateOfPayment = LocalDate.of(2025, 10, 14),
        apEndDate = Some(LocalDate.of(2026, 3, 31)),
        paymentType = "BAC",
        postingAmount = 6250.50,
        isPartPayment = true
      ),
      PaymentsOnAccountRow(
        accPeriod = 1,
        effectiveDateOfPayment = LocalDate.of(2025, 11, 9),
        apEndDate = Some(LocalDate.of(2026, 3, 31)),
        paymentType = "BAC",
        postingAmount = 4504.00,
        isPartPayment = false
      ),
      PaymentsOnAccountRow(
        accPeriod = 1,
        effectiveDateOfPayment = LocalDate.of(2024, 11, 14),
        apEndDate = Some(LocalDate.of(2024, 3, 31)),
        paymentType = "BAC",
        postingAmount = 8423.00,
        isPartPayment = true
      ),
      PaymentsOnAccountRow(
        accPeriod = 1,
        effectiveDateOfPayment = LocalDate.of(2025, 7, 14),
        apEndDate = Some(LocalDate.of(2026, 3, 31)),
        paymentType = "BAC",
        postingAmount = 12500.00,
        isPartPayment = false
      ),
      PaymentsOnAccountRow(
        accPeriod = 1,
        effectiveDateOfPayment = LocalDate.of(2025, 7, 14),
        apEndDate = Some(LocalDate.of(2026, 3, 31)),
        paymentType = "BAC",
        postingAmount = 12500.00,
        isPartPayment = true
      ),
      PaymentsOnAccountRow(
        accPeriod = 1,
        effectiveDateOfPayment = LocalDate.of(2025, 7, 14),
        apEndDate = Some(LocalDate.of(2026, 3, 31)),
        paymentType = "BAC",
        postingAmount = 12500.00,
        isPartPayment = false
      ),
      PaymentsOnAccountRow(
        accPeriod = 1,
        effectiveDateOfPayment = LocalDate.of(2025, 10, 14),
        apEndDate = Some(LocalDate.of(2026, 3, 31)),
        paymentType = "BAC",
        postingAmount = 6250.50,
        isPartPayment = true
      ),
      PaymentsOnAccountRow(
        accPeriod = 1,
        effectiveDateOfPayment = LocalDate.of(2025, 11, 9),
        apEndDate = Some(LocalDate.of(2026, 3, 31)),
        paymentType = "BAC",
        postingAmount = 4504.00,
        isPartPayment = false
      ),
      PaymentsOnAccountRow(
        accPeriod = 1,
        effectiveDateOfPayment = LocalDate.of(2024, 11, 14),
        apEndDate = Some(LocalDate.of(2024, 3, 31)),
        paymentType = "BAC",
        postingAmount = 8423.00,
        isPartPayment = true
      ),
      PaymentsOnAccountRow(
        accPeriod = 1,
        effectiveDateOfPayment = LocalDate.of(2025, 7, 14),
        apEndDate = Some(LocalDate.of(2026, 3, 31)),
        paymentType = "BAC",
        postingAmount = 12500.00,
        isPartPayment = false
      ),
      PaymentsOnAccountRow(
        accPeriod = 1,
        effectiveDateOfPayment = LocalDate.of(2025, 7, 14),
        apEndDate = Some(LocalDate.of(2026, 3, 31)),
        paymentType = "BAC",
        postingAmount = 12500.00,
        isPartPayment = true
      ),
      PaymentsOnAccountRow(
        accPeriod = 1,
        effectiveDateOfPayment = LocalDate.of(2025, 7, 14),
        apEndDate = Some(LocalDate.of(2026, 3, 31)),
        paymentType = "BAC",
        postingAmount = 12500.00,
        isPartPayment = false
      ),
      PaymentsOnAccountRow(
        accPeriod = 1,
        effectiveDateOfPayment = LocalDate.of(2025, 10, 14),
        apEndDate = Some(LocalDate.of(2026, 3, 31)),
        paymentType = "BAC",
        postingAmount = 6250.50,
        isPartPayment = true
      ),
      PaymentsOnAccountRow(
        accPeriod = 1,
        effectiveDateOfPayment = LocalDate.of(2025, 11, 9),
        apEndDate = Some(LocalDate.of(2026, 3, 31)),
        paymentType = "BAC",
        postingAmount = 4504.00,
        isPartPayment = false
      ),
      PaymentsOnAccountRow(
        accPeriod = 1,
        effectiveDateOfPayment = LocalDate.of(2024, 11, 14),
        apEndDate = Some(LocalDate.of(2024, 3, 31)),
        paymentType = "BAC",
        postingAmount = 8423.00,
        isPartPayment = true
      ),
      PaymentsOnAccountRow(
        accPeriod = 1,
        effectiveDateOfPayment = LocalDate.of(2025, 7, 14),
        apEndDate = Some(LocalDate.of(2026, 3, 31)),
        paymentType = "BAC",
        postingAmount = 12500.00,
        isPartPayment = false
      ),
      PaymentsOnAccountRow(
        accPeriod = 1,
        effectiveDateOfPayment = LocalDate.of(2025, 7, 14),
        apEndDate = Some(LocalDate.of(2026, 3, 31)),
        paymentType = "BAC",
        postingAmount = 12500.00,
        isPartPayment = true
      ),
      PaymentsOnAccountRow(
        accPeriod = 1,
        effectiveDateOfPayment = LocalDate.of(2025, 7, 14),
        apEndDate = Some(LocalDate.of(2026, 3, 31)),
        paymentType = "BAC",
        postingAmount = 12500.00,
        isPartPayment = false
      ),
      PaymentsOnAccountRow(
        accPeriod = 1,
        effectiveDateOfPayment = LocalDate.of(2025, 10, 14),
        apEndDate = Some(LocalDate.of(2026, 3, 31)),
        paymentType = "BAC",
        postingAmount = 6250.50,
        isPartPayment = true
      ),
      PaymentsOnAccountRow(
        accPeriod = 1,
        effectiveDateOfPayment = LocalDate.of(2025, 11, 9),
        apEndDate = Some(LocalDate.of(2026, 3, 31)),
        paymentType = "BAC",
        postingAmount = 4504.00,
        isPartPayment = false
      ),
      PaymentsOnAccountRow(
        accPeriod = 1,
        effectiveDateOfPayment = LocalDate.of(2024, 11, 14),
        apEndDate = Some(LocalDate.of(2024, 3, 31)),
        paymentType = "BAC",
        postingAmount = 8423.00,
        isPartPayment = true
      ),
      PaymentsOnAccountRow(
        accPeriod = 1,
        effectiveDateOfPayment = LocalDate.of(2025, 7, 14),
        apEndDate = Some(LocalDate.of(2026, 3, 31)),
        paymentType = "BAC",
        postingAmount = 12500.00,
        isPartPayment = false
      ),
      PaymentsOnAccountRow(
        accPeriod = 1,
        effectiveDateOfPayment = LocalDate.of(2025, 7, 14),
        apEndDate = Some(LocalDate.of(2026, 3, 31)),
        paymentType = "BAC",
        postingAmount = 12500.00,
        isPartPayment = true
      ),
      PaymentsOnAccountRow(
        accPeriod = 1,
        effectiveDateOfPayment = LocalDate.of(2025, 11, 9),
        apEndDate = Some(LocalDate.of(2026, 3, 31)),
        paymentType = "BAC",
        postingAmount = 4504.00,
        isPartPayment = false
      ),
      PaymentsOnAccountRow(
        accPeriod = 1,
        effectiveDateOfPayment = LocalDate.of(2024, 11, 14),
        apEndDate = Some(LocalDate.of(2024, 3, 31)),
        paymentType = "BAC",
        postingAmount = 8423.00,
        isPartPayment = true
      ),
      PaymentsOnAccountRow(
        accPeriod = 1,
        effectiveDateOfPayment = LocalDate.of(2025, 7, 14),
        apEndDate = Some(LocalDate.of(2026, 3, 31)),
        paymentType = "BAC",
        postingAmount = 12500.00,
        isPartPayment = false
      ),
      PaymentsOnAccountRow(
        accPeriod = 1,
        effectiveDateOfPayment = LocalDate.of(2025, 7, 14),
        apEndDate = Some(LocalDate.of(2026, 3, 31)),
        paymentType = "BAC",
        postingAmount = 12500.00,
        isPartPayment = true
      ),
      PaymentsOnAccountRow(
        accPeriod = 1,
        effectiveDateOfPayment = LocalDate.of(2025, 7, 14),
        apEndDate = Some(LocalDate.of(2026, 3, 31)),
        paymentType = "BAC",
        postingAmount = 12500.00,
        isPartPayment = false
      ),
      PaymentsOnAccountRow(
        accPeriod = 1,
        effectiveDateOfPayment = LocalDate.of(2026, 9, 1),
        apEndDate = Some(LocalDate.of(2026, 10, 1)),
        paymentType = "BAC",
        postingAmount = 9999.99,
        isPartPayment = true
      )
    )
  )

}
