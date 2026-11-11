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
import helpers.payments.PaymentAllocationHelper
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
import views.html.payments.PaymentAllocationView

class PaymentAllocationViewSpec
    extends SpecBase
    with GuiceOneAppPerSuite
    with PaymentAllocationHelper
    with MockitoSugar {

  private trait Setup {

    lazy val app: Application = new GuiceApplicationBuilder().build()

    implicit val request: FakeRequest[AnyContentAsEmpty.type] = FakeRequest()
    implicit val messagesApi: MessagesApi                     = app.injector.instanceOf[MessagesApi]
    implicit val messages: Messages                           = MessagesImpl(Lang.defaultLang, messagesApi)

    val view: PaymentAllocationView = app.injector.instanceOf[PaymentAllocationView]

    def htmlDoc(html: Html): Document = Jsoup.parse(html.toString)

    val page                   = 1
    val pageUrl: Int => String = page =>
      s"/ct-accounting/find-your-payment/payment-results/payment-allocation?page=$page"
  }

  "PaymentAllocationView" - {

    "render the page with correct title and heading and caption" in new Setup {
      val vm   = paymentAllocationToVM(paymentAllocation, pageUrl)
      val html = view(vm)
      val doc  = htmlDoc(html)

      val heading = doc.select("h1.govuk-heading-l")

      heading.size() mustBe 1
      heading.text() mustBe messages("paymentAllocation.heading")
      doc.title() must include(messages("paymentAllocation.title"))
    }

    "render the page with correct page breadcrumbs" in new Setup {
      val vm   = paymentAllocationToVM(paymentAllocation, pageUrl)
      val html = view(vm)
      val doc  = htmlDoc(html)

      val breadcrumbs = doc.select("li.govuk-breadcrumbs__list-item")

      breadcrumbs.size() mustBe 3

      breadcrumbs.text() must include(messages("breadcrumbs.home"))
      breadcrumbs.text() must include(messages("breadcrumbs.findYourPayment"))
      breadcrumbs.text() must include(messages("breadcrumbs.paymentResults"))
    }

    "render the page with correct paragraphs" in new Setup {
      val vm   = paymentAllocationToVM(paymentAllocation, pageUrl)
      val html = view(vm)
      val doc  = htmlDoc(html)

      val paragraphs = doc.select("p.govuk-body")

      paragraphs.size() mustBe 1

      paragraphs.text() must include(messages("paymentAllocation.p1"))
    }

    "render the page with correct table contents for payment allocation" in new Setup {
      val vm   = paymentAllocationToVM(paymentAllocation, pageUrl)
      val html = view(vm)
      val doc  = htmlDoc(html)

      val headers  = doc.select("th.govuk-table__header")
      val contents = doc.select("td.govuk-table__cell")

      headers.text()  must include(messages("paymentAllocation.paymentAllocationDate"))
      contents.text() must include("10")

      headers.text()  must include(messages("paymentAllocation.accountingPeriodEnding"))
      contents.text() must include("£10.00")

      headers.text()  must include(messages("paymentAllocation.amount"))
      contents.text() must include("£10.00")

    }

    "render the page with correct table header and contents for payment allocation without pagination" in new Setup {
      val vm   = paymentAllocationToVM(nonPaginatedPaymentAllocation, pageUrl)
      val html = view(vm)
      val doc  = htmlDoc(html)

      val pagination = doc.select("ul.govuk-pagination__list")

      pagination.size mustBe 0

    }

    "render the page with pagination for payment allocation records > 10" in new Setup {
      val vm   = paymentAllocationToVM(paginatedPaymentAllocation, pageUrl)
      val html = view(vm)
      val doc  = htmlDoc(html)

      val pagination     = doc.select("ul.govuk-pagination__list")
      val paginationInfo = doc.select("div.govuk-pagination__next a")

      println(doc)

      pagination.size mustBe 1
      paginationInfo.attr("href") mustBe s"/ct-accounting/find-your-payment/payment-results/payment-allocation?page=2"

    }

    "render the page with welsh toggle translation" in new Setup {
      val vm   = paymentAllocationToVM(paymentAllocation, pageUrl)
      val html = view(vm)
      val doc  = htmlDoc(html)

      val translation = doc.select("li.hmrc-service-navigation-language-select__list-item")

      translation.size() mustBe 2

      translation.text() must include("ENG")
      translation.text() must include("CYM")
    }

    "render the page with View Corporation Tax service name" in new Setup {
      val vm   = paymentAllocationToVM(paymentAllocation, pageUrl)
      val html = view(vm)
      val doc  = htmlDoc(html)

      val banner = doc.select("div.govuk-service-navigation__container")

      banner.text() mustBe messages("service.name")
    }

  }
}
