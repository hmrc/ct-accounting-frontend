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

import models.gpa.{GpaPaymentDetails, GpaPayments}
import play.api.i18n.Messages
import utils.Constants.emptyString
import viewmodels.gpa.{
  GpaDetailsGroupPaymentArrangementPageViewModel, GpaDetailsGroupPaymentArrangementViewModel, GroupPaymentRecord
}

import java.time.LocalDate

trait GpaDetailsGroupPaymentHelper {

  val responseRfrReallocation: GpaPaymentDetails       = GpaPaymentDetails(
    gpaPayments = List(
      GpaPayments(
        displayDate = Some(LocalDate.of(2025, 1, 1)),
        total = BigDecimal(17.01),
        tablename = Some("RFRReallocation"),
        paymentType = Some("RFRReallocation"),
        repaymentType = None,
        targetTaxpayerReference = None,
        targetApNo = Some(7),
        targetApEndDate = None,
        contractEndDate = None,
        participatorPresent = Some(false)
      )
    ),
    totalNumOfRecords = Some(1),
    gppEndDate = Some(LocalDate.of(2026, 1, 1)),
    gppTotalGroupPayment = BigDecimal(1125017),
    gppTotalGroupTax = BigDecimal(1125012),
    gppStatus = "S",
    gppCni = None,
    gppApportionmentMethod = Some("METHOD")
  )
  val responseRtoReallocation: GpaPaymentDetails       = GpaPaymentDetails(
    gpaPayments = List(
      GpaPayments(
        displayDate = Some(LocalDate.of(2025, 1, 1)),
        total = BigDecimal(17.01),
        tablename = Some("RTOReallocation"),
        paymentType = Some("RTOReallocation"),
        repaymentType = None,
        targetTaxpayerReference = Some("99"),
        targetApNo = Some(7),
        targetApEndDate = Some(LocalDate.of(2026, 1, 1)),
        contractEndDate = Some(LocalDate.of(2026, 2, 1)),
        participatorPresent = Some(true)
      )
    ),
    totalNumOfRecords = Some(1),
    gppEndDate = Some(LocalDate.of(2026, 1, 1)),
    gppTotalGroupPayment = BigDecimal(1125017),
    gppTotalGroupTax = BigDecimal(1125012),
    gppStatus = "C",
    gppCni = None,
    gppApportionmentMethod = Some("METHOD")
  )
  val responsePayslipReallocation: GpaPaymentDetails   = GpaPaymentDetails(
    gpaPayments = List(
      GpaPayments(
        displayDate = Some(LocalDate.of(2025, 1, 1)),
        total = BigDecimal(17.01),
        tablename = Some("Payslip"),
        paymentType = Some("IRP"),
        repaymentType = None,
        targetTaxpayerReference = None,
        targetApNo = Some(7),
        targetApEndDate = None,
        contractEndDate = None,
        participatorPresent = Some(true)
      )
    ),
    totalNumOfRecords = Some(1),
    gppEndDate = Some(LocalDate.of(2026, 1, 1)),
    gppTotalGroupPayment = BigDecimal(1125017),
    gppTotalGroupTax = BigDecimal(1125012),
    gppStatus = "L",
    gppCni = None,
    gppApportionmentMethod = Some("METHOD")
  )
  val responseRepaymentReallocation: GpaPaymentDetails = GpaPaymentDetails(
    gpaPayments = List(
      GpaPayments(
        displayDate = Some(LocalDate.of(2025, 1, 1)),
        total = BigDecimal(17.01),
        tablename = Some("Repayment"),
        paymentType = Some("Repayment"),
        repaymentType = None,
        targetTaxpayerReference = None,
        targetApNo = Some(7),
        targetApEndDate = None,
        contractEndDate = None,
        participatorPresent = Some(true)
      )
    ),
    totalNumOfRecords = Some(1),
    gppEndDate = Some(LocalDate.of(2026, 1, 1)),
    gppTotalGroupPayment = BigDecimal(1125017),
    gppTotalGroupTax = BigDecimal(1125012),
    gppStatus = "E",
    gppCni = None,
    gppApportionmentMethod = Some("METHOD")
  )

