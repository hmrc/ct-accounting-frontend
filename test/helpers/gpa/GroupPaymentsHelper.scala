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

import models.gpa.{
  GpaPaymentDetails, GpaPayments, GroupReferenceNumberLstItem, GroupSummaryDetailsRecord, GroupSummaryDetailsResponse
}

import java.time.LocalDate

trait GroupPaymentsHelper {

  val groupSummaryDetRecOne = GroupSummaryDetailsRecord(
    contractEndDate = LocalDate.of(2026, 1, 7),
    groupTaxCharge = BigDecimal(-11.01),
    groupPayment = BigDecimal(-13.02),
    groupPaymentRecordCount = 2,
    contractStatus = "ACTIVE",
    contractVersion = 2
  )

  val groupSummaryDetRecTwo = GroupSummaryDetailsRecord(
    contractEndDate = LocalDate.of(2025, 1, 4),
    groupTaxCharge = BigDecimal(-532.86),
    groupPayment = BigDecimal(742.16),
    groupPaymentRecordCount = 2,
    contractStatus = "CLOSED",
    contractVersion = 1
  )

  val groupSummaryDetRecThree = GroupSummaryDetailsRecord(
    contractEndDate = LocalDate.of(2024, 1, 9),
    groupTaxCharge = BigDecimal(-384.09),
    groupPayment = BigDecimal(-217.62),
    groupPaymentRecordCount = 2,
    contractStatus = "CLOSED",
    contractVersion = 1
  )

  val groupSummaryDetRecFour = GroupSummaryDetailsRecord(
    contractEndDate = LocalDate.of(2023, 1, 7),
    groupTaxCharge = BigDecimal(832.52),
    groupPayment = BigDecimal(12.73),
    groupPaymentRecordCount = 2,
    contractStatus = "CLOSED",
    contractVersion = 1
  )

  val groupSummaryDetRecFive = GroupSummaryDetailsRecord(
    contractEndDate = LocalDate.of(2022, 1, 12),
    groupTaxCharge = BigDecimal(743.32),
    groupPayment = BigDecimal(-431.56),
    groupPaymentRecordCount = 2,
    contractStatus = "CLOSED",
    contractVersion = 1
  )

  val groupPaymentDetailsResponse = GroupSummaryDetailsResponse(
    gpaGrpSummaryDetails = List(
      groupSummaryDetRecOne,
      groupSummaryDetRecTwo,
      groupSummaryDetRecThree,
      groupSummaryDetRecFour,
      groupSummaryDetRecFive
    ),
    gpaReferenceNumberLst = List(
      GroupReferenceNumberLstItem(112)
    ),
    nominatedCompanyName = "Some company name"
  )

  // PaymentDetails
  val defaultPaymentDetails = GpaPaymentDetails(
    gpaPayments = List(
      GpaPayments(
        displayDate = Some(LocalDate.of(2008, 4, 14)),
        total = BigDecimal(6250),
        tablename = Some("Payslip"),
        targetTaxpayerReference = None,
        paymentType = Some("BGP"),
        repaymentType = None,
        targetApNo = Some(0),
        targetApEndDate = None,
        contractEndDate = None,
        participatorPresent = Some(false)
      )
    ),
    totalNumOfRecords = Some(1L),
    gppEndDate = Some(LocalDate.of(2007, 12, 31)),
    gppTotalGroupPayment = BigDecimal(25000),
    gppTotalGroupTax = BigDecimal(-250000),
    gppStatus = "L",
    gppCni = None,
    gppApportionmentMethod = Some("METHOD")
  )

  val defaultPaymentDetailsResponse = GpaPaymentDetails(
    gpaPayments = List(
      GpaPayments(
        displayDate = Some(LocalDate.of(2008, 4, 14)),
        total = BigDecimal(-6250),
        tablename = Some("Payslip"),
        targetTaxpayerReference = None,
        paymentType = Some("BGP"),
        repaymentType = None,
        targetApNo = Some(0),
        targetApEndDate = None,
        contractEndDate = None,
        participatorPresent = Some(false)
      )
    ),
    totalNumOfRecords = Some(1L),
    gppEndDate = Some(LocalDate.of(2007, 12, 31)),
    gppTotalGroupPayment = BigDecimal(-25000),
    gppTotalGroupTax = BigDecimal(250000),
    gppStatus = "L",
    gppCni = None,
    gppApportionmentMethod = Some("METHOD")
  )
}
