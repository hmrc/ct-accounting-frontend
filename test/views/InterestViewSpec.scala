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
import helpers.InterestViewModelHelper
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.scalatest.matchers.should.Matchers.should
import play.api.Application
import play.api.i18n.{Lang, Messages, MessagesApi, MessagesImpl}
import play.api.test.FakeRequest
import viewmodels.InterestViewModel
import views.html.InterestView

class InterestViewSpec extends SpecBase with InterestViewModelHelper {

  val application: Application = applicationBuilder().build()
  val view: InterestView       = application.injector.instanceOf[InterestView]

  implicit val messagesApi: MessagesApi = application.injector.instanceOf[MessagesApi]
  implicit val messages: Messages       = MessagesImpl(Lang.defaultLang, messagesApi)

  implicit val request: FakeRequest[_] = FakeRequest()

  def render(viewModel: InterestViewModel): Document =
    Jsoup.parse(view(viewModel)(request, messages(application)).toString)

  "InterestView" - {

    "render the page title" in {
      val doc = render(interestViewModelForAccruing)

      doc.title() must include(messages("interest.title"))
      doc.title() must include(messages("interest.section"))
    }

    "render the heading" in {
      val doc = render(interestViewModelForAccruing)
      doc.select("h1").text() mustBe messages("interest.heading")
    }

    "render the table caption" in {
      val doc = render(interestViewModelForAccruing)
      doc.select("caption.govuk-table__caption").text() mustBe messages("interest.caption")
    }

    "render the table headers" in {
      val doc     = render(interestViewModelForAccruing)
      val headers = doc.select("thead th")
      headers.size() mustBe 2
      headers.get(0).text() mustBe messages("interest.description")
      headers.get(1).text() mustBe messages("interest.amount")
      headers.get(1).hasClass("govuk-table__cell--numeric") mustBe true
    }

    "render a row as a link when isLink is true" in {
      val doc  = render(interestViewModelForAccruing)
      val rows = doc.select("tbody tr")

      val linkRow = rows.get(1) // Repayment interest row has isLink = true

      val anchor = linkRow.select("a.govuk-link")

      anchor.size mustEqual 1
      anchor.text() mustEqual messages("interest.table.repaymentInterest")
      anchor.attr("href") mustEqual "/"
    }
    "render a row as a plain text when isLink is false" in {
      val doc  = render(interestViewModelForAccruing)
      val rows = doc.select("tbody tr")

      val plainRow = rows.get(0) // LatePayment row has isLink = false

      plainRow.select("a").size mustEqual 0
      plainRow.select("span").text() mustEqual messages("interest.table.latePayment.accruing")
    }
  }

  "render numeric amount cells with the numeric class" in {
    val doc         = render(interestViewModelForAccruing)
    val amountCells = doc.select("tbody tr td.govuk-table__cell--numeric")
    amountCells.size() must be >= 4
  }

  "render the total row" in {
    val doc      = render(interestViewModelForAccruing)
    val totalRow = doc.select("tbody tr").last()
    totalRow.text() must include(messages("interest.table.total"))
    totalRow.text() must include("£51.25")
  }

  "render exactly 1 link in the table body" in {
    val doc = render(interestViewModelForAccruing)
    doc.select("tbody a").size() mustBe 1
  }

  "render the correct breadcrumbs" in {
    val doc         = render(interestViewModelForAccruing)
    val breadcrumbs = doc.select("li.govuk-breadcrumbs__list-item").eachText()
    breadcrumbs must contain allOf (
      messages("breadcrumbs.home"),
      messages("breadcrumbs.accountingPeriods"),
      messages("breadcrumbs.accountingPeriodEnding")
    )
    doc.select(".govuk-breadcrumbs__list-item").size() mustBe 3
  }
}