  def viewModelResponseRfrReallocation(implicit messages: Messages): GpaDetailsGroupPaymentArrangementViewModel     =
    GpaDetailsGroupPaymentArrangementViewModel(
      arrangementReference = "12",
      accountEnding = Some(LocalDate.of(2026, 1, 1)),
      accountStatus = messages("groupPaymentsArrangements.status.open"),
      paymentTotal = BigDecimal(1125017),
      taxTotal = BigDecimal(1125012),
      totalNumberOfRecords = 1,
      records = List(
        GroupPaymentRecord(
          date = Some(LocalDate.of(2025, 1, 1)),
          description = "Miscellaneous Transfer",
          amount = BigDecimal(17.01)
        )
      )
    )
  def viewModelResponseRfrReallocationWithTaxTotalZero(implicit
    messages: Messages
  ): GpaDetailsGroupPaymentArrangementViewModel =
    GpaDetailsGroupPaymentArrangementViewModel(
      arrangementReference = "12",
      accountEnding = Some(LocalDate.of(2026, 1, 1)),
      accountStatus = messages("groupPaymentsArrangements.status.open"),
      paymentTotal = BigDecimal(1125017),
      taxTotal = BigDecimal(0.00),
      totalNumberOfRecords = 1,
      records = List(
        GroupPaymentRecord(
          date = Some(LocalDate.of(2025, 1, 1)),
          description = "Miscellaneous Transfer",
          amount = BigDecimal(17.01)
        )
      )
    )
  def viewModelResponseRtoReallocation(implicit messages: Messages): GpaDetailsGroupPaymentArrangementViewModel     =
    GpaDetailsGroupPaymentArrangementViewModel(
      arrangementReference = "12",
      accountEnding = Some(LocalDate.of(2026, 1, 1)),
      accountStatus = messages("groupPaymentsArrangements.status.earlierCleared"),
      paymentTotal = BigDecimal(1125017),
      taxTotal = BigDecimal(1125012),
      totalNumberOfRecords = 1,
      records = List(
        GroupPaymentRecord(
          date = Some(LocalDate.of(2025, 1, 1)),
          description = "Reallocation FROM 99, AP ending 2026-01-01",
          amount = BigDecimal(17.01)
        )
      )
    )
  def viewModelResponsePayslipReallocation(implicit messages: Messages): GpaDetailsGroupPaymentArrangementViewModel =
    GpaDetailsGroupPaymentArrangementViewModel(
      arrangementReference = "12",
      accountEnding = Some(LocalDate.of(2026, 1, 1)),
      accountStatus = messages("groupPaymentsArrangements.status.cleared"),
      paymentTotal = BigDecimal(1125017),
      taxTotal = BigDecimal(1125012),
      totalNumberOfRecords = 1,
      records = List(
        GroupPaymentRecord(
          date = Some(LocalDate.of(2025, 1, 1)),
          description = "HMRC Credit",
          amount = BigDecimal(17.01)
        )
      )
    )
  def viewModelRepaymentReallocation(implicit messages: Messages): GpaDetailsGroupPaymentArrangementViewModel       =
    GpaDetailsGroupPaymentArrangementViewModel(
      arrangementReference = "12",
      accountEnding = Some(LocalDate.of(2026, 1, 1)),
      accountStatus = messages("groupPaymentsArrangements.status.earlierExempt"),
      paymentTotal = BigDecimal(1125017),
      taxTotal = BigDecimal(1125012),
      totalNumberOfRecords = 1,
      records = List(
        GroupPaymentRecord(
          date = Some(LocalDate.of(2025, 1, 1)),
          description = emptyString,
          amount = BigDecimal(17.01)
        )
      )
    )
  def viewModelWithMultipleRecords(implicit messages: Messages): GpaDetailsGroupPaymentArrangementViewModel         =
    GpaDetailsGroupPaymentArrangementViewModel(
      arrangementReference = "12",
      accountEnding = Some(LocalDate.of(2026, 1, 1)),
      accountStatus = messages("groupPaymentsArrangements.status.earlierExempt"),
      paymentTotal = BigDecimal(1125017),
      taxTotal = BigDecimal(1125012),
      totalNumberOfRecords = 12,
      records = List(
        GroupPaymentRecord(
          date = Some(LocalDate.of(2025, 1, 1)),
          description = emptyString,
          amount = BigDecimal(17.01)
        ),
        GroupPaymentRecord(
          date = Some(LocalDate.of(2026, 1, 1)),
          description = "HMRC Credit",
          amount = BigDecimal(180.01)
        ),
        GroupPaymentRecord(
          date = Some(LocalDate.of(2027, 1, 1)),
          description = "Reallocation FROM 99, AP ending 2026-01-01",
          amount = BigDecimal(20.01)
        ),
        GroupPaymentRecord(
          date = Some(LocalDate.of(2025, 1, 1)),
          description = "Miscellaneous Transfer",
          amount = BigDecimal(17.01)
        ),
        GroupPaymentRecord(
          date = Some(LocalDate.of(2025, 1, 1)),
          description = "Miscellaneous Transfer",
          amount = BigDecimal(17.01)
        ),
        GroupPaymentRecord(
          date = Some(LocalDate.of(2025, 1, 1)),
          description = "Miscellaneous Transfer",
          amount = BigDecimal(17.01)
        ),
        GroupPaymentRecord(
          date = Some(LocalDate.of(2025, 1, 1)),
          description = "Miscellaneous Transfer",
          amount = BigDecimal(17.01)
        ),
        GroupPaymentRecord(
          date = Some(LocalDate.of(2025, 1, 1)),
          description = "Miscellaneous Transfer",
          amount = BigDecimal(17.01)
        ),
        GroupPaymentRecord(
          date = Some(LocalDate.of(2025, 1, 1)),
          description = "Miscellaneous Transfer",
          amount = BigDecimal(17.01)
        ),
        GroupPaymentRecord(
          date = Some(LocalDate.of(2025, 1, 1)),
          description = "Miscellaneous Transfer",
          amount = BigDecimal(17.01)
        ),
        GroupPaymentRecord(
          date = Some(LocalDate.of(2025, 1, 1)),
          description = "Miscellaneous Transfer",
          amount = BigDecimal(17.01)
        ),
        GroupPaymentRecord(
          date = Some(LocalDate.of(2025, 1, 1)),
          description = "Miscellaneous Transfer",
          amount = BigDecimal(17.01)
        )
      )
    )

  def returnGroupPaymentPageViewModel(
    viewModel: GpaDetailsGroupPaymentArrangementViewModel,
    requestedPage: Option[Int],
    pageUrl: Int => String
  )(implicit messages: Messages): GpaDetailsGroupPaymentArrangementPageViewModel =
    GpaDetailsGroupPaymentArrangementPageViewModel.toPageViewModel(viewModel, requestedPage, pageUrl)

}
