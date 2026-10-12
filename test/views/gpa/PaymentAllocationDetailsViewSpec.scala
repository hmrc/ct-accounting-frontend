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

package views.gpa

import base.SpecBase
import helpers.gpa.PaymentAllocationDetailsHelper
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.scalatestplus.mockito.MockitoSugar
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.Application
import play.api.i18n.{Lang, Messages, MessagesApi, MessagesImpl}
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.mvc.AnyContentAsEmpty
import play.api.test.FakeRequest
import play.twirl.api.Html
import views.html.gpa.PaymentAllocationDetailsView

class PaymentAllocationDetailsViewSpec
    extends SpecBase
    with GuiceOneAppPerSuite
    with PaymentAllocationDetailsHelper
    with MockitoSugar {

  private trait Setup {

    lazy val app: Application = new GuiceApplicationBuilder().build()

    implicit val request: FakeRequest[AnyContentAsEmpty.type] = FakeRequest()
    implicit val messagesApi: MessagesApi                     = app.injector.instanceOf[MessagesApi]
    implicit val messages: Messages                           = MessagesImpl(Lang.defaultLang, messagesApi)

    val view: PaymentAllocationDetailsView = app.injector.instanceOf[PaymentAllocationDetailsView]

    def htmlDoc(html: Html): Document = Jsoup.parse(html.toString)
  }

  "PaymentAllocationDetailsView" - {

    "render the page with correct title and heading and caption" in new Setup {
      val html = view(fullPaymentAllocationDetailsVM)
      val doc  = htmlDoc(html)

      val heading = doc.select("h1.govuk-heading-l")

      heading.size() mustBe 1
      heading.text() mustBe messages("paymentAllocationDetails.heading")
      doc.title() must include(messages("paymentAllocationDetails.title"))
    }

    "render the page with correct page breadcrumbs" in new Setup {
      val html = view(fullPaymentAllocationDetailsVM)
      val doc  = htmlDoc(html)

      val breadcrumbs = doc.select("li.govuk-breadcrumbs__list-item")

      breadcrumbs.size() mustBe 3

      breadcrumbs.text() must include(messages("breadcrumbs.home"))
      breadcrumbs.text() must include(messages("breadcrumbs.gpa"))
      breadcrumbs.text() must include(messages("breadcrumbs.groupTaxes"))
    }

    "render the page with correct paragraphs" in new Setup {
      val html = view(fullPaymentAllocationDetailsVM)
      val doc  = htmlDoc(html)

      val paragraphs = doc.select("p.govuk-body")

      println(doc)

      paragraphs.size() mustBe 2

      paragraphs.text() must include(messages("paymentAllocationDetails.p1"))
      paragraphs.text() must include(messages("paymentAllocationDetails.p2"))
    }

    "render the page with correct table contents" in new Setup {
      val html = view(fullPaymentAllocationDetailsVM)
      val doc  = htmlDoc(html)

      val contents = doc.select("td.govuk-table__cell")

      println(doc)

      contents.text() must include(messages("paymentAllocationDetails.gpaReference"))
      contents.text() must include("12345")

      contents.text() must include(messages("paymentAllocationDetails.paymentAllocated"))
      contents.text() must include("-30.00")

    }

    "render the page with welsh toggle translation" in new Setup {
      val html = view(fullPaymentAllocationDetailsVM)
      val doc  = htmlDoc(html)

      val translation = doc.select("li.hmrc-service-navigation-language-select__list-item")

      translation.size() mustBe 2

      translation.text() must include("ENG")
      translation.text() must include("CYM")
    }

    "render the page with View Corporation Tax service name" in new Setup {
      val html = view(fullPaymentAllocationDetailsVM)
      val doc  = htmlDoc(html)

      val banner = doc.select("div.govuk-service-navigation__container")

      banner.text() mustBe messages("service.name")
    }

  }
}
