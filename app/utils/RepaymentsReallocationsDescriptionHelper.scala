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
import models.RepayReallocationSummaryDetails
import views.ViewUtils.formatDate
import java.time.LocalDate

object RepaymentsReallocationsDescriptionHelper {

  def getDescription(repayReallocationDetails: RepayReallocationSummaryDetails)(implicit messages: Messages): String = {
    val formattedDate =
      formatDate(repayReallocationDetails.transactionDate.getOrElse(LocalDate.of(2026, 1, 1)), messages.lang)

    val messageName = repayReallocationDetails.`type` match {
      case Some("RTO") =>
        "repaymentReallocations.description.rto" // Reallocation To
      case Some("RFR") =>
        "repaymentReallocations.description.rfr" // Reallocation From
      case Some("CRT") =>
        "repaymentReallocations.description.crt" // Repayments
      case _           =>
        ""
    }

    messages(messageName, formattedDate, messages.lang)
  }
}
