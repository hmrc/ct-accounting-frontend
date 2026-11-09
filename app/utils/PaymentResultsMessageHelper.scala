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

class PaymentResultsMessageHelper {

  def getPaymentResultsMessage(
    listCount: Int,
    amount: String,
    fromDate: Option[String],
    toDate: Option[String]
  )(implicit messages: Messages): String = {

    val prefix = if (listCount == 1) "singleResult" else "multipleResults"

    (fromDate, toDate) match {
      case (Some(from), Some(to)) =>
        messages(s"paymentResults.${prefix}Para", listCount, amount, from, to)

      case (Some(from), None) =>
        messages(s"paymentResults.${prefix}FromDatePara", listCount, amount, from)

      case (None, Some(to)) =>
        messages(s"paymentResults.${prefix}ToDatePara", listCount, amount, to)

      case (None, None) =>
        messages(s"paymentResults.${prefix}NoDatesPara", listCount, amount)
    }
  }
}
