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

package views.accountingPeriods

import base.SpecBase
import helpers.AccountingPeriodOverviewHelper
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import play.api.i18n.{Lang, Messages, MessagesApi, MessagesImpl}
import play.api.test.FakeRequest
import viewmodels.accountingPeriods.AccountingPeriodOverviewViewModel
import views.ViewUtils.formatDate
import views.html.accountingPeriods.AccountingPeriodsOverviewView

class AccountingPeriodOverviewViewSpec extends SpecBase with AccountingPeriodOverviewHelper {
  val application = applicationBuilder().build()

  val view: AccountingPeriodsOverviewView = application.injector.instanceOf[AccountingPeriodsOverviewView]

  implicit val messagesApi: MessagesApi = application.injector.instanceOf[MessagesApi]
  implicit val messages: Messages       = MessagesImpl(Lang.defaultLang, messagesApi)

  implicit val request: FakeRequest[_] = FakeRequest()

  def render(viewModel: AccountingPeriodOverviewViewModel): Document =
    Jsoup.parse(view(viewModel)(request, messages(application)).toString)

  // TODO: Extra tests covering all content
  "AccountingPeriodsOverviewView" - {

    "render the correct page title" in {
      val doc = render(viewModel)
      doc.title() mustBe "Accounting period overview - GOV.UK"
    }

    "render the correct heading" in {
      val doc = render(viewModel)
      doc
        .select("h1.govuk-heading-l")
        .text() mustBe s"Accounting period ending ${formatDate(accountingPeriodEndDate, messages.lang)}"
    }

    "render the paragraph with tax reference" in {
      val doc = render(viewModel)
      doc
        .getElementsByClass("govuk-body")
        .get(0)
        .text() mustBe s"When paying for this accounting period, use reference number $taxReference."
    }

    "render the link with correct text and href" in {
      val doc = render(viewModel)
      doc.getElementsByClass("govuk-body").get(1).text() mustBe s"How to pay (opens in new tab)"
      doc
        .getElementsByClass("govuk-body")
        .get(1)
        .select("a")
        .attr("href") mustBe "https://www.gov.uk/pay-corporation-tax"

    }

    "render the correct table headers" in {
      val doc     = render(viewModel)
      val headers = doc.select("th.govuk-table__header").eachText()
      headers must contain allOf (
        messages("Description"),
        messages("Amount")
      )
      headers.size() mustBe 2
    }

    "render the correct breadcrumbs" in {
      val doc         = render(viewModel)
      val breadcrumbs = doc.select("li.govuk-breadcrumbs__list-item").eachText()

      breadcrumbs must contain allOf (
        messages("Home"),
        messages("Accounting periods")
      )
      doc.select(".govuk-breadcrumbs__list-item").size() mustBe 2
    }

    "render correct table with all content" in {
      val doc      = render(viewModel)
      val firstRow =
        doc.select("tbody.govuk-table__body tr.govuk-table__row").get(0).getElementsByClass("govuk-table__cell")
      firstRow.get(0).text() mustBe "Taxes"
      firstRow.get(1).text() mustBe "£100.00"

      val secondRow =
        doc.select("tbody.govuk-table__body tr.govuk-table__row").get(1).getElementsByClass("govuk-table__cell")
      secondRow.get(0).text() mustBe "Interest"
      secondRow.get(1).text() mustBe "£150.00"

      val thirdRow =
        doc.select("tbody.govuk-table__body tr.govuk-table__row").get(2).getElementsByClass("govuk-table__cell")
      thirdRow.get(0).text() mustBe "Penalties"
      thirdRow.get(1).text() mustBe "£100.00"

      val forthRow =
        doc.select("tbody.govuk-table__body tr.govuk-table__row").get(3).getElementsByClass("govuk-table__cell")
      forthRow.get(0).text() mustBe "Subtotal"
      forthRow.get(1).text() mustBe "£350.00"

      val fifthRow =
        doc.select("tbody.govuk-table__body tr.govuk-table__row").get(4).getElementsByClass("govuk-table__cell")
      fifthRow.get(0).text() mustBe "Payments"
      fifthRow.get(1).text() mustBe "£300.00"

      val sixthRow =
        doc.select("tbody.govuk-table__body tr.govuk-table__row").get(5).getElementsByClass("govuk-table__cell")
      sixthRow.get(0).text() mustBe "Repayments and reallocations"
      sixthRow.get(1).text() mustBe "£250.00"

      val seventhRow =
        doc.select("tbody.govuk-table__body tr.govuk-table__row").get(6).getElementsByClass("govuk-table__cell")
      seventhRow.get(0).text() mustBe "Adjustments"
      seventhRow.get(1).text() mustBe "£155.00"

      val eighthRow =
        doc.select("tbody.govuk-table__body tr.govuk-table__row").get(7).getElementsByClass("govuk-table__cell")
      eighthRow.get(0).text() mustBe "Total"
      eighthRow.get(1).text() mustBe "£1,055.00"

      doc.select("tbody.govuk-table__body tr.govuk-table__row").size() mustBe 8

    }

    "render correct table with display needed set false content" in {
      val doc = render(viewModelNoDisplay)

      val rows = doc.select("tbody.govuk-table__body tr.govuk-table__row")

      val linkRows = doc.getElementsByClass("govuk-table__cell").select("a")

      rows.size() mustBe 4
      rows.text() mustNot contain oneOf ("Taxes", "Interest", "Payments", "Repayments and reallocation")
      linkRows.size() mustBe 2

    }

    "render correct table with no links where amount equals 0" in {
      val doc = render(viewModelNoLinks)

      val linkRows = doc.getElementsByClass("govuk-table__cell").select("a")

      linkRows.size() mustBe 0
    }
  }
}
