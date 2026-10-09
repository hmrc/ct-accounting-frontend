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
import utils.Constants.{ZERO, emptyString}

import java.time.LocalDate

class GpaPaymentDetailsService(
) extends Logging {

  def gpaPaymentDescription(gpaPayment: GpaPayments, gpaUtr: Long, accountingPeriodEndDate: LocalDate)(implicit
    messages: Messages
  ): String =
    gpaPayment.tablename match {
      case Some("RFRReallocation")                        =>
        reallocationDescription(gpaPayment, gpaUtr, accountingPeriodEndDate, isRto = false)
      case Some("RTOReallocation")                        => reallocationDescription(gpaPayment, gpaUtr, accountingPeriodEndDate, isRto = true)
      case Some("Payslip")                                =>
        PaymentsDescriptionHelper.getPaymentsDescription(gpaPayment.paymentType.getOrElse(emptyString))
      case Some("Repayment") | Some("CancelledRepayment") =>
        emptyString // No mapping in ASIS for repayment/cancelled in gpa journey
      case _                                              => emptyString
    }

  private def reallocationDescription(
    gpaPayment: GpaPayments,
    gpaUtr: Long,
    accountingPeriodEndDate: LocalDate,
    isRto: Boolean
  )(implicit
    messages: Messages
  ): String = {
    val keyPrefix          = if (isRto) "gpaPayment.Description.rto" else "gpaPayment.Description.rfr"
    val miscellaneous      = messages("gpaPayment.Description.miscellaneous")
    val isRtoWithZeroAp    = isRto && gpaPayment.targetApNo.getOrElse(ZERO) == ZERO
    val participantPresent = gpaPayment.participatorPresent.getOrElse(false)

    gpaPayment.targetTaxpayerReference match {
      case Some(reallocationTaxRef) if reallocationTaxRef == gpaUtr.toString =>
        if (isRtoWithZeroAp)
          miscellaneous
        else
          messages(s"$keyPrefix.date", gpaPayment.contractEndDate.getOrElse(accountingPeriodEndDate))

      case Some(reallocationTaxRef) =>
        if (!participantPresent || isRtoWithZeroAp)
          miscellaneous
        else messages(s"$keyPrefix.taxRef", reallocationTaxRef, accountingPeriodEndDate)
      case None                     => miscellaneous
    }
  }

}
