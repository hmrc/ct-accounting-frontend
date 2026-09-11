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
import helpers.DebitInterestHelper
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
import views.html.DebitInterestAccountingPeriodView

class DebitInterestAccountingPeriodViewSpec
    extends SpecBase
    with GuiceOneAppPerSuite
    with MockitoSugar
    with DebitInterestHelper {

  private trait Setup {

    lazy val app: Application = new GuiceApplicationBuilder().build()

    implicit val request: FakeRequest[AnyContentAsEmpty.type] = FakeRequest()
    implicit val messagesApi: MessagesApi                     = app.injector.instanceOf[MessagesApi]
    implicit val messages: Messages                           = MessagesImpl(Lang.defaultLang, messagesApi)

    val view: DebitInterestAccountingPeriodView = app.injector.instanceOf[DebitInterestAccountingPeriodView]

    def htmlDoc(html: Html): Document = Jsoup.parse(html.toString)
  }

  "DebitInterestAccountingPeriodView" - {

    "render the page with correct title and heading and caption" in new Setup {
      val html = view(defaultViewModel)
      val doc  = htmlDoc(html)

      val heading = doc.select("h1.govuk-heading-l")
      val caption = doc.select("caption.govuk-table__caption")

      heading.size() mustBe 1
      heading.text() mustBe messages("debitInterestAccountingPeriod.heading")

      caption.text() must startWith(messages("debitInterestAccountingPeriod.caption"))
      doc.title()    must startWith(messages("debitInterestAccountingPeriod.title"))
    }

    "render the page with correct page breadcrumbs" in new Setup {
      val html = view(defaultViewModel)
      val doc  = htmlDoc(html)

      val breadcrumbs = doc.select("li.govuk-breadcrumbs__list-item")

      breadcrumbs.size() mustBe 4

      breadcrumbs.text() must include(messages("breadcrumbs.home"))
      breadcrumbs.text() must include(messages("breadcrumbs.accountingPeriods"))
      breadcrumbs.text() must include(messages("breadcrumbs.accountingPeriodEnding"))
      breadcrumbs.text() must include(messages("breadcrumbs.debitInterest"))
    }

    "render the page with correct table headings" in new Setup {
      val html = view(defaultViewModel)
      val doc  = htmlDoc(html)

      val headers = doc.select("th.govuk-table__header")

      headers.size() mustBe 6

      headers.text() must include(messages("debitInterestAccountingPeriod.tableHeader.interestAmount"))
      headers.text() must include(messages("debitInterestAccountingPeriod.tableHeader.fromDate"))
      headers.text() must include(messages("debitInterestAccountingPeriod.tableHeader.toDate"))

      headers.text() must include(messages("debitInterestAccountingPeriod.tableHeader.daysOvd"))
      headers.text() must include(messages("debitInterestAccountingPeriod.tableHeader.aprc"))
      headers.text() must include(messages("debitInterestAccountingPeriod.tableHeader.interestAcc"))
    }

    "render the page with correct table contents" in new Setup {
      val html = view(defaultViewModel)
      val doc  = htmlDoc(html)

      val contents = doc.select("td.govuk-table__cell")

      contents.text() must include(messages("debitInterestAccountingPeriod.total"))
      contents.text() must include("£17.01")
    }

    "render the page with welsh toggle translation" in new Setup {
      val html = view(defaultViewModel)
      val doc  = htmlDoc(html)

      val translation = doc.select("li.hmrc-service-navigation-language-select__list-item")

      translation.size() mustBe 2

      translation.text() must include("ENG")
      translation.text() must include("CYM")
    }

    "render the page with View Corporation Tax service name" in new Setup {
      val html = view(defaultViewModel)
      val doc  = htmlDoc(html)

      val banner = doc.select("div.govuk-service-navigation__container")

      banner.text() mustBe messages("service.name")
    }

  }
}
