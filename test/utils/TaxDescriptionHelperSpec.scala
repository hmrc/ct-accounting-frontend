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

import org.scalatest.concurrent.ScalaFutures
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec
import play.api.i18n.{DefaultMessagesApi, Lang, Messages}

class TaxDescriptionHelperSpec extends AnyWordSpec with Matchers with ScalaFutures {

  implicit val messages: Messages = new DefaultMessagesApi(
    Map(
      "en" -> Map(
        "taxDescription.assessment.a.standard" -> "Amended assessment",
        "taxDescription.assessment.a.claim"    -> "Amended assessment (claim)",
        "taxDescription.assessment.d.standard" -> "Discovery assessment",
        "taxDescription.assessment.d.claim"    -> "Discovery assessment (claim)",
        "taxDescription.assessment.e.standard" -> "HMRC determination",
        "taxDescription.assessment.e.claim"    -> "HMRC determination (claim)",
        "taxDescription.assessment.f.standard" -> "Further assessment",
        "taxDescription.assessment.f.claim"    -> "Further assessment (claim)",
        "taxDescription.assessment.j.standard" -> "HMRC amended self assessment",
        "taxDescription.assessment.j.claim"    -> "HMRC amended self assessment (claim)",
        "taxDescription.assessment.m.standard" -> "Main assessment",
        "taxDescription.assessment.m.claim"    -> "Main assessment (claim)",
        "taxDescription.assessment.r.standard" -> "HMRC amended self assessment",
        "taxDescription.assessment.r.claim"    -> "HMRC amended self assessment (claim)",
        "taxDescription.assessment.s.standard" -> "Self assessment",
        "taxDescription.assessment.s.claim"    -> "Self assessment (claim)",
        "taxDescription.assessment.t.standard" -> "Taxpayer amended self assessment",
        "taxDescription.assessment.t.claim"    -> "Taxpayer amended self assessment (claim)",
        "taxDescription.assessment.z.standard" -> "Return charge",
        "taxDescription.assessment.z.claim"    -> "Return charge (claim)"
      )
    )
  ).preferred(Seq(Lang("en")))

  s"Assessment Type is A, it should return correct Tax Description" should {
    val assessmentType = "A"

    "when Correction Claim Indicator is null" in {
      val result: String = messages(TaxDescriptionHelper.getTaxDescription(assessmentType, null))

      val messageName    = s"taxDescription.assessment.${assessmentType.toLowerCase()}.standard"
      val expectedResult = messages(messageName)

      result mustBe expectedResult
    }

    "when Correction Claim Indicator is 0" in {
      val result: String = messages(TaxDescriptionHelper.getTaxDescription(assessmentType, Some("0")))

      val messageName    = s"taxDescription.assessment.${assessmentType.toLowerCase()}.standard"
      val expectedResult = messages(messageName)

      result mustBe expectedResult
    }

    "when Correction Claim Indicator is 2" in {
      val result: String = messages(TaxDescriptionHelper.getTaxDescription(assessmentType, Some("2")))

      val messageName    = s"taxDescription.assessment.${assessmentType.toLowerCase()}.claim"
      val expectedResult = messages(messageName)

      result mustBe expectedResult
    }
  }

  s"Assessment Type is D, it should return correct Tax Description" should {
    val assessmentType = "D"

    "when Correction Claim Indicator is null" in {
      val result: String = messages(TaxDescriptionHelper.getTaxDescription(assessmentType, null))

      val messageName    = s"taxDescription.assessment.${assessmentType.toLowerCase()}.standard"
      val expectedResult = messages(messageName)

      result mustBe expectedResult
    }

    "when Correction Claim Indicator is 0" in {
      val result: String = messages(TaxDescriptionHelper.getTaxDescription(assessmentType, Some("0")))

      val messageName    = s"taxDescription.assessment.${assessmentType.toLowerCase()}.standard"
      val expectedResult = messages(messageName)

      result mustBe expectedResult
    }

    "when Correction Claim Indicator is 2" in {
      val result: String = messages(TaxDescriptionHelper.getTaxDescription(assessmentType, Some("2")))

      val messageName    = s"taxDescription.assessment.${assessmentType.toLowerCase()}.claim"
      val expectedResult = messages(messageName)

      result mustBe expectedResult
    }
  }

