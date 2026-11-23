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

package utils

import play.api.i18n.Messages
import utils.Constants.emptyString

object RepaymentsDescriptionHelper {

  def getRepaymentsDescription(repaymentType: String)(implicit messages: Messages): String =
    repaymentType match {
      case "B"       =>
        messages("repayments.description.B") // Repayment by BACS payment
      case "C"       =>
        messages("repayments.description.C") // Card repayment
      case "CRT"     =>
        messages("repayments.description.CRT") // Cancelled repayment
      case "MiscTFR" =>
        messages("repayments.description.MiscTFR") // Miscellaneous transfer
      case "P"       =>
        messages("repayments.description.P") // Repayment by payable order
      case "RFR"     =>
        messages("repayments.description.RFR") // Reallocation from AP ending
      case "RTO"     =>
        messages("repayments.description.RTO") // Reallocation to AP ending
      case _         =>
        emptyString
    }
}
