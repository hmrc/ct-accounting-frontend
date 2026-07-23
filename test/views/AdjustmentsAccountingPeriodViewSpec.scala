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
import helpers.AdjustmentsDataHelper
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
import views.html.AdjustmentsAccountingPeriodView

class AdjustmentsAccountingPeriodViewSpec
    extends SpecBase
    with GuiceOneAppPerSuite
    with AdjustmentsDataHelper
    with MockitoSugar {

  private trait Setup {

    lazy val app: Application = new GuiceApplicationBuilder().build()

    implicit val request: FakeRequest[AnyContentAsEmpty.type] = FakeRequest()
    implicit val messagesApi: MessagesApi                     = app.injector.instanceOf[MessagesApi]
    implicit val messages: Messages                           = MessagesImpl(Lang.defaultLang, messagesApi)

    val view: AdjustmentsAccountingPeriodView = app.injector.instanceOf[AdjustmentsAccountingPeriodView]

    def htmlDoc(html: Html): Document = Jsoup.parse(html.toString)
  }

  "AdjustmentsAccountingPeriodView" - {

    "render the page with correct title and heading and caption" in new Setup {
      val html = view(adjustmentsViewModelWithOneItem)
      val doc  = htmlDoc(html)

      val heading = doc.select("h1.govuk-heading-l")
      val caption = doc.select("caption.govuk-table__caption")

      heading.size() mustBe 1
      heading.text() mustBe messages("adjustmentsAccountingPeriod.heading")

      caption.text() mustBe messages("adjustmentsAccountingPeriod.caption")
      doc.title() must include(messages("adjustmentsAccountingPeriod.title"))
    }

    "render the page with correct page breadcrumbs" in new Setup {
      val html = view(adjustmentsViewModelWithOneItem)
      val doc  = htmlDoc(html)

      val breadcrumbs = doc.select("li.govuk-breadcrumbs__list-item")

      breadcrumbs.size() mustBe 3

      breadcrumbs.text() must include("Corporation tax home")
      breadcrumbs.text() must include("Balance")
      breadcrumbs.text() must include("Accounting period overview")
    }

    "render the page with correct table headings" in new Setup {
      val html = view(adjustmentsViewModelWithOneItem)
      val doc  = htmlDoc(html)

      val headers = doc.select("th.govuk-table__header")

      headers.size() mustBe 2

      headers.text() must include("Description")
      headers.text() must include("Amount")
    }

    "render the page with correct table contents" in new Setup {
      val html = view(adjustmentsViewModelWithOneItem)
      val doc  = htmlDoc(html)

      val contents = doc.select("td.govuk-table__cell")

      contents.text() must include("Not currently being pursued")
      contents.text() must include("Total")
      contents.text() must include("£50.00")
    }

    "render the page with welsh toggle translation" in new Setup {
      val html = view(adjustmentsViewModelWithOneItem)
      val doc  = htmlDoc(html)

      val translation = doc.select("li.hmrc-service-navigation-language-select__list-item")

      translation.size() mustBe 2

      translation.text() must include("ENG")
      translation.text() must include("CYM")
    }

    "render the page with View Corporation Tax service name" in new Setup {
      val html = view(adjustmentsViewModelWithOneItem)
      val doc  = htmlDoc(html)

      val banner = doc.select("div.govuk-service-navigation__container")

      banner.text() mustBe messages("service.name")
    }

  }
}
