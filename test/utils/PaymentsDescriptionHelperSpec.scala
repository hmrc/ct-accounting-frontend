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

class PaymentsDescriptionHelperSpec extends SpecBase {

  val application = applicationBuilder().build()

  implicit val messagesApi: MessagesApi = application.injector.instanceOf[MessagesApi]
  implicit val messages: Messages       = MessagesImpl(Lang.defaultLang, messagesApi)

  s"Payment Type is an IRC payment, it should return correct Payments Description" - {

    "when paymentType is DSO" in {
      val paymentType = "DSO"

      val result: String = messages(PaymentsDescriptionHelper.getPaymentsDescription(paymentType))

      val expectedResult = "HMRC Credit"

      result mustBe expectedResult
    }

    "when paymentType is IRP" in {
      val paymentType = "IRP"

      val result: String = messages(PaymentsDescriptionHelper.getPaymentsDescription(paymentType))

      val expectedResult = "HMRC Credit"

      result mustBe expectedResult
    }
  }
  s"Payment Type is an IRC part payment, it should return correct Payments Description" - {

    "when paymentType is DSOPP" in {
      val paymentType = "DSOPP"

      val result: String = messages(PaymentsDescriptionHelper.getPaymentsDescription(paymentType))

      val expectedResult = "HMRC Credit part payment"

      result mustBe expectedResult
    }

    "when paymentType is IRPPP" in {
      val paymentType = "IRPPP"

      val result: String = messages(PaymentsDescriptionHelper.getPaymentsDescription(paymentType))

      val expectedResult = "HMRC Credit part payment"

      result mustBe expectedResult
    }
  }

  s"Payment Type is an CP payment, it should return correct Payments Description" - {

    "when paymentType is BLP" in {
      val paymentType = "BLP"

      val result: String = messages(PaymentsDescriptionHelper.getPaymentsDescription(paymentType))

      val expectedResult = "Cheque payment"

      result mustBe expectedResult
    }

    "when paymentType is LOP" in {
      val paymentType = "LOP"

      val result: String = messages(PaymentsDescriptionHelper.getPaymentsDescription(paymentType))

      val expectedResult = "Cheque payment"

      result mustBe expectedResult
    }

    "when paymentType is NGP" in {
      val paymentType = "NGP"

      val result: String = messages(PaymentsDescriptionHelper.getPaymentsDescription(paymentType))

      val expectedResult = "Cheque payment"

      result mustBe expectedResult
    }

    "when paymentType is BGT" in {
      val paymentType = "BGT"

      val result: String = messages(PaymentsDescriptionHelper.getPaymentsDescription(paymentType))

      val expectedResult = "Cheque payment"

      result mustBe expectedResult
    }
  }

  s"Payment Type is an CP part payment, it should return correct Payments Description" - {

    "when paymentType is BLPPP" in {
      val paymentType = "BLPPP"

      val result: String = messages(PaymentsDescriptionHelper.getPaymentsDescription(paymentType))

      val expectedResult = "Cheque part payment"

      result mustBe expectedResult
    }

    "when paymentType is LOPPP" in {
      val paymentType = "LOPPP"

      val result: String = messages(PaymentsDescriptionHelper.getPaymentsDescription(paymentType))

      val expectedResult = "Cheque part payment"

      result mustBe expectedResult
    }

    "when paymentType is NGPPP" in {
      val paymentType = "NGPPP"

      val result: String = messages(PaymentsDescriptionHelper.getPaymentsDescription(paymentType))

      val expectedResult = "Cheque part payment"

      result mustBe expectedResult
    }

    "when paymentType is BGTPP" in {
      val paymentType = "BGTPP"

      val result: String = messages(PaymentsDescriptionHelper.getPaymentsDescription(paymentType))

      val expectedResult = "Cheque part payment"

      result mustBe expectedResult
    }
  }

  s"Payment Type is an EP payment, it should return correct Payments Description" - {

    "when paymentType is BAC" in {
      val paymentType = "BAC"

      val result: String = messages(PaymentsDescriptionHelper.getPaymentsDescription(paymentType))

      val expectedResult = "Electronic payment"

      result mustBe expectedResult
    }

    "when paymentType is BGP" in {
      val paymentType = "BGP"

      val result: String = messages(PaymentsDescriptionHelper.getPaymentsDescription(paymentType))

      val expectedResult = "Electronic payment"

      result mustBe expectedResult
    }

    "when paymentType is CHP" in {
      val paymentType = "CHP"

      val result: String = messages(PaymentsDescriptionHelper.getPaymentsDescription(paymentType))

      val expectedResult = "Electronic payment"

      result mustBe expectedResult
    }
  }

  s"Payment Type is an EP part payment, it should return correct Payments Description" - {

    "when paymentType is BACPP" in {
      val paymentType = "BACPP"

      val result: String = messages(PaymentsDescriptionHelper.getPaymentsDescription(paymentType))

      val expectedResult = "Electronic part payment"

      result mustBe expectedResult
    }

    "when paymentType is BGPPP" in {
      val paymentType = "BGPPP"

      val result: String = messages(PaymentsDescriptionHelper.getPaymentsDescription(paymentType))

      val expectedResult = "Electronic part payment"

      result mustBe expectedResult
    }

    "when paymentType is CHPPP" in {
      val paymentType = "CHPPP"

      val result: String = messages(PaymentsDescriptionHelper.getPaymentsDescription(paymentType))

      val expectedResult = "Electronic part payment"

      result mustBe expectedResult
    }
  }
}
