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
import helpers.PenaltiesDataHelper
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import play.api.i18n.{Lang, Messages, MessagesApi, MessagesImpl}
import play.api.test.FakeRequest
import viewmodels.accountingPeriods.PenaltiesAccountingPeriodViewModel
import views.ViewUtils.formatDate
import views.html.accountingPeriods.PenaltiesAccountingPeriodView

class PenaltiesAccountingPeriodViewSpec extends SpecBase with PenaltiesDataHelper {

  val application = applicationBuilder().build()

  val view = application.injector.instanceOf[PenaltiesAccountingPeriodView]

  implicit val messagesApi: MessagesApi = application.injector.instanceOf[MessagesApi]
  implicit val messages: Messages       = MessagesImpl(Lang.defaultLang, messagesApi)

  implicit val request: FakeRequest[_] = FakeRequest()

  def render(viewModel: PenaltiesAccountingPeriodViewModel): Document =
    Jsoup.parse(view(viewModel)(request, messages(application)).toString)

  "PenaltiesAccountingPeriodView" - {

    "render the correct page title" in {
      val doc = render(viewModelWithTwoRows)
      doc.title() must include(messages("penaltiesAccountingPeriod.title"))
      doc.title() must include(messages("penaltiesAccountingPeriod.section"))
    }

    "render the correct heading" in {
      val doc = render(viewModelWithTwoRows)
      doc.select("h1").text() must include(messages("penaltiesAccountingPeriod.heading"))
    }

    "render the table caption with the formatted account period date" in {
      val doc          = render(viewModelWithTwoRows)
      val expectedDate = formatDate(viewModelWithTwoRows.accountingPeriodEnd, messages.lang)
      doc.select(".govuk-table__caption").text() mustBe messages("penaltiesAccountingPeriod.caption", expectedDate)
    }

    "render the correct number of table rows" in {
      val doc = render(viewModelWithTwoRows)
      doc.select("tbody.govuk-table__body tr.govuk-table__row").size() mustBe 3
    }

    "render the accounting period dates in the table" in {
      val doc           = render(viewModelWithTwoRows)
      val expectedDates = viewModelWithTwoRows.rows.map(row => formatDate(row.date, messages.lang))
      val actualDates   = doc.select("tbody tr.govuk-table__row td:first-child").eachText().toArray.toSeq
      actualDates.take(expectedDates.size) mustBe expectedDates
    }

    "render the correct table headers" in {
      val doc     = render(viewModelWithTwoRows)
      val headers = doc.select("th.govuk-table__header").eachText()
      headers must contain allOf (
        messages("penaltiesAccountingPeriod.date"),
        messages("penaltiesAccountingPeriod.description"),
        messages("penaltiesAccountingPeriod.amount")
      )
    }

    "render the correct breadcrumbs" in {
      val doc         = render(viewModelWithTwoRows)
      val breadcrumbs = doc.select("li.govuk-breadcrumbs__list-item").eachText()
      breadcrumbs must contain allOf (
        messages("breadcrumbs.home"),
        messages("breadcrumbs.accountingPeriods"),
        messages("breadcrumbs.accountingPeriodEnding")
      )
      doc.select(".govuk-breadcrumbs__list-item").size() mustBe 3
    }
  }

}
