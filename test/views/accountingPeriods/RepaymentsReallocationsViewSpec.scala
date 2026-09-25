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
import helpers.RepaymentsReallocationsHelper
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import play.api.i18n.{Lang, Messages, MessagesApi, MessagesImpl}
import play.api.test.FakeRequest
import viewmodels.accountingPeriods.RepaymentReallocationSummaryViewModel
import views.ViewUtils.formatDate
import views.html.accountingPeriods.RepaymentsReallocationsView

import java.time.LocalDate

class RepaymentsReallocationsViewSpec extends SpecBase with RepaymentsReallocationsHelper {
  val application = applicationBuilder().build()

  val view: RepaymentsReallocationsView = application.injector.instanceOf[RepaymentsReallocationsView]

  implicit val messagesApi: MessagesApi = application.injector.instanceOf[MessagesApi]
  implicit val messages: Messages       = MessagesImpl(Lang.defaultLang, messagesApi)

  implicit val request: FakeRequest[_] = FakeRequest()

  val accountPeriod: LocalDate = LocalDate.of(2026, 1, 1)
  val total: BigDecimal        = 10000.12

  def render(
    viewModel: RepaymentReallocationSummaryViewModel =
      RepaymentReallocationSummaryViewModel.convertToViewModel(multipleSummaries)
  ): Document =
    Jsoup.parse(view(viewModel, accountPeriod)(request, messages(application)).toString)

  // TODO: Extra tests covering all content
  "RepaymentsReallocationsView" - {

    "render the correct page title" in {
      val doc = render()
      doc.title() must include(messages("repaymentReallocations.title"))
    }

    "render the correct heading" in {
      val doc = render()
      doc.select("h1.govuk-heading-l").text() mustBe messages("repaymentReallocations.heading")
    }

    "render the table caption with the formatted account period" in {
      val doc = render()
      doc.select(".govuk-table__caption").text() must include(
        messages("repaymentReallocations.table.header", formatDate(accountPeriod, messages.lang))
      )
    }

    "render the correct table headers" in {
      val doc     = render()
      val headers = doc.select("th.govuk-table__header").eachText()
      headers must contain allOf (
        messages("repaymentReallocations.date"),
        messages("repaymentReallocations.description"),
        messages("repaymentReallocations.amount")
      )
    }

    "render one row with Reallocations From Summary transaction" in {
      val doc = render(viewModel = RepaymentReallocationSummaryViewModel.convertToViewModel(reallocationsFromSummary))
      doc.select("tbody.govuk-table__body tr.govuk-table__row").size() mustBe 2
    }

    "render one row with Reallocations To Summary transaction" in {
      val doc = render(viewModel = RepaymentReallocationSummaryViewModel.convertToViewModel(reallocationsToSummary))
      doc.select("tbody.govuk-table__body tr.govuk-table__row").size() mustBe 2
    }

    "render one row with multiple transactions" in {
      val doc = render(viewModel = RepaymentReallocationSummaryViewModel.convertToViewModel(multipleSummaries))
      doc.select("tbody.govuk-table__body tr.govuk-table__row").size() mustBe 3
    }

    "render no data rows when there are no transactions" in {
      val doc = render(viewModel = RepaymentReallocationSummaryViewModel.convertToViewModel(summaryEmptyList))
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
