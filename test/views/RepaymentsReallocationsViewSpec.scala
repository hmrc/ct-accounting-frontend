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
import helpers.RepaymentsReallocationsHelper
import models.{RepayReallocationSummary, RepayReallocationSummaryDetails}
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import play.api.i18n.{Lang, Messages, MessagesApi, MessagesImpl}
import play.api.test.FakeRequest
import views.ViewUtils.formatDate
import views.html.RepaymentsReallocationsView

import java.time.LocalDate

class RepaymentsReallocationsViewSpec extends SpecBase with RepaymentsReallocationsHelper {
  val application = applicationBuilder().build()

  val view: RepaymentsReallocationsView = application.injector.instanceOf[RepaymentsReallocationsView]

  implicit val messagesApi: MessagesApi = application.injector.instanceOf[MessagesApi]
  implicit val messages: Messages       = MessagesImpl(Lang.defaultLang, messagesApi)

  implicit val request: FakeRequest[_] = FakeRequest()

  val accountPeriod: LocalDate = LocalDate.of(2026, 1, 1)
  val total: BigDecimal        = 10000.12

  val paymentTypeDescription: List[String] =
    List(messages("payments.description.IRC"), messages("payments.description.CP"), messages("payments.description.EP"))

  def render(summary: RepayReallocationSummary = multipleSummaries): Document =
    Jsoup.parse(view(summary, accountPeriod, total)(request, messages(application)).toString)

  // TODO: Extra tests covering all content
  "PaymentsView" - {

    "render the correct page title" in {
      val doc = render()
      doc.title() must include(messages("repaymentreallocations.title"))
      doc.title() must include(messages("repaymentreallocations.section"))
    }

    "render the correct heading" in {
      val doc = render()
      doc.select("h1.govuk-heading-l").text() mustBe messages("repaymentreallocations.heading")
    }

    "render the table caption with the formatted account period" in {
      val doc = render()
      doc.select(".govuk-table__caption").text() must include(
        messages("repaymentreallocations.table.header", formatDate(accountPeriod, messages.lang))
      )
    }

    "render the correct table headers" in {
      val doc     = render()
      val headers = doc.select("th.govuk-table__header").eachText()
      headers must contain allOf (
        messages("repaymentreallocations.date"),
        messages("repaymentreallocations.description"),
        messages("repaymentreallocations.amount")
      )
    }

    "render the correct table description" in {
      val doc         = render()
      val description = doc.select("td.govuk-table__cell").text()

      description must include(messages("repaymentreallocations.description.IRC"))
    }
//
//    "render one row with repayment transaction" in {
//      val doc = render(summary = realloctionsFromSummary)
//      doc.select("tbody.govuk-table__body tr.govuk-table__row").size() mustBe 2
//    }
//
//    "render one row with reallocations transaction" in {
//      val doc = render(summary = realloctionsToSummary)
//      doc.select("tbody.govuk-table__body tr.govuk-table__row").size() mustBe 2
//    }
//
//    "render one row with multiple transactions" in {
//      val doc = render(summary = multipleSummaries)
//      doc.select("tbody.govuk-table__body tr.govuk-table__row").size() mustBe 3
//    }

    "render no data rows when there are no transactions" in {
      val doc = render(items = List.empty)
      doc.select("tbody.govuk-table__body tr.govuk-table__row").size() mustBe 1
    }

    "render the correct breadcrumbs" in {
      val doc         = render()
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
