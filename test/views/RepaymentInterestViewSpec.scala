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
import models.InterestAccrualWithInterestAccruedDays
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import play.api.i18n.{Lang, Messages, MessagesApi, MessagesImpl}
import play.api.test.FakeRequest
import views.ViewUtils.formatDate
import views.html.accountingPeriods.RepaymentInterestView

import java.time.LocalDate

class RepaymentInterestViewSpec extends SpecBase {
  val application = applicationBuilder().build()

  val view: RepaymentInterestView = application.injector.instanceOf[RepaymentInterestView]

  implicit val messagesApi: MessagesApi = application.injector.instanceOf[MessagesApi]
  implicit val messages: Messages       = MessagesImpl(Lang.defaultLang, messagesApi)

  implicit val request: FakeRequest[_] = FakeRequest()

  val accountPeriod: LocalDate = LocalDate.of(2026, 1, 1)

  val total: BigDecimal                                                       = 10000.12
  val repaymentInterestResponse: List[InterestAccrualWithInterestAccruedDays] = List(
    InterestAccrualWithInterestAccruedDays(
      computationAmount = BigDecimal(3125.43),
      interestAccrualFromDate = LocalDate.of(2026, 3, 1),
      interestAccrualToDate = LocalDate.of(2026, 4, 1),
      interestRate = BigDecimal(42),
      interestAmount = BigDecimal(535.12),
      apEndDate = LocalDate.of(2026, 1, 1),
      noOfDays = 7
    )
  )

  def render(items: List[InterestAccrualWithInterestAccruedDays] = repaymentInterestResponse): Document =
    Jsoup.parse(view(items, accountPeriod, total)(request, messages(application)).toString)

  // TODO: Extra tests covering all content
  "RepaymentInterestView" - {

    "render the correct page title" in {
      val doc = render()
      doc.title() must include(messages("repaymentInterest.title"))
      doc.title() must include(messages("repaymentInterest.section"))
    }

    "render the correct heading" in {
      val doc = render()
      doc.select("h1.govuk-heading-l").text() mustBe messages("repaymentInterest.heading")
    }

    "render the table caption with the formatted account period" in {
      val doc = render()
      doc.select(".govuk-table__caption").text() must include(
        messages("repaymentInterest.table.header", formatDate(accountPeriod, messages.lang))
      )
    }

    "render the correct table headers" in {
      val doc     = render()
      val headers = doc.select("th.govuk-table__header").eachText()
      headers must contain allOf (
        messages("repaymentInterest.subject"),
        messages("repaymentInterest.fromDate"),
        messages("repaymentInterest.toDate"),
        messages("repaymentInterest.days"),
        messages("repaymentInterest.percentage"),
        messages("repaymentInterest.interest")
      )
    }

    "render one row per transaction when there are multiple" in {
      val twoAccruals = repaymentInterestResponse :+ InterestAccrualWithInterestAccruedDays(
        computationAmount = BigDecimal(213.43),
        interestAccrualFromDate = LocalDate.of(2026, 2, 1),
        interestAccrualToDate = LocalDate.of(2026, 2, 1),
        interestRate = BigDecimal(1),
        interestAmount = BigDecimal(34.12),
        apEndDate = LocalDate.of(2026, 1, 1),
        noOfDays = 1
      )
      val doc         = render(items = twoAccruals)
      doc.select("tbody.govuk-table__body tr.govuk-table__row").size() mustBe 3
    }

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
        messages("breadcrumbs.accountingPeriodEnding"),
        messages("breadcrumbs.interest")
      )
      doc.select(".govuk-breadcrumbs__list-item").size() mustBe 4
    }
  }
}
