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
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import play.api.i18n.{Lang, Messages, MessagesApi, MessagesImpl}
import play.api.test.FakeRequest
import viewmodels.{GroupPaymentArrangementViewModel, GroupPaymentRecord}
import views.html.GroupPaymentArrangementsView

import java.time.LocalDate

class GroupPaymentArrangementsViewSpec extends SpecBase {
  val application = applicationBuilder().build()

  val viewModel = GroupPaymentArrangementViewModel(
    arrangementReference = "933636936A00104A",
    accountEnding = LocalDate.of(2026, 1, 1),
    accountStatus = "Open",
    paymentTotal = BigDecimal(1125000),
    taxTotal = BigDecimal(1125000),
    records = List(
      GroupPaymentRecord(
        date = LocalDate.of(2025, 1, 1),
        description = "Electronic payment",
        amount = BigDecimal(50.17)
      ),
      GroupPaymentRecord(
        date = LocalDate.of(2021, 2, 7),
        description = "Electronic payment",
        amount = BigDecimal(475)
      ),
      GroupPaymentRecord(
        date = LocalDate.of(2026, 4, 8),
        description = "Electronic payment",
        amount = BigDecimal(50.18)
      )
    )
  )

  val view: GroupPaymentArrangementsView = application.injector.instanceOf[GroupPaymentArrangementsView]

  implicit val messagesApi: MessagesApi = application.injector.instanceOf[MessagesApi]
  implicit val messages: Messages       = MessagesImpl(Lang.defaultLang, messagesApi)

  implicit val request: FakeRequest[_] = FakeRequest()

  def render(viewModel: GroupPaymentArrangementViewModel): Document =
    Jsoup.parse(view(viewModel)(request, messages(application)).toString)

  "GroupPaymentArrangementView" - {

    "render the correct page title" in {
      val doc = render(viewModel)
      doc.title() mustBe "Group payments – Group Payment Arrangement - GOV.UK"
    }

    "render the correct heading" in {
      val doc = render(viewModel)
      doc
        .select("h1.govuk-heading-l")
        .text() mustBe s"Group payments"
    }

    "render the correct breadcrumbs" in {
      val doc         = render(viewModel)
      val breadcrumbs = doc.select("li.govuk-breadcrumbs__list-item").eachText()

      breadcrumbs must contain allOf (
        messages("Home"),
        messages("Group Payment Arrangement")
      )
      doc.select(".govuk-breadcrumbs__list-item").size() mustBe 2
    }

    "render correct table with all content" in {
      val doc      = render(viewModel)
      val firstRow =
        doc.select("tbody.govuk-table__body tr.govuk-table__row").get(0).getElementsByClass("govuk-table__cell")
      firstRow.get(0).text() mustBe "01 Jan 2025"
      firstRow.get(1).text() mustBe "Electronic payment"
      firstRow.get(2).text() mustBe "£50.17"

      val secondRow =
        doc.select("tbody.govuk-table__body tr.govuk-table__row").get(1).getElementsByClass("govuk-table__cell")
      secondRow.get(0).text() mustBe "07 Feb 2021"
      secondRow.get(1).text() mustBe "Electronic payment"
      secondRow.get(2).text() mustBe "£475.00"

      val thirdRow =
        doc.select("tbody.govuk-table__body tr.govuk-table__row").get(2).getElementsByClass("govuk-table__cell")
      thirdRow.get(0).text() mustBe "08 Apr 2026"
      thirdRow.get(1).text() mustBe "Electronic payment"
      thirdRow.get(2).text() mustBe "£50.18"

      doc.select("tbody.govuk-table__body tr.govuk-table__row").size() mustBe 3
    }

    "render correct table with no links where amount equals 0" in {
      val doc = render(viewModel)

      val text = doc
        .getElementsByClass("govuk-table__caption govuk-table__caption govuk-table__caption--m")
        .select("caption")
        .text()

      text mustBe "Group payment records"
    }

  }
}
