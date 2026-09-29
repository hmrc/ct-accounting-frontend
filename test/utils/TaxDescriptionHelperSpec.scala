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

class TaxDescriptionHelperSpec extends SpecBase {

  val application = applicationBuilder().build()


  implicit val messagesApi: MessagesApi = application.injector.instanceOf[MessagesApi]
  implicit val messages: Messages = MessagesImpl(Lang.defaultLang, messagesApi)


  s"Assessment Type is A, it should return correct Tax Description" - {
    val assessmentType = "A"

    "when Correction Claim Indicator is null" in {
      val result: String = messages(TaxDescriptionHelper.getTaxDescription(assessmentType, null))

      val messageName    = s"taxDescription.assessment.${assessmentType.toLowerCase()}"
      val expectedResult = messages(messageName, "")

      result mustBe expectedResult
    }

    "when Correction Claim Indicator is 0" in {
      val result: String = messages(TaxDescriptionHelper.getTaxDescription(assessmentType, Some("0")))

      val messageName    = s"taxDescription.assessment.${assessmentType.toLowerCase()}"
      val expectedResult = messages(messageName, "")

      result mustBe expectedResult
    }

    "when Correction Claim Indicator is 2" in {
      val result: String = messages(TaxDescriptionHelper.getTaxDescription(assessmentType, Some("2")))

      val messageName    = s"taxDescription.assessment.${assessmentType.toLowerCase()}"
      val expectedResult = messages(messageName, "(claim)")

      result mustBe expectedResult
    }
  }

  s"Assessment Type is D, it should return correct Tax Description" - {
    val assessmentType = "D"

    "when Correction Claim Indicator is null" in {
      val result: String = messages(TaxDescriptionHelper.getTaxDescription(assessmentType, null))

      val messageName    = s"taxDescription.assessment.${assessmentType.toLowerCase()}"
      val expectedResult = messages(messageName, "")

      result mustBe expectedResult
    }

    "when Correction Claim Indicator is 0" in {
      val result: String = messages(TaxDescriptionHelper.getTaxDescription(assessmentType, Some("0")))

      val messageName    = s"taxDescription.assessment.${assessmentType.toLowerCase()}"
      val expectedResult = messages(messageName, "")

      result mustBe expectedResult
    }

    "when Correction Claim Indicator is 2" in {
      val result: String = messages(TaxDescriptionHelper.getTaxDescription(assessmentType, Some("2")))

      val messageName    = s"taxDescription.assessment.${assessmentType.toLowerCase()}"
      val expectedResult = messages(messageName, "(claim)")

      result mustBe expectedResult
    }
  }

  s"Assessment Type is E, it should return correct Tax Description" - {
    val assessmentType = "E"

    "when Correction Claim Indicator is null" in {
      val result: String = messages(TaxDescriptionHelper.getTaxDescription(assessmentType, null))

      val messageName    = s"taxDescription.assessment.${assessmentType.toLowerCase()}"
      val expectedResult = messages(messageName, "")

      result mustBe expectedResult
    }

    "when Correction Claim Indicator is 0" in {
      val result: String = messages(TaxDescriptionHelper.getTaxDescription(assessmentType, Some("0")))

      val messageName    = s"taxDescription.assessment.${assessmentType.toLowerCase()}"
      val expectedResult = messages(messageName, "")

      result mustBe expectedResult
    }

    "when Correction Claim Indicator is 2" in {
      val result: String = messages(TaxDescriptionHelper.getTaxDescription(assessmentType, Some("2")))

      val messageName    = s"taxDescription.assessment.${assessmentType.toLowerCase()}"
      val expectedResult = messages(messageName, "(claim)")

      result mustBe expectedResult
    }
  }

  s"Assessment Type is F, it should return correct Tax Description" - {
    val assessmentType = "F"

    "when Correction Claim Indicator is null" in {
      val result: String = messages(TaxDescriptionHelper.getTaxDescription(assessmentType, null))

      val messageName    = s"taxDescription.assessment.${assessmentType.toLowerCase()}"
      val expectedResult = messages(messageName, "")

      result mustBe expectedResult
    }

    "when Correction Claim Indicator is 0" in {
      val result: String = messages(TaxDescriptionHelper.getTaxDescription(assessmentType, Some("0")))

      val messageName    = s"taxDescription.assessment.${assessmentType.toLowerCase()}"
      val expectedResult = messages(messageName, "")

      result mustBe expectedResult
    }

    "when Correction Claim Indicator is 2" in {
      val result: String = messages(TaxDescriptionHelper.getTaxDescription(assessmentType, Some("2")))

      val messageName    = s"taxDescription.assessment.${assessmentType.toLowerCase()}"
      val expectedResult = messages(messageName, "(claim)")

      result mustBe expectedResult
    }
  }

  s"Assessment Type is J, it should return correct Tax Description" - {
    val assessmentType = "J"

    "when Correction Claim Indicator is null" in {
      val result: String = messages(TaxDescriptionHelper.getTaxDescription(assessmentType, null))

      val messageName    = s"taxDescription.assessment.${assessmentType.toLowerCase()}"
      val expectedResult = messages(messageName, "")

      result mustBe expectedResult
    }

    "when Correction Claim Indicator is 0" in {
      val result: String = messages(TaxDescriptionHelper.getTaxDescription(assessmentType, Some("0")))

      val messageName    = s"taxDescription.assessment.${assessmentType.toLowerCase()}"
      val expectedResult = messages(messageName, "")

      result mustBe expectedResult
    }

    "when Correction Claim Indicator is 2" in {
      val result: String = messages(TaxDescriptionHelper.getTaxDescription(assessmentType, Some("2")))

      val messageName    = s"taxDescription.assessment.${assessmentType.toLowerCase()}"
      val expectedResult = messages(messageName, "(claim)")

      result mustBe expectedResult
    }
  }

