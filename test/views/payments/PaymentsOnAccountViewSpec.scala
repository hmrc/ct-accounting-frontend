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

package views.payments

import base.SpecBase
import controllers.payments.routes
import helpers.payments.PaymentsOnAccountResponseHelper
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import play.api.i18n.{Lang, Messages, MessagesApi, MessagesImpl}
import play.api.test.FakeRequest
import viewmodels.payments.PaymentsOnAccountViewModel
import views.html.payments.PaymentsOnAccountView
import play.api.Application

class PaymentsOnAccountViewSpec extends SpecBase with PaymentsOnAccountResponseHelper {
  val application: Application = applicationBuilder().build()

  val view: PaymentsOnAccountView = application.injector.instanceOf[PaymentsOnAccountView]

  implicit val messagesApi: MessagesApi = application.injector.instanceOf[MessagesApi]
  implicit val messages: Messages       = MessagesImpl(Lang.defaultLang, messagesApi)

  val viewModel = PaymentsOnAccountViewModel.toViewModel(
    paymentsOnAccountSingleList,
    Some(1),
    pageUrl = pageNumber => routes.PaymentsOnAccountController.onPageLoad(Some(pageNumber)).url
  )

  implicit val request: FakeRequest[_] = FakeRequest()

  def render(viewModel: PaymentsOnAccountViewModel): Document =
    Jsoup.parse(view(viewModel)(request, messages(application)).toString)

  "paymentsOnAccountView" - {

    "render the correct page title" in {
      val doc = render(viewModel)
      doc.title() mustBe "Payments on account - GOV.UK"
    }

    "render the correct heading" in {
      val doc = render(viewModel)
      doc
        .select("h1.govuk-heading-l")
        .get(0)
        .text() mustBe s"Payments on account"
    }

    "render the correct paragraph" in {
      val doc = render(viewModel)
      doc
        .select("p")
        .get(0)
        .text() mustBe s"The payment shown below have been received by HMRC. They either relate to future Corporation Tax liabilities or have been allocated to an accounting period."
    }

    "render the correct breadcrumbs" in {
      val doc         = render(viewModel)
      val breadcrumbs = doc.select("li.govuk-breadcrumbs__list-item").eachText()

      breadcrumbs.get(0) mustBe messages("Home")

      doc.select(".govuk-breadcrumbs__list-item").size() mustBe 1
    }

    "render the table caption heading" in {
      val doc = render(viewModel)
      doc
        .select("caption")
        .get(0)
        .text() mustBe s"Payment history"
    }

    "render the correct table headers. Table below Payments History caption" in {
      val doc     = render(viewModel)
      val headers = doc.select("th.govuk-table__header").eachText()
      headers must contain allOf (
        messages("Payment date"),
        messages("Payment allocation date"),
        messages("Description"),
        messages("Amount")
      )
      headers.size() mustBe 4
    }

    "render the first row for table Payments on Account" in {
      val doc      = render(viewModel)
      val firstRow =
        doc.select("tbody.govuk-table__body tr.govuk-table__row").get(0).getElementsByClass("govuk-table__cell")

      firstRow.get(0).text() mustBe "14 Jul 2025"
      firstRow.get(1).text() mustBe "31 Mar 2026"
      firstRow.get(2).text() mustBe "Electronic payment"
      firstRow.get(3).text() mustBe "£12,500.00"
    }

  }
}
