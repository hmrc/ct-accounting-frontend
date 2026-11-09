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
import helpers.payments.PaymentResultsHelper
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
import viewmodels.payments.PaymentResultsViewModel
import views.html.payments.PaymentResultsView

class PaymentResultsViewSpec extends SpecBase with GuiceOneAppPerSuite with PaymentResultsHelper with MockitoSugar {

  private trait Setup {

    lazy val app: Application = new GuiceApplicationBuilder().build()

    implicit val request: FakeRequest[AnyContentAsEmpty.type] = FakeRequest()
    implicit val messagesApi: MessagesApi                     = app.injector.instanceOf[MessagesApi]
    implicit val messages: Messages                           = MessagesImpl(Lang.defaultLang, messagesApi)

    val view: PaymentResultsView = app.injector.instanceOf[PaymentResultsView]

    def htmlDoc(html: Html): Document = Jsoup.parse(html.toString)

    val taxRef: Long                   = 1111444444L
    val userEnteredPaymentAmountString = "£38,000.00"
    val fromDateString: String         = "01 Jan 2006"
    val toDateString: String           = "01 Jan 2008"
    val page                           = 1
    val pageUrl: Int => String         = page => s"/ct-accounting/find-your-payment/payment-results?page=$page"
  }

  "PaymentResultsView" - {

    "render the page with correct title and heading and caption" in new Setup {
      val vm: PaymentResultsViewModel =
        paymentSearchResultToVM(multiplePaymentSearchResult, taxRef, pageUrl, Some(page))
      val html: Html                  = view(
        vm,
        userEnteredPaymentAmountString,
        multiplePaymentSearchResult.paymentSearchResult.size,
        Some(fromDateString),
        Some(toDateString)
      )
      val doc: Document               = htmlDoc(html)

      val heading = doc.select("h1.govuk-heading-l")

      heading.text() mustBe messages("paymentResults.heading")
      doc.title() must include(messages("paymentResults.title"))
    }

    "render the page with correct page breadcrumbs" in new Setup {
      val vm: PaymentResultsViewModel =
        paymentSearchResultToVM(multiplePaymentSearchResult, taxRef, pageUrl, Some(page))
      val html: Html                  = view(
        vm,
        userEnteredPaymentAmountString,
        multiplePaymentSearchResult.paymentSearchResult.size,
        Some(fromDateString),
        Some(toDateString)
      )
      val doc: Document               = htmlDoc(html)

      val breadcrumbs = doc.select("li.govuk-breadcrumbs__list-item")

      breadcrumbs.size() mustBe 2

      breadcrumbs.text() must include(messages("breadcrumbs.home"))
      breadcrumbs.text() must include(messages("breadcrumbs.findYourPayment"))
    }

    "render the page with correct paragraphs when paginated" in new Setup {
      val vm: PaymentResultsViewModel =
        paymentSearchResultToVM(multiplePaymentSearchResult, taxRef, pageUrl, Some(page))
      val html: Html                  = view(
        vm,
        userEnteredPaymentAmountString,
        multiplePaymentSearchResult.paymentSearchResult.size,
        Some(fromDateString),
        Some(toDateString)
      )
      val doc: Document               = htmlDoc(html)

      val paragraphs = doc.select("p.govuk-body")

      paragraphs.size() mustBe 2

      paragraphs.text() must include(
        messages(
          "paymentResults.multipleResultsPara",
          multiplePaymentSearchResult.paymentSearchResult.size,
          userEnteredPaymentAmountString,
          fromDateString,
          toDateString
        )
      )
      paragraphs.text() must include(messages("Displaying 1 to 10 of 15 records"))
    }

    "render the page with correct paragraphs when single result" in new Setup {
      val vm: PaymentResultsViewModel = paymentSearchResultToVM(singlePaymentSearchResult, taxRef, pageUrl, Some(page))
      val html: Html                  = view(
        vm,
        userEnteredPaymentAmountString,
        singlePaymentSearchResult.paymentSearchResult.size,
        Some(fromDateString),
        Some(toDateString)
      )
      val doc: Document               = htmlDoc(html)

      val heading    = doc.select("h1.govuk-heading-l")
      val paragraphs = doc.select("p.govuk-body")

      paragraphs.size() mustBe 1
      heading.text() mustBe messages("paymentResults.heading")
      paragraphs.text() must include(
        messages(
          "paymentResults.singleResultPara",
          singlePaymentSearchResult.paymentSearchResult.size,
          userEnteredPaymentAmountString,
          fromDateString,
          toDateString
        )
      )
      paragraphs.text() must not include (messages("Displaying"))
    }

    "render the page with correct paragraphs when no results" in new Setup {
      val vm: PaymentResultsViewModel = paymentSearchResultToVM(emptyPaymentSearchResult, taxRef, pageUrl, Some(page))
      val html: Html                  = view(
        vm,
        userEnteredPaymentAmountString,
        emptyPaymentSearchResult.paymentSearchResult.size,
        Some(fromDateString),
        Some(toDateString)
      )
      val doc: Document               = htmlDoc(html)

      val heading    = doc.select("h1.govuk-heading-l")
      val paragraphs = doc.select("p.govuk-body")

      paragraphs.size() mustBe 1

      heading.text()    must include(messages("paymentResults.noResultsHeading"))
      paragraphs.text() must include(messages("paymentResults.noResultsPara"))
    }

    "render the page with correct table contents for Payment Results" in new Setup {
      val vm: PaymentResultsViewModel = paymentSearchResultToVM(singlePaymentSearchResult, taxRef, pageUrl, Some(page))
      val html: Html                  = view(
        vm,
        userEnteredPaymentAmountString,
        singlePaymentSearchResult.paymentSearchResult.size,
        Some(fromDateString),
        Some(toDateString)
      )
      val doc: Document               = htmlDoc(html)

      val tableHeader = doc.select("th.govuk-table__header")
      val contents    = doc.select("td.govuk-table__cell")
      val pagination  = doc.select("ul.govuk-pagination__list")

      tableHeader.text() must include(messages("paymentResults.dateCol"))
      contents.text()    must include("14 Jul 2007")

      tableHeader.text() must include(messages("paymentResults.descriptionCol"))
      contents.text()    must include("Electronic payment")
      pagination.size mustBe 0
      tableHeader.text() must include(messages("paymentResults.amountCol"))
      contents.text()    must include("£38,000.00")

    }

    "render the page with pagination for payment results > 10" in new Setup {
      val vm: PaymentResultsViewModel =
        paymentSearchResultToVM(multiplePaymentSearchResult, taxRef, pageUrl, Some(page))
      val html: Html                  = view(
        vm,
        userEnteredPaymentAmountString,
        multiplePaymentSearchResult.paymentSearchResult.size,
        Some(fromDateString),
        Some(toDateString)
      )
      val doc: Document               = htmlDoc(html)

      val pagination     = doc.select("ul.govuk-pagination__list")
      val paginationInfo = doc.select("div.govuk-pagination__next a")

      pagination.size mustBe 1
      paginationInfo.attr("href") mustBe "/ct-accounting/find-your-payment/payment-results?page=2"
    }

    "render the page without pagination and show no table when there are no payment results" in new Setup {
      val vm: PaymentResultsViewModel = paymentSearchResultToVM(emptyPaymentSearchResult, taxRef, pageUrl, Some(page))
      val html: Html                  = view(
        vm,
        userEnteredPaymentAmountString,
        emptyPaymentSearchResult.paymentSearchResult.size,
        Some(fromDateString),
        Some(toDateString)
      )
      val doc: Document               = htmlDoc(html)

      val pagination = doc.select("ul.govuk-pagination__list")
      val paragraphs = doc.select("p.govuk-body")

      pagination.size mustBe 0
      html.toString must not include "table"

    }

    "render the page with welsh toggle translation" in new Setup {
      val vm: PaymentResultsViewModel = paymentSearchResultToVM(emptyPaymentSearchResult, taxRef, pageUrl, Some(page))
      val html: Html                  = view(
        vm,
        userEnteredPaymentAmountString,
        emptyPaymentSearchResult.paymentSearchResult.size,
        Some(fromDateString),
        Some(toDateString)
      )
      val doc: Document               = htmlDoc(html)

      val translation = doc.select("li.hmrc-service-navigation-language-select__list-item")

      translation.size() mustBe 2

      translation.text() must include("ENG")
      translation.text() must include("CYM")
    }

    "render the page with View Corporation Tax service name" in new Setup {
      val vm: PaymentResultsViewModel =
        paymentSearchResultToVM(multiplePaymentSearchResult, taxRef, pageUrl, Some(page))
      val html: Html                  = view(
        vm,
        userEnteredPaymentAmountString,
        multiplePaymentSearchResult.paymentSearchResult.size,
        Some(fromDateString),
        Some(toDateString)
      )
      val doc: Document               = htmlDoc(html)

      val banner = doc.select("div.govuk-service-navigation__container")

      banner.text() mustBe messages("service.name")
    }

  }
}
