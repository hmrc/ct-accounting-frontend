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

package views

import base.SpecBase
import models.PaymentTransaction
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import play.api.test.FakeRequest
import views.html.PaymentsView

import java.time.LocalDate

class PaymentsViewSpec extends SpecBase {
  val application = applicationBuilder().build()

  val view: PaymentsView = application.injector.instanceOf[PaymentsView]

  implicit val request: FakeRequest[_] = FakeRequest()

  val accountPeriod: LocalDate = LocalDate.of(2026, 1, 1)

  val total: BigDecimal = 10000.12

  val paymentTransactions: List[PaymentTransaction] = List(
    PaymentTransaction(
      amount = 1234.56,
      paymentType = "CP",
      effectiveDateOfPayment = LocalDate.of(2026, 1, 15)
    )
  )

  def render(items: List[PaymentTransaction] = paymentTransactions): Document =
    Jsoup.parse(view(items, accountPeriod, total)(request, messages(application)).toString)

  // TODO: Extra tests covering all content
  "PaymentsView" - {

    "render the correct page title" in {
      val doc = render()
      doc.title() mustBe "Payments - Accounting period overview - GOV.UK"
    }

    "render the correct heading" in {
      val doc = render()
      doc.select("h1.govuk-heading-xl").text() mustBe "Payments"
    }

    "render the table caption with the formatted account period" in {
      val doc = render()
      doc.select(".govuk-table__caption").text() must include("Accounting Period ending")
    }

    "render the correct table headers" in {
      val doc     = render()
      val headers = doc.select("th.govuk-table__header").eachText()
      headers must contain allOf ("Date", "Description", "Amount")
    }

    "render one row per transaction when there are multiple" in {
      val twoTransactions = paymentTransactions :+ PaymentTransaction(
        amount = 99.99,
        paymentType = "CP",
        effectiveDateOfPayment = LocalDate.of(2026, 2, 1)
      )
      val doc             = render(items = twoTransactions)
      doc.select("tbody.govuk-table__body tr.govuk-table__row").size() mustBe 3
    }

    "render no data rows when there are no transactions" in {
      val doc = render(items = List.empty)
      doc.select("tbody.govuk-table__body tr.govuk-table__row").size() mustBe 1
    }

    "render the breadcrumbs" in {
      val doc = render()
      doc.select(".govuk-breadcrumbs__list-item").size() must be > 0
    }
  }
}
