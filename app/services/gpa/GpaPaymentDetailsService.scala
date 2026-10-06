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

package services.gpa

import models.gpa.GpaPayments
import play.api.Logging
import play.api.i18n.Messages
import utils.PaymentsDescriptionHelper

import java.time.LocalDate

class GpaPaymentDetailsService(
) extends Logging {

  def gpaPaymentDescription(gpaPayment: GpaPayments, utr: Long, accountingPeriodEndDate: LocalDate)(implicit
    messages: Messages
  ): String =
    gpaPayment.tablename match {
      case Some("RFRReallocation") | Some("RTOReallocation") if !gpaPayment.participatorPresent.getOrElse(false) =>
        messages("gpaPayment.Description.miscellaneous")
      case Some("RFRReallocation") | Some("RTOReallocation") if gpaPayment.targetApNo.getOrElse(0) == 0          =>
        messages("gpaPayment.Description.miscellaneous")
      case Some("RFRReallocation")                                                                               => rfrReallocationDescription(gpaPayment, utr, accountingPeriodEndDate)
      case Some("RTOReallocation")                                                                               => rtoReallocationDescription(gpaPayment, utr, accountingPeriodEndDate)
      case Some("Payslip")                                                                                       => PaymentsDescriptionHelper.getPaymentsDescription(gpaPayment.paymentType.getOrElse(""))
      case Some("Repayment") | Some("CancelledRepayment")                                                        =>
        "" // No mapping in ASIS for repayment/cancelled in gpa journey
      case _                                                                                                     => ""
    }

  private def rfrReallocationDescription(gpaPayment: GpaPayments, utr: Long, accountingPeriodEndDate: LocalDate)(
    implicit messages: Messages
  ): String =
    (gpaPayment.targetTaxpayerReference, gpaPayment.contractEndDate) match {
      case (Some(reallocationTaxRef), _) if reallocationTaxRef != utr.toString =>
        messages("gpaPayment.Description.rfr.taxRef", reallocationTaxRef, accountingPeriodEndDate)
      case (_, None)                                                           => messages("gpaPayment.Description.rfr.date", accountingPeriodEndDate)
      case (_, Some(endDate))                                                  => messages("gpaPayment.Description.rfr.date", endDate)
    }

  private def rtoReallocationDescription(gpaPayment: GpaPayments, utr: Long, accountingPeriodEndDate: LocalDate)(
    implicit messages: Messages
  ): String =
    (gpaPayment.targetTaxpayerReference, gpaPayment.contractEndDate) match {
      case (Some(reallocationTaxRef), _) if reallocationTaxRef != utr.toString =>
        messages("gpaPayment.Description.rto.taxRef", reallocationTaxRef, accountingPeriodEndDate)
      case (_, None)                                                           => messages("gpaPayment.Description.rto.date", accountingPeriodEndDate)
      case (_, Some(endDate))                                                  => messages("gpaPayment.Description.rto.date", endDate)
    }
}
