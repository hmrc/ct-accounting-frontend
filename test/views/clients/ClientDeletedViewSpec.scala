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

package views.clients

import base.SpecBase
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import play.api.Application
import play.api.i18n.{Lang, Messages, MessagesApi, MessagesImpl}
import play.api.test.FakeRequest
import views.html.clients.ClientDeletedView

class ClientDeletedViewSpec extends SpecBase {

  val application: Application = applicationBuilder().build()
  val view: ClientDeletedView  = application.injector.instanceOf[ClientDeletedView]

  implicit val messagesApi: MessagesApi = application.injector.instanceOf[MessagesApi]
  implicit val messages: Messages       = MessagesImpl(Lang.defaultLang, messagesApi)

  implicit val request: FakeRequest[_] = FakeRequest()

  private val clientId      = "ACY-001"
  private val clientListUrl = "/clients/clientList" // TODO placeholder, update when the real route exists

  def render(id: String): Document =
    Jsoup.parse(view(id)(request, messages).toString)

  "ClientDeletedView" - {

    "render the page title" in {
      val doc = render(clientId)

      doc.title() must include(messages("clientDeleted.title"))
    }

    "render the confirmation heading" in {
      val doc = render(clientId)

      doc.select("h1").text() must include(messages("clientDeleted.heading"))
    }

    "render the confirmation message with the client id" in {
      val doc = render(clientId)

      doc.body().text() must include(messages("clientDeleted.panel.body", clientId))
    }

    "render the what happens next heading" in {
      val doc = render(clientId)

      doc.select("h2").text() must include(messages("clientDeleted.whatHappensNext"))
    }

    "render the delay message" in {
      val doc = render(clientId)

      doc.body().text() must include(messages("clientDeleted.delay"))
    }

    "render a link back to the client list" in {
      val link = render(clientId).select(s"a[href=$clientListUrl]")

      link.size() mustBe 1
      link.text() mustBe messages("clientDeleted.backToClients")
    }

    "escape the client id" in {
      val doc = render("<script>alert(1)</script>")

      doc.select("script").text() mustNot include("alert(1)")
      doc.body().text() must include("<script>alert(1)</script>")
    }
  }
}
