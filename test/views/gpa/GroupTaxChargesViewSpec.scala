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

package views.gpa

import base.SpecBase
import controllers.gpa.routes
import helpers.gpa.GroupTaxChargesHelper
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import play.api.i18n.{Lang, Messages, MessagesApi, MessagesImpl}
import play.api.test.FakeRequest
import viewmodels.gpa.GroupTaxChargesViewModel
import views.html.gpa.GroupTaxChargesView
import play.api.Application

class GroupTaxChargesViewSpec extends SpecBase with GroupTaxChargesHelper {
  val application: Application = applicationBuilder().build()

  val view: GroupTaxChargesView = application.injector.instanceOf[GroupTaxChargesView]

  implicit val messagesApi: MessagesApi = application.injector.instanceOf[MessagesApi]
  implicit val messages: Messages       = MessagesImpl(Lang.defaultLang, messagesApi)

  val viewModel = returnGroupTaxChargesViewModel(
    gpaGroupTaxChargesWithTwoParticipators,
    Some(1),
    pageUrl = pageNumber => routes.GroupTaxChargesController.onPageLoad(Some(pageNumber)).url
  )

  implicit val request: FakeRequest[_] = FakeRequest()

  def render(viewModel: GroupTaxChargesViewModel): Document =
    Jsoup.parse(view(viewModel)(request, messages(application)).toString)

  "GroupTaxChargesView" - {

    "render the correct page title" in {
      val doc = render(viewModel)
      doc.title() mustBe "Group taxes – Group Payment Arrangement - GOV.UK"
    }

    "render the correct heading" in {
      val doc = render(viewModel)
      doc
        .select("h1.govuk-heading-l")
        .get(0)
        .text() mustBe s"Group taxes"
    }

    "render the correct breadcrumbs" in {
      val doc         = render(viewModel)
      val breadcrumbs = doc.select("li.govuk-breadcrumbs__list-item").eachText()

      breadcrumbs.get(0) mustBe messages("Home")
      breadcrumbs.get(1) mustBe messages("Group Payment Arrangement")

      doc.select(".govuk-breadcrumbs__list-item").size() mustBe 2
    }

    "render Summary List" in {
      val doc             = render(viewModel)
      val summaryListRows = doc.select("dl.govuk-summary-list").get(0).getElementsByClass("govuk-summary-list__row")

      summaryListRows.get(0).text mustBe "Group Payment Arrangement reference 23456789012"
      summaryListRows.get(0).text mustBe "Group Payment Arrangement reference 23456789012"
      summaryListRows.get(1).text mustBe "Period of account ending 31 Mar 2026"
      summaryListRows.get(2).text mustBe "Group period of account status Cleared"
      summaryListRows.get(3).text mustBe "Group payment total £125,000.50"
      summaryListRows.get(4).text mustBe "Group tax total £31,250.25"

      doc.select("dl.govuk-summary-list div.govuk-summary-list__row").size() mustBe 5
    }

    "render Group Taxes subheading" in {
      val doc = render(viewModel)
      doc
        .select("h1.govuk-heading-l")
        .get(1)
        .text() mustBe s"Group tax records"
    }

    "render the correct table headers. Table below Group Taxes subheading" in {
      val doc     = render(viewModel)
      val headers = doc.select("th.govuk-table__header").eachText()
      headers must contain allOf (
        messages("Participating company"),
        messages("Unique Taxpayer Reference"),
        messages("Accounting period ending"),
        messages("Tax liability"),
        messages("Payment allocated")
      )
      headers.size() mustBe 5
    }

    "render the first row for table Group Taxes" in {
      val doc      = render(viewModel)
      val firstRow =
        doc.select("tbody.govuk-table__body tr.govuk-table__row").get(0).getElementsByClass("govuk-table__cell")

      firstRow.get(0).text() mustBe "Example Holdings Ltd"
      firstRow.get(1).text() mustBe "34567890123"
      firstRow.get(2).text() mustBe "31 Mar 2026"
      firstRow.get(3).text() mustBe "£18,750.12"
      firstRow.get(4).text() mustBe "£62,500.25"
    }

  }
}
