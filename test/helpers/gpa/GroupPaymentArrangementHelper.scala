package helpers.gpa

import viewmodels.gpa.GroupPaymentArrangementViewModel

import java.time.LocalDate

trait GroupPaymentArrangementHelper {

  val accountingPeriodEndDate: LocalDate = LocalDate.of(2026, 1, 1)
  val taxReference: Long                 = 1L
  val referenceNumber                    = "933636936A00104A"
  val taxRef                             = 1L
  val accPeriod                          = 1L
  val groupPayments                      = -1536642.00
  val groupTaxes                         = 1536642.00
  val gpaStatus                          = "Open"

  val groupPaymentArrangementViewModel: GroupPaymentArrangementViewModel =
    GroupPaymentArrangementViewModel(
      referenceNumber = referenceNumber,
      periodOfAccountEnding = accountingPeriodEndDate,
      groupPayments = groupPayments,
      groupTaxes = groupTaxes,
      status = gpaStatus
    )

}