  s"Assessment Type is E, it should return correct Tax Description" should {
    val assessmentType = "E"

    "when Correction Claim Indicator is null" in {
      val result: String = messages(TaxDescriptionHelper.getTaxDescription(assessmentType, null))

      val messageName    = s"taxDescription.assessment.${assessmentType.toLowerCase()}.standard"
      val expectedResult = messages(messageName)

      result mustBe expectedResult
    }

    "when Correction Claim Indicator is 0" in {
      val result: String = messages(TaxDescriptionHelper.getTaxDescription(assessmentType, Some("0")))

      val messageName    = s"taxDescription.assessment.${assessmentType.toLowerCase()}.standard"
      val expectedResult = messages(messageName)

      result mustBe expectedResult
    }

    "when Correction Claim Indicator is 2" in {
      val result: String = messages(TaxDescriptionHelper.getTaxDescription(assessmentType, Some("2")))

      val messageName    = s"taxDescription.assessment.${assessmentType.toLowerCase()}.claim"
      val expectedResult = messages(messageName)

      result mustBe expectedResult
    }
  }

  s"Assessment Type is F, it should return correct Tax Description" should {
    val assessmentType = "F"

    "when Correction Claim Indicator is null" in {
      val result: String = messages(TaxDescriptionHelper.getTaxDescription(assessmentType, null))

      val messageName    = s"taxDescription.assessment.${assessmentType.toLowerCase()}.standard"
      val expectedResult = messages(messageName)

      result mustBe expectedResult
    }

    "when Correction Claim Indicator is 0" in {
      val result: String = messages(TaxDescriptionHelper.getTaxDescription(assessmentType, Some("0")))

      val messageName    = s"taxDescription.assessment.${assessmentType.toLowerCase()}.standard"
      val expectedResult = messages(messageName)

      result mustBe expectedResult
    }

    "when Correction Claim Indicator is 2" in {
      val result: String = messages(TaxDescriptionHelper.getTaxDescription(assessmentType, Some("2")))

      val messageName    = s"taxDescription.assessment.${assessmentType.toLowerCase()}.claim"
      val expectedResult = messages(messageName)

      result mustBe expectedResult
    }
  }

  s"Assessment Type is J, it should return correct Tax Description" should {
    val assessmentType = "J"

    "when Correction Claim Indicator is null" in {
      val result: String = messages(TaxDescriptionHelper.getTaxDescription(assessmentType, null))

      val messageName    = s"taxDescription.assessment.${assessmentType.toLowerCase()}.standard"
      val expectedResult = messages(messageName)

      result mustBe expectedResult
    }

    "when Correction Claim Indicator is 0" in {
      val result: String = messages(TaxDescriptionHelper.getTaxDescription(assessmentType, Some("0")))

      val messageName    = s"taxDescription.assessment.${assessmentType.toLowerCase()}.standard"
      val expectedResult = messages(messageName)

      result mustBe expectedResult
    }

    "when Correction Claim Indicator is 2" in {
      val result: String = messages(TaxDescriptionHelper.getTaxDescription(assessmentType, Some("2")))

      val messageName    = s"taxDescription.assessment.${assessmentType.toLowerCase()}.claim"
      val expectedResult = messages(messageName)

      result mustBe expectedResult
    }
  }

  s"Assessment Type is M, it should return correct Tax Description" should {
    val assessmentType = "M"

    "when Correction Claim Indicator is null" in {
      val result: String = messages(TaxDescriptionHelper.getTaxDescription(assessmentType, null))

      val messageName    = s"taxDescription.assessment.${assessmentType.toLowerCase()}.standard"
      val expectedResult = messages(messageName)

      result mustBe expectedResult
    }

    "when Correction Claim Indicator is 0" in {
      val result: String = messages(TaxDescriptionHelper.getTaxDescription(assessmentType, Some("0")))

      val messageName    = s"taxDescription.assessment.${assessmentType.toLowerCase()}.standard"
      val expectedResult = messages(messageName)

      result mustBe expectedResult
    }

    "when Correction Claim Indicator is 2" in {
      val result: String = messages(TaxDescriptionHelper.getTaxDescription(assessmentType, Some("2")))

      val messageName    = s"taxDescription.assessment.${assessmentType.toLowerCase()}.claim"
      val expectedResult = messages(messageName)

      result mustBe expectedResult
    }
  }

