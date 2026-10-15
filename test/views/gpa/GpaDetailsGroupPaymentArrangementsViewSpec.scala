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
import helpers.gpa.GroupPaymentHelper
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import play.api.Application
import play.api.i18n.{Lang, Messages, MessagesApi, MessagesImpl}
import play.api.test.FakeRequest
import viewmodels.gpa.GpaDetailsGroupPaymentArrangementPageViewModel
import views.html.gpa.GroupPaymentArrangementsView

class GroupPaymentArrangementsViewSpec extends SpecBase with GroupPaymentHelper {

  val application: Application = applicationBuilder().build()

  val pageUrl: Int => String = page => s"/group-payment-arrangement?page=$page"

  def viewModel(paginationIndex: Option[Int]): GpaDetailsGroupPaymentArrangementPageViewModel =
    returnGroupPaymentPageViewModel(viewModelWithMultipleRecords, paginationIndex, pageUrl)

  val paginationIndex: Option[Int] = Some(1)

  val view: GroupPaymentArrangementsView = application.injector.instanceOf[GroupPaymentArrangementsView]

  implicit val messagesApi: MessagesApi = application.injector.instanceOf[MessagesApi]
  implicit val messages: Messages       = MessagesImpl(Lang.defaultLang, messagesApi)

  implicit val request: FakeRequest[_] = FakeRequest()

  def render(viewModel: GpaDetailsGroupPaymentArrangementPageViewModel): Document =
    Jsoup.parse(view(viewModel)(request, messages(application)).toString)

  "GroupPaymentArrangementView" - {
    val paginationIndex = Some(1)
    "render the correct page title" in {

      val doc = render(viewModel(paginationIndex))
      doc.title() mustBe "Group payments – Group Payment Arrangement - GOV.UK"
    }
    "render the correct heading" in {
      val doc = render(viewModel(paginationIndex))
      doc
        .select("h1.govuk-heading-l")
        .text() mustBe s"Group payments"
    }

    "render the correct breadcrumbs" in {
      val doc         = render(viewModel(paginationIndex))
      val breadcrumbs = doc.select("li.govuk-breadcrumbs__list-item").eachText()

      breadcrumbs must contain allOf (
        messages("Home"),
        messages("Group Payment Arrangement")
      )
      doc.select(".govuk-breadcrumbs__list-item").size() mustBe 2
    }
    "render correct table with all content" in {
      val doc      = render(viewModel(paginationIndex))
      val firstRow =
        doc.select("tbody.govuk-table__body tr.govuk-table__row").get(0).getElementsByClass("govuk-table__cell")
      firstRow.get(0).text() mustBe "01 Jan 2025"
      firstRow.get(1).text() mustBe ""
      firstRow.get(2).text() mustBe "£17.01"

      val secondRow =
        doc.select("tbody.govuk-table__body tr.govuk-table__row").get(1).getElementsByClass("govuk-table__cell")
      secondRow.get(0).text() mustBe "01 Jan 2026"
      secondRow.get(1).text() mustBe "HMRC Credit"
      secondRow.get(2).text() mustBe "£180.01"

      val thirdRow =
        doc.select("tbody.govuk-table__body tr.govuk-table__row").get(2).getElementsByClass("govuk-table__cell")
      thirdRow.get(0).text() mustBe "01 Jan 2027"
      thirdRow.get(1).text() mustBe "Reallocation FROM 99, AP ending 2026-01-01"
      thirdRow.get(2).text() mustBe "£20.01"

      doc.select("tbody.govuk-table__body tr.govuk-table__row").size() mustBe 12
    }
    "render pagination with a Next link and no Previous link on the first page" in {
      val doc = render(viewModel(Some(1)))

      doc.select("nav.govuk-pagination").size() mustBe 1
      doc.select(".govuk-pagination__prev").isEmpty mustBe true
      doc.select(".govuk-pagination__next a").attr("href") mustBe pageUrl(2)
      doc.select(".govuk-pagination__item--current a").text() mustBe "1"
    }

    "render pagination when all records doesn't fit on one page" in {
      val doc = render(viewModel(Some(2)))
      doc.select("nav.govuk-pagination").isEmpty mustBe false
    }

    "render correct table with no links where amount equals 0" in {
      val paginationIndex = Some(1)
      val doc             = render(viewModel(paginationIndex))
      val text            = doc
        .getElementsByClass("govuk-table__caption govuk-table__caption govuk-table__caption--m")
        .select("caption")
        .text()
      text mustBe "Group payment records"
    }

  }
}
