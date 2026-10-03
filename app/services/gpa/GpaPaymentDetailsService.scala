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

import javax.inject.Inject

class GpaPaymentDetailsService @Inject()(
) extends Logging {

  def reallocationDescription(gpaPayment: GpaPayments, utr: Long, accountingPeriod: Long): String =
    gpaPayment.tablename match {
      case Some("RFRReallocation") | Some("RTOReallocation") if !gpaPayment.participatorPresent.getOrElse(false) => "Miscellaneous Transfer" // IS FALSE SAME AS NOT EXISTING?
      case Some("RFRReallocation") | Some("RTOReallocation") if gpaPayment.targetApNo.getOrElse("0") == 0.toString => "Miscellaneous Transfer" // WHAT DO YOU DO IF targetTaxpayerReference isn't there? // Should this apply to both RFR AND RTO?
      case Some("RFRReallocation") => rfrReallocationDescription(gpaPayment, utr, accountingPeriod)
      case Some("RTOReallocation") => rtoReallocationDescription(gpaPayment, utr, accountingPeriod)
      case _ => "DUMMY"
    }

  private def rfrReallocationDescription(gpaPayment: GpaPayments, utr: Long, accountingPeriod: Long): String =
    (gpaPayment.targetTaxpayerReference, gpaPayment.contractEndDate) match {
      case (Some(reallocationTaxRef), _) if reallocationTaxRef != utr.toString  => s"Reallocation TO ${reallocationTaxRef}, AP ending ${accountingPeriod}"
      case (_, None) => s"Reallocation TO AP ending ${accountingPeriod}"
      case (_, Some(endDate)) => s"Reallocation TO AP ending ${endDate}"
    }

  private def rtoReallocationDescription(gpaPayment: GpaPayments, utr: Long, accountingPeriod: Long): String =
    (gpaPayment.targetTaxpayerReference, gpaPayment.contractEndDate) match {
      case (Some(reallocationTaxRef), _) if reallocationTaxRef != utr.toString  => s"Reallocation FROM ${reallocationTaxRef}, AP ending ${accountingPeriod}"
      case (_, None) => s"Reallocation FROM AP ending ${accountingPeriod}"
      case (_, Some(endDate)) => s"Reallocation FROM AP ending ${endDate}"
    }
  }