  s"Assessment Type is M, it should return correct Tax Description" - {
    val assessmentType = "M"

    "when Correction Claim Indicator is null" in {
      val result: String = messages(TaxDescriptionHelper.getTaxDescription(assessmentType, null))

      val messageName    = s"taxDescription.assessment.${assessmentType.toLowerCase()}"
      val expectedResult = messages(messageName, "")

      result mustBe expectedResult
    }

    "when Correction Claim Indicator is 0" in {
      val result: String = messages(TaxDescriptionHelper.getTaxDescription(assessmentType, Some("0")))

      val messageName    = s"taxDescription.assessment.${assessmentType.toLowerCase()}"
      val expectedResult = messages(messageName, "")

      result mustBe expectedResult
    }

    "when Correction Claim Indicator is 2" in {
      val result: String = messages(TaxDescriptionHelper.getTaxDescription(assessmentType, Some("2")))

      val messageName    = s"taxDescription.assessment.${assessmentType.toLowerCase()}"
      val expectedResult = messages(messageName, "(claim)")

      result mustBe expectedResult
    }
  }

  s"Assessment Type is R, it should return correct Tax Description" - {
    val assessmentType = "M"

    "when Correction Claim Indicator is null" in {
      val result: String = messages(TaxDescriptionHelper.getTaxDescription(assessmentType, null))

      val messageName    = s"taxDescription.assessment.${assessmentType.toLowerCase()}"
      val expectedResult = messages(messageName, "")

      result mustBe expectedResult
    }

    "when Correction Claim Indicator is 0" in {
      val result: String = messages(TaxDescriptionHelper.getTaxDescription(assessmentType, Some("0")))

      val messageName    = s"taxDescription.assessment.${assessmentType.toLowerCase()}"
      val expectedResult = messages(messageName, "")

      result mustBe expectedResult
    }

    "when Correction Claim Indicator is 2" in {
      val result: String = messages(TaxDescriptionHelper.getTaxDescription(assessmentType, Some("2")))

      val messageName    = s"taxDescription.assessment.${assessmentType.toLowerCase()}"
      val expectedResult = messages(messageName, "(claim)")

      result mustBe expectedResult
    }
  }

  s"Assessment Type is S, it should return correct Tax Description" - {
    val assessmentType = "S"

    "when Correction Claim Indicator is null" in {
      val result: String = messages(TaxDescriptionHelper.getTaxDescription(assessmentType, null))

      val messageName    = s"taxDescription.assessment.${assessmentType.toLowerCase()}"
      val expectedResult = messages(messageName, "")

      result mustBe expectedResult
    }

    "when Correction Claim Indicator is 0" in {
      val result: String = messages(TaxDescriptionHelper.getTaxDescription(assessmentType, Some("0")))

      val messageName    = s"taxDescription.assessment.${assessmentType.toLowerCase()}"
      val expectedResult = messages(messageName, "")

      result mustBe expectedResult
    }

    "when Correction Claim Indicator is 2" in {
      val result: String = messages(TaxDescriptionHelper.getTaxDescription(assessmentType, Some("2")))

      val messageName    = s"taxDescription.assessment.${assessmentType.toLowerCase()}"
      val expectedResult = messages(messageName, "(claim)")

      result mustBe expectedResult
    }
  }

  s"Assessment Type is T, it should return correct Tax Description" - {
    val assessmentType = "T"

    "when Correction Claim Indicator is null" in {
      val result: String = messages(TaxDescriptionHelper.getTaxDescription(assessmentType, null))

      val messageName    = s"taxDescription.assessment.${assessmentType.toLowerCase()}"
      val expectedResult = messages(messageName, "")

      result mustBe expectedResult
    }

    "when Correction Claim Indicator is 0" in {
      val result: String = messages(TaxDescriptionHelper.getTaxDescription(assessmentType, Some("0")))

      val messageName    = s"taxDescription.assessment.${assessmentType.toLowerCase()}"
      val expectedResult = messages(messageName, "")

      result mustBe expectedResult
    }

    "when Correction Claim Indicator is 2" in {
      val result: String = messages(TaxDescriptionHelper.getTaxDescription(assessmentType, Some("2")))

      val messageName    = s"taxDescription.assessment.${assessmentType.toLowerCase()}"
      val expectedResult = messages(messageName, "(claim)")

      result mustBe expectedResult
    }
  }

  s"Assessment Type is Z, it should return correct Tax Description" - {
    val assessmentType = "Z"

    "when Correction Claim Indicator is null" in {
      val result: String = messages(TaxDescriptionHelper.getTaxDescription(assessmentType, null))

      val messageName    = s"taxDescription.assessment.${assessmentType.toLowerCase()}"
      val expectedResult = messages(messageName, "")

      result mustBe expectedResult
    }

    "when Correction Claim Indicator is 0" in {
      val result: String = messages(TaxDescriptionHelper.getTaxDescription(assessmentType, Some("0")))

      val messageName    = s"taxDescription.assessment.${assessmentType.toLowerCase()}"
      val expectedResult = messages(messageName, "")

      result mustBe expectedResult
    }

    "when Correction Claim Indicator is 2" in {
      val result: String = messages(TaxDescriptionHelper.getTaxDescription(assessmentType, Some("2")))

      val messageName    = s"taxDescription.assessment.${assessmentType.toLowerCase()}"
      val expectedResult = messages(messageName, "(claim)")

      result mustBe expectedResult
    }
  }
}
