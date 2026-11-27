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

import base.SpecBase
import play.api.i18n.{Lang, Messages, MessagesApi, MessagesImpl}

class RepaymentsDescriptionHelperSpec extends SpecBase {

  val application = applicationBuilder().build()

  implicit val messagesApi: MessagesApi = application.injector.instanceOf[MessagesApi]
  implicit val messages: Messages       = MessagesImpl(Lang.defaultLang, messagesApi)

  "getRepaymentsDescription" - {

    "when repaymentType is B" in {
      val repaymentType = "B"

      val result: String = messages(RepaymentsDescriptionHelper.getRepaymentsDescription(repaymentType))

      val expectedResult = "Repayment by BACS payment"

      result mustBe expectedResult
    }

    "when repaymentType is C" in {
      val repaymentType = "C"

      val result: String = messages(RepaymentsDescriptionHelper.getRepaymentsDescription(repaymentType))

      val expectedResult = "Card repayment"

      result mustBe expectedResult
    }

    "when repaymentType is CRT" in {
      val repaymentType = "CRT"

      val result: String = messages(RepaymentsDescriptionHelper.getRepaymentsDescription(repaymentType))

      val expectedResult = "Cancelled repayment"

      result mustBe expectedResult
    }

    "when repaymentType is MiscTFR" in {
      val repaymentType = "MiscTFR"

      val result: String = messages(RepaymentsDescriptionHelper.getRepaymentsDescription(repaymentType))

      val expectedResult = "Miscellaneous transfer"

      result mustBe expectedResult
    }

    "when repaymentType is P" in {
      val repaymentType = "P"

      val result: String = messages(RepaymentsDescriptionHelper.getRepaymentsDescription(repaymentType))

      val expectedResult = "Repayment by payable order"

      result mustBe expectedResult
    }

    "when repaymentType is RFR" in {
      val repaymentType = "RFR"

      val result: String = messages(RepaymentsDescriptionHelper.getRepaymentsDescription(repaymentType))

      val expectedResult = "Reallocation from AP ending"

      result mustBe expectedResult
    }

    "when repaymentType is RTO" in {
      val repaymentType = "RTO"

      val result: String = messages(RepaymentsDescriptionHelper.getRepaymentsDescription(repaymentType))

      val expectedResult = "Reallocation to AP ending"

      result mustBe expectedResult
    }
  }
}
