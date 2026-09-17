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
import helpers.LatePaymentInterestViewModelHelper
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.scalatest.matchers.should.Matchers.should
import play.api.Application
import play.api.i18n.{Lang, Messages, MessagesApi, MessagesImpl}
import play.api.test.FakeRequest
import viewmodels.LatePaymentInterestViewModel
import views.html.LatePaymentInterestStillAccruingView

class LatePaymentInterestStillAccruingViewSpec extends SpecBase with LatePaymentInterestViewModelHelper {

  val application: Application                   = applicationBuilder().build()
  val view: LatePaymentInterestStillAccruingView = application.injector.instanceOf[LatePaymentInterestStillAccruingView]

  implicit val messagesApi: MessagesApi = application.injector.instanceOf[MessagesApi]
  implicit val messages: Messages       = MessagesImpl(Lang.defaultLang, messagesApi)

  implicit val request: FakeRequest[_] = FakeRequest()

  def render(viewModel: LatePaymentInterestViewModel): Document =
    Jsoup.parse(view(viewModel)(request, messages(application)).toString)

  "LatePaymentInterestStillAccruingView" - {

    "render the page title" in {
      val doc = render(viewModel)

      doc.title() must include(messages("latePaymentInterest.stillAccruing.title"))
      doc.title() must include(messages("latePaymentInterest.stillAccruing.section"))
    }

    "render the heading" in {
      val doc = render(viewModel)
      doc.select("h1").text() mustBe messages("latePaymentInterest.stillAccruing.heading")
    }

    "render the table caption" in {
      val doc = render(viewModel)
      doc.select("caption.govuk-table__caption").text() mustBe messages("latePaymentInterest.stillAccruing.caption")
    }

    "render the table headers with matching values" in {
      val doc     = render(viewModel)
      val headers = doc.select("thead th")
      headers.size() mustBe 6
      headers.get(0).text() mustBe messages("latePaymentInterest.stillAccruing.amountSubjectToInterest")
      headers.get(1).text() mustBe messages("latePaymentInterest.stillAccruing.toDate")
      headers.get(2).text() mustBe messages("latePaymentInterest.stillAccruing.fromDate")
      headers.get(3).text() mustBe messages("latePaymentInterest.stillAccruing.daysOverdue")
      headers.get(4).text() mustBe messages("latePaymentInterest.stillAccruing.annualPercentageRate")
      headers.get(5).text() mustBe messages("latePaymentInterest.stillAccruing.interestAccrued")

      headers.get(3).hasClass("govuk-table__cell--numeric") mustBe true
      headers.get(4).hasClass("govuk-table__cell--numeric") mustBe true
      headers.get(5).hasClass("govuk-table__cell--numeric") mustBe true
    }

    "render numeric amount cells with the numeric class" in {
      val doc         = render(viewModel)
      val amountCells = doc.select("tbody tr td.govuk-table__cell--numeric")
      amountCells.size() must be <= 4
    }

    "render the total row" in {
      val doc      = render(viewModel)
      val totalRow = doc.select("tbody tr").last()
      totalRow.text() must include(messages("latePaymentInterest.stillAccruing.total"))
      totalRow.text() must include("£193.22")
    }
    "render the annual percentage rate row" in {
      val doc = render(viewModel)

      val headers = doc.select("table thead th")
      headers.get(4).text() mustBe messages("latePaymentInterest.stillAccruing.annualPercentageRate")

      val firstRowCells = doc.select("table tbody tr").get(0).select("td")
      firstRowCells.get(4).text() mustBe "7.75%"
    }
  }

  "render the correct breadcrumbs" in {
    val doc         = render(viewModel)
    val breadcrumbs = doc.select("li.govuk-breadcrumbs__list-item").eachText()
    breadcrumbs must contain allOf (
      messages("breadcrumbs.home"),
      messages("breadcrumbs.accountingPeriods"),
      messages("breadcrumbs.accountingPeriodEnding"),
      messages("interest.title")
    )
    doc.select(".govuk-breadcrumbs__list-item").size() mustBe 4
  }
}