  s"Assessment Type is R, it should return correct Tax Description" should {
    val assessmentType = "M"

    "when Correction Claim Indicator is null" in {
      val result: String = messages(TaxDescriptionHelper.getTaxDescription(assessmentType, null))

      val messageName    = s"taxDescription.assessment.${assessmentType.toLowerCase()}.standard"
      val expectedResult = messages(messageName)

      result mustBe expectedResult
    }

    "when Correction Claim Indicator is 0" in {
      val result: String = messages(TaxDescriptionHelper.getTaxDescription(assessmentType, Some("0")))

      val messageName    = s"taxDescription.assessment.${assessmentType.toLowerCase()}.standard"
      val expectedResult = messages(messageName)

      result mustBe expectedResult
    }

    "when Correction Claim Indicator is 2" in {
      val result: String = messages(TaxDescriptionHelper.getTaxDescription(assessmentType, Some("2")))

      val messageName    = s"taxDescription.assessment.${assessmentType.toLowerCase()}.claim"
      val expectedResult = messages(messageName)

      result mustBe expectedResult
    }
  }

  s"Assessment Type is S, it should return correct Tax Description" should {
    val assessmentType = "S"

    "when Correction Claim Indicator is null" in {
      val result: String = messages(TaxDescriptionHelper.getTaxDescription(assessmentType, null))

      val messageName    = s"taxDescription.assessment.${assessmentType.toLowerCase()}.standard"
      val expectedResult = messages(messageName)

      result mustBe expectedResult
    }

    "when Correction Claim Indicator is 0" in {
      val result: String = messages(TaxDescriptionHelper.getTaxDescription(assessmentType, Some("0")))

      val messageName    = s"taxDescription.assessment.${assessmentType.toLowerCase()}.standard"
      val expectedResult = messages(messageName)

      result mustBe expectedResult
    }

    "when Correction Claim Indicator is 2" in {
      val result: String = messages(TaxDescriptionHelper.getTaxDescription(assessmentType, Some("2")))

      val messageName    = s"taxDescription.assessment.${assessmentType.toLowerCase()}.claim"
      val expectedResult = messages(messageName)

      result mustBe expectedResult
    }
  }

  s"Assessment Type is T, it should return correct Tax Description" should {
    val assessmentType = "T"

    "when Correction Claim Indicator is null" in {
      val result: String = messages(TaxDescriptionHelper.getTaxDescription(assessmentType, null))

      val messageName    = s"taxDescription.assessment.${assessmentType.toLowerCase()}.standard"
      val expectedResult = messages(messageName)

      result mustBe expectedResult
    }

    "when Correction Claim Indicator is 0" in {
      val result: String = messages(TaxDescriptionHelper.getTaxDescription(assessmentType, Some("0")))

      val messageName    = s"taxDescription.assessment.${assessmentType.toLowerCase()}.standard"
      val expectedResult = messages(messageName)

      result mustBe expectedResult
    }

    "when Correction Claim Indicator is 2" in {
      val result: String = messages(TaxDescriptionHelper.getTaxDescription(assessmentType, Some("2")))

      val messageName    = s"taxDescription.assessment.${assessmentType.toLowerCase()}.claim"
      val expectedResult = messages(messageName)

      result mustBe expectedResult
    }
  }

  s"Assessment Type is Z, it should return correct Tax Description" should {
    val assessmentType = "Z"

    "when Correction Claim Indicator is null" in {
      val result: String = messages(TaxDescriptionHelper.getTaxDescription(assessmentType, null))

      val messageName    = s"taxDescription.assessment.${assessmentType.toLowerCase()}.standard"
      val expectedResult = messages(messageName)

      result mustBe expectedResult
    }

    "when Correction Claim Indicator is 0" in {
      val result: String = messages(TaxDescriptionHelper.getTaxDescription(assessmentType, Some("0")))

      val messageName    = s"taxDescription.assessment.${assessmentType.toLowerCase()}.standard"
      val expectedResult = messages(messageName)

      result mustBe expectedResult
    }

    "when Correction Claim Indicator is 2" in {
      val result: String = messages(TaxDescriptionHelper.getTaxDescription(assessmentType, Some("2")))

      val messageName    = s"taxDescription.assessment.${assessmentType.toLowerCase()}.claim"
      val expectedResult = messages(messageName)

      result mustBe expectedResult
    }
  }
}
