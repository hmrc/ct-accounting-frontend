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

import models.gpa.{AllocationDetails, PaymentAllocationDetails}
import play.api.i18n.Messages
import utils.PaginationHelper
import viewmodels.Paginated
import viewmodels.gpa.{
  AllocationDetailsViewModelRow, PaymentAllocationDetailsViewModel, PaymentAllocationDetailsViewModelRow
}

import java.time.LocalDate

trait PaymentAllocationDetailsHelper {
  implicit val msgs: Messages

  val fullPaymentAllocationDetails: PaymentAllocationDetails =
    PaymentAllocationDetails(
      gppEndDate = LocalDate.of(2024, 7, 24),
      gppTotalGroupPayment = BigDecimal(10),
      gppTotalGroupTax = BigDecimal(10),
      gppStatus = "O",
      gppApportionmentMethod = "O",
      participatingCompanyDesc = "ABC Limited",
      participatorAccPeriodEnd = LocalDate.of(2024, 7, 24),
      participatorTaxCharge = BigDecimal(10),
      participatorAllocPayments = BigDecimal(10),
      gpaUtr = BigDecimal(10),
      gppContractVersionOut = BigDecimal(10),
      allocationDetails = List(
        AllocationDetails(
          effectivePaymentDate = LocalDate.of(2024, 7, 24),
          paymentAmount = BigDecimal(10)
        )
      ),
      totalNumOfRecords = BigDecimal(10)
    )

  val paymentAllocationDetailsWithMultipleAllocationDetails: PaymentAllocationDetails =
    PaymentAllocationDetails(
      gppEndDate = LocalDate.of(2025, 7, 24),
      gppTotalGroupPayment = BigDecimal(20),
      gppTotalGroupTax = BigDecimal(20),
      gppStatus = "C",
      gppApportionmentMethod = "C",
      participatingCompanyDesc = "DEF Limited",
      participatorAccPeriodEnd = LocalDate.of(2025, 7, 24),
      participatorTaxCharge = BigDecimal(20),
      participatorAllocPayments = BigDecimal(20),
      gpaUtr = BigDecimal(20),
      gppContractVersionOut = BigDecimal(20),
      allocationDetails = List(
        AllocationDetails(
          effectivePaymentDate = LocalDate.of(2025, 7, 24),
          paymentAmount = BigDecimal(20)
        ),
        AllocationDetails(
          effectivePaymentDate = LocalDate.of(2025, 7, 24),
          paymentAmount = BigDecimal(20)
        )
      ),
      totalNumOfRecords = BigDecimal(20)
    )

  val minimalPaymentAllocationDetails: PaymentAllocationDetails =
    PaymentAllocationDetails(
      gppEndDate = LocalDate.of(2026, 7, 24),
      gppTotalGroupPayment = BigDecimal(30),
      gppTotalGroupTax = BigDecimal(30),
      gppStatus = "P",
      gppApportionmentMethod = "P",
      participatingCompanyDesc = "GHI Limited",
      participatorAccPeriodEnd = LocalDate.of(2026, 7, 24),
      participatorTaxCharge = BigDecimal(30),
      participatorAllocPayments = BigDecimal(30),
      gpaUtr = BigDecimal(30),
      gppContractVersionOut = BigDecimal(30),
      allocationDetails = List(
        AllocationDetails(
          effectivePaymentDate = LocalDate.of(2026, 7, 24),
          paymentAmount = BigDecimal(30)
        )
      ),
      totalNumOfRecords = BigDecimal(30)
    )

  val allocRows = List(
    AllocationDetailsViewModelRow(
      paymentDate = "24 Jul 2024",
      allocatedPaymentAmount = "£10.00"
    )
  )
  val page      = Paginated.alreadyPaginated(allocRows, Some(1), fullPaymentAllocationDetails.totalNumOfRecords.toInt)

  val fullPaymentAllocationDetailsVM: PaymentAllocationDetailsViewModel = PaymentAllocationDetailsViewModel(
    rows = List(
      PaymentAllocationDetailsViewModelRow(
        rowName = "Group Payment Arrangement reference",
        content = "10",
        isLink = false,
        href = None
      ),
      PaymentAllocationDetailsViewModelRow(
        rowName = "Participating company",
        content = "ABC Limited",
        isLink = false,
        href = None
      ),
      PaymentAllocationDetailsViewModelRow(
        rowName = "Accounting period ending",
        content = "24 Jul 2024",
        isLink = false,
        href = None
      ),
      PaymentAllocationDetailsViewModelRow(
        rowName = "Period of account ending",
        content = "24 Jul 2024",
        isLink = false,
        href = None
      ),
      PaymentAllocationDetailsViewModelRow(
        rowName = "Group period of account status",
        content = "O",
        isLink = false,
        href = None
      ),
      PaymentAllocationDetailsViewModelRow(
        rowName = "Tax charge as of Cleared status",
        content = "£10.00",
        isLink = false,
        href = None
      ),
      PaymentAllocationDetailsViewModelRow(
        rowName = "Payment allocated by",
        content = "O",
        isLink = false,
        href = None
      ),
      PaymentAllocationDetailsViewModelRow(
        rowName = "Payment allocated",
        content = "£10.00",
        isLink = false,
        href = None
      ),
      PaymentAllocationDetailsViewModelRow(
        rowName = "Group tax total",
        content = "£10.00",
        isLink = true,
        href = None
      )
    ),
    allocRows = List(
      AllocationDetailsViewModelRow(
        paymentDate = "24 Jul 2024",
        allocatedPaymentAmount = "£10.00"
      )
    ),
    page = page,
    pagination = PaginationHelper
      .toPagination(page, _ => s"/ct-accounting/group-payment-arrangement/group-taxes/payment-allocated?page=1")(msgs),
    paginationInfo = Some("PaginationInfo")
  )

}
