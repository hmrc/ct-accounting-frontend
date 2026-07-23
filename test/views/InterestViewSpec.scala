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
import helpers.AccountingPeriodResponseHelper
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.scalatest.matchers.should.Matchers.should
import play.api.Application
import play.api.test.FakeRequest
import viewmodels.InterestViewModel
import views.html.InterestView

class InterestViewSpec extends SpecBase with AccountingPeriodResponseHelper {

  val application: Application = applicationBuilder().build()
  val view: InterestView       = application.injector.instanceOf[InterestView]

  implicit val request: FakeRequest[_] = FakeRequest()

  def render(viewModel: InterestViewModel): Document =
    Jsoup.parse(view(viewModel)(request, messages(application)).toString)

  "InterestView" - {

    "render the page title" in {
      val doc = render(accountingResponseEquivalentViewModel)
      doc.title mustBe "Interest - Accounting period overview - GOV.UK"
    }

    "render the heading" in {
      val doc = render(accountingResponseEquivalentViewModel)
      doc.select("h1").text() mustBe "Interest"
    }

    "render the table caption" in {
      val doc = render(accountingResponseEquivalentViewModel)
      doc.select("caption.govuk-table__caption").text() mustBe "Accounting period ending 30 Sep 2025"
    }

    "render the table headers" in {
      val doc     = render(accountingResponseEquivalentViewModel)
      val headers = doc.select("thead th")
      headers.size() mustBe 2
      headers.get(0).text() mustBe "Description"
      headers.get(1).text() mustBe "Amount"
      headers.get(1).hasClass("govuk-table__cell--numeric") mustBe true
    }

    "render all interest rows with description, link and formatted amount" in {
      val doc  = render(accountingResponseEquivalentViewModel)
      val rows = doc.select("tbody tr")

      val expectedRows = Seq(
        ("Late payment interest(still accruing)", "25.50"),
        ("Repayment interest", "0.00"),
        ("Debit interest", "15.75"),
        ("Credit Interest", "0.00")
      )

      expectedRows.zipWithIndex.foreach { case ((description, amount), index) =>
        val row = rows.get(index)
        row.text() must include(description)
        row.text() must include(amount)

        val link = row.select("a").first()
        link.text() mustBe description
        link.attr("href") mustBe "/"
        link.hasClass("govuk-link") mustBe true
      }
    }

    "render numeric amount cells with the numeric class" in {
      val doc         = render(accountingResponseEquivalentViewModel)
      val amountCells = doc.select("tbody tr td.govuk-table__cell--numeric")
      amountCells.size() must be >= 4
    }

    "render the total row" in {
      val doc      = render(accountingResponseEquivalentViewModel)
      val totalRow = doc.select("tbody tr").last()
      totalRow.text() must include("Total")
      totalRow.text() must include("£41.25")
    }

    "render exactly 4 links in the table body" in {
      val doc = render(accountingResponseEquivalentViewModel)
      doc.select("tbody a").size() mustBe 4
    }
  }
}
