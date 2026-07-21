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
import helpers.PenaltiesDataHelper
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import play.api.test.FakeRequest
import viewmodels.PenaltiesAccountingPeriodViewModel
import views.html.PenaltiesAccountingPeriodView

class PenaltiesAccountingPeriodViewSpec extends SpecBase with PenaltiesDataHelper{

  val application = applicationBuilder().build()

  val view = application.injector.instanceOf[PenaltiesAccountingPeriodView]

  implicit val request: FakeRequest[_] = FakeRequest()

  def render(viewModel: PenaltiesAccountingPeriodViewModel): Document =
    Jsoup.parse(view(viewModel)(request, messages(application)).toString)

  "PenaltiesAccountingPeriodView" - {

    "render the correct page title" in {
      val doc = render(viewModelWithTwoRows)
      println(doc.title())
      doc.title() mustBe "Penalties – Accounting period overview - ct-accounting-frontend - GOV.UK"
    }

//    "render the correct heading" in {
//      val doc = render()
//      doc.select("h1.govuk-heading-xl").text() mustBe "Taxes"
//    }
//
//    "render the table caption with the formatted account period" in {
//      val doc = render()
//      doc.select(".govuk-table__caption").text() must include("Accounting Period ending")
//    }
//
//    "render the correct table headers" in {
//      val doc = render()
//      val headers = doc.select("th.govuk-table__header").eachText()
//      headers must contain allOf("Date", "Description", "Amount")
//    }
//
//    "render one row per transaction when there are multiple" in {
//      val twoTransactions = taxTransactions :+ TaxTransactionsItem(
//        currentAmount = 99.99,
//        assessmentType = "A",
//        taxDate = LocalDate.of(2026, 2, 1),
//        correctionClaimSignal = Some("2")
//      )
//      val doc = render(items = twoTransactions)
//      doc.select("tbody.govuk-table__body tr.govuk-table__row").size() mustBe 3
//    }
//
//    "render no data rows when there are no transactions" in {
//      val doc = render(items = List.empty)
//      doc.select("tbody.govuk-table__body tr.govuk-table__row").size() mustBe 1
//    }
//
//    "render the breadcrumbs" in {
//      val doc = render()
//      doc.select(".govuk-breadcrumbs__list-item").size() must be > 0
//    }
  }

}
