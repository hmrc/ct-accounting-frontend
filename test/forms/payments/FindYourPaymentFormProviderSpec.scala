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

package forms.payments

import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec
import play.api.i18n.{Lang, Messages}
import play.api.test.Helpers.stubMessagesApi

class FindYourPaymentFormProviderSpec extends AnyWordSpec with Matchers {

  implicit val messages: Messages = stubMessagesApi().preferred(Seq(Lang.defaultLang))

  private val formProvider = new FindYourPaymentFormProvider()

  private def formData(
    amount: String = "1570.67",
    startDay: String = "",
    startMonth: String = "",
    startYear: String = "",
    endDay: String = "",
    endMonth: String = "",
    endYear: String = ""
  ): Map[String, String]          =
    Map(
      "amount"          -> amount,
      "startDate.day"   -> startDay,
      "startDate.month" -> startMonth,
      "startDate.year"  -> startYear,
      "endDate.day"     -> endDay,
      "endDate.month"   -> endMonth,
      "endDate.year"    -> endYear
    )

  "FindYourPaymentFormProvider" should {

    "accept a valid amount with no dates" in {
      val result = formProvider.form.bind(formData())

      result.hasErrors mustBe false
    }

    "reject an empty amount" in {
      val result = formProvider.form.bind(formData(amount = ""))

      result.errors.map(_.message) must contain("findYourPayment.error.amountRequired")
    }

    "accept a whole number amount" in {
      val result = formProvider.form.bind(formData(amount = "1500"))

      result.hasErrors mustBe false
    }

    "reject an amount with one decimal place" in {
      val result = formProvider.form.bind(formData(amount = "1500.5"))

      result.errors.map(_.message) must contain("findYourPayment.error.amountFormat")
    }

    "reject an amount with three decimal places" in {
      val result = formProvider.form.bind(formData(amount = "1500.567"))

      result.errors.map(_.message) must contain("findYourPayment.error.amountFormat")
    }

    "reject an amount containing a currency symbol" in {
      val result = formProvider.form.bind(formData(amount = "£1500.00"))

      result.errors.map(_.message) must contain("findYourPayment.error.amountFormat")
    }

    "accept a valid start and end date when the end date is later" in {
      val result = formProvider.form.bind(
        formData(
          startDay = "1",
          startMonth = "3",
          startYear = "2025",
          endDay = "31",
          endMonth = "3",
          endYear = "2025"
        )
      )

      result.hasErrors mustBe false
    }

    "reject an end date earlier than the start date" in {
      val result = formProvider.form.bind(
        formData(
          startDay = "31",
          startMonth = "3",
          startYear = "2025",
          endDay = "1",
          endMonth = "3",
          endYear = "2025"
        )
      )

      result.errors.map(_.message) must contain("findYourPayment.error.dateRange")
    }

    "reject an end date equal to the start date" in {
      val result = formProvider.form.bind(
        formData(
          startDay = "1",
          startMonth = "3",
          startYear = "2025",
          endDay = "1",
          endMonth = "3",
          endYear = "2025"
        )
      )

      result.errors.map(_.message) must contain("findYourPayment.error.dateRange")
    }

    "reject an invalid calendar date" in {
      val result = formProvider.form.bind(
        formData(
          startDay = "31",
          startMonth = "2",
          startYear = "2025"
        )
      )

      result.errors.map(_.message) must contain("findYourPayment.error.invalidDate")
    }

    "reject a partially completed start date" in {
      val result = formProvider.form.bind(
        formData(
          startDay = "1",
          startMonth = "3"
        )
      )

      result.errors.map(_.message) must contain("findYourPayment.error.requiredDate")
    }

    "allow only a start date when it is valid" in {
      val result = formProvider.form.bind(
        formData(
          startDay = "1",
          startMonth = "3",
          startYear = "2025"
        )
      )

      result.hasErrors mustBe false
    }

    "allow only an end date when valid" in {
      val result = formProvider.form.bind(
        formData(
          endDay = "31",
          endMonth = "3",
          endYear = "2025"
        )
      )

      result.hasErrors mustBe false
    }
  }
}
