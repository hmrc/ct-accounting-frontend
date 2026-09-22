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
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import play.api.i18n.{Lang, Messages, MessagesApi, MessagesImpl}
import play.api.test.FakeRequest
import views.ViewUtils.formatDate
import views.html.accountingPeriods.RepaymentInterestView
import viewmodels.accountingPeriods.RepaymentInterestViewModel
import helpers.RepaymentInterestHelper

class RepaymentInterestViewSpec extends SpecBase with RepaymentInterestHelper {
  val application = applicationBuilder().build()

  val view: RepaymentInterestView = application.injector.instanceOf[RepaymentInterestView]

  implicit val messagesApi: MessagesApi = application.injector.instanceOf[MessagesApi]
  implicit val messages: Messages       = MessagesImpl(Lang.defaultLang, messagesApi)

  implicit val request: FakeRequest[_] = FakeRequest()

  def render(viewModel: RepaymentInterestViewModel): Document =
    Jsoup.parse(view(viewModel)(request, messages(application)).toString)

  // TODO: Extra tests covering all content
  "RepaymentInterestView" - {

    "render the correct page title" in {
      val doc = render(viewModel)
      doc.title() must include(messages("repaymentInterest.title"))
      doc.title() must include(messages("repaymentInterest.section"))
    }

    "render the correct heading" in {
      val doc = render(viewModel)
      doc.select("h1.govuk-heading-l").text() mustBe messages("repaymentInterest.heading")
    }

    "render the table caption with the formatted account period" in {
      val doc = render(viewModel)
      doc.select(".govuk-table__caption").text() must include(
        messages("repaymentInterest.table.header", formatDate(accountingPeriodEndDate, messages.lang))
      )
    }

    "render the correct table headers" in {
      val doc     = render(viewModel)
      val headers = doc.select("th.govuk-table__header").eachText()
      headers must contain allOf (
        messages("repaymentInterest.subject"),
        messages("repaymentInterest.fromDate"),
        messages("repaymentInterest.toDate"),
        messages("repaymentInterest.days"),
        messages("repaymentInterest.percentage"),
        messages("repaymentInterest.interest")
      )
      headers.size() mustBe 6
    }

    "render one row per transaction when there are multiple" in {
      val doc = render(eqvViewModelOfInterestAccrualMultipleObjects)
      doc.select("tbody.govuk-table__body tr.govuk-table__row").size() mustBe 3
    }

    "render no data rows when there are no transactions" in {
      val doc = render(viewModelEmptyList)
      doc.select("tbody.govuk-table__body tr.govuk-table__row").size() mustBe 1
    }

    "render the correct breadcrumbs" in {
      val doc         = render(viewModel)
      val breadcrumbs = doc.select("li.govuk-breadcrumbs__list-item").eachText()
      breadcrumbs must contain allOf (
        messages("breadcrumbs.home"),
        messages("breadcrumbs.accountingPeriods"),
        messages("breadcrumbs.accountingPeriodEnding"),
        messages("breadcrumbs.interest")
      )
      doc.select(".govuk-breadcrumbs__list-item").size() mustBe 4
    }

    "render correct table content" in {
      val doc      = render(viewModel)
      val firstRow =
        doc.select("tbody.govuk-table__body tr.govuk-table__row").get(0).getElementsByClass("govuk-table__cell")
      firstRow.get(0).text() mustBe "£10,000.00"
      firstRow.get(1).text() mustBe "30 Jun 2024"
      firstRow.get(2).text() mustBe "01 Apr 2024"
      firstRow.get(3).text() mustBe "91"
      firstRow.get(4).text() mustBe "7.75%"
      firstRow.get(5).text() mustBe "£193.22"
    }
  }
}
