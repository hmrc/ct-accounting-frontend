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
import helpers.AccountPositionHelper
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import play.api.i18n.{Lang, Messages, MessagesApi, MessagesImpl}
import play.api.test.FakeRequest
import viewmodels.accountingPeriods.AccountingPeriodsViewModel
import views.ViewUtils.formatCurrency
import views.html.AccountingPeriodsView

class AccountingPeriodsViewSpec extends SpecBase with AccountPositionHelper {
  val application = applicationBuilder().build()

  val view: AccountingPeriodsView = application.injector.instanceOf[AccountingPeriodsView]

  implicit val messagesApi: MessagesApi = application.injector.instanceOf[MessagesApi]
  implicit val messages: Messages       = MessagesImpl(Lang.defaultLang, messagesApi)

  implicit val request: FakeRequest[_] = FakeRequest()

  def render(viewModel: AccountingPeriodsViewModel): Document =
    Jsoup.parse(view(viewModel)(request, messages(application)).toString)

  "AccountingPeriodsView" - {

    "render the correct page title" in {
      val doc = render(viewModel)
      doc.title() mustBe "Accounting periods - GOV.UK"
    }

    "render the correct heading" in {
      val doc = render(viewModel)
      doc
        .select("h1.govuk-heading-l")
        .text() mustBe "Accounting periods"
    }

    "render the paragraph with balance" in {
      val doc = render(viewModel)
      doc
        .getElementsByClass("govuk-body")
        .get(0)
        .text() mustBe s"Your Corporation Tax balance is ${formatCurrency(amountDue)}. This amount is based on the accounting periods currently shown."
    }

    "render the correct paragraph when balance is zero and account periods empty" in {
      val doc = render(viewModelNoAccounts)
      doc
        .getElementsByClass("govuk-body")
        .get(0)
        .text() mustBe "There is no data for any accounting periods yet."
    }

    "render the correct table headers" in {
      val doc     = render(viewModel)
      val headers = doc.select("th.govuk-table__header").eachText()
      headers must contain allOf (
        messages("Accounting period ending"),
        messages("Status"),
        messages("Amount")
      )
      headers.size() mustBe 3
    }

    "render the correct breadcrumbs" in {
      val doc         = render(viewModel)
      val breadcrumbs = doc.select("li.govuk-breadcrumbs__list-item").eachText()

      breadcrumbs must contain(messages("Home"))
      doc.select(".govuk-breadcrumbs__list-item").size() mustBe 1
    }

    "render correct table with all content" in {
      val doc      = render(viewModel)
      val firstRow =
        doc.select("tbody.govuk-table__body tr.govuk-table__row").get(0).getElementsByClass("govuk-table__cell")
      firstRow.get(0).text() mustBe "01 Jan 2026"
      firstRow.get(1).text() mustBe "Live"
      firstRow.get(2).text() mustBe "£400.50"

      val secondRow =
        doc.select("tbody.govuk-table__body tr.govuk-table__row").get(1).getElementsByClass("govuk-table__cell")
      secondRow.get(0).text() mustBe "01 Jan 2025"
      secondRow.get(1).text() mustBe "Superseded"
      secondRow.get(2).text() mustBe "£123.50"

      val thirdRow =
        doc.select("tbody.govuk-table__body tr.govuk-table__row").get(2).getElementsByClass("govuk-table__cell")
      thirdRow.get(0).text() mustBe "01 Jan 2024"
      thirdRow.get(1).text() mustBe "Exempt"
      thirdRow.get(2).text() mustBe "£456.50"

      val forthRow =
        doc.select("tbody.govuk-table__body tr.govuk-table__row").get(3).getElementsByClass("govuk-table__cell")
      forthRow.get(0).text() mustBe "01 Jan 2023"
      forthRow.get(1).text() mustBe "Dormant"
      forthRow.get(2).text() mustBe "£789.50"

      doc.select("tbody.govuk-table__body tr.govuk-table__row").size() mustBe 4

    }

    "not render table when balance is 0 and account period list empty" in {
      val doc = render(viewModelNoAccounts)
      doc.select("tbody.govuk-table__body tr.govuk-table__row").size() mustBe 0
    }
  }
}
