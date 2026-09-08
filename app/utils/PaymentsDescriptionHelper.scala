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

import models.PaymentTransaction
import play.api.i18n.Messages

trait PaymentsDescriptionHelper {

  def getPaymentsDescription(paymentTransactions: List[PaymentTransaction])(implicit messages: Messages): List[String] =
    paymentTransactions.map { pay =>
      pay.paymentType match {
        case "DSO" | "IRP"                 =>
          messages("payments.description.IRC") // HMRC Credit
        case "BLP" | "LOP" | "NGP" | "BGT" =>
          messages("payments.description.CP") // Cheque payment
        case "BAC" | "BGP" | "CHP"         =>
          messages("payments.description.EP") // Electronic payment
        case _                             =>
          ""
      }
    }
}
