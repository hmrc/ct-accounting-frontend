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

package controllers.clients

import base.SpecBase
import controllers.actions.FakeAgentIdentifierAction
import play.api.test.FakeRequest
import play.api.test.Helpers.*

class ClientDeleteConfirmationControllerSpec extends SpecBase {

  private def agentApplication =
    applicationBuilder(identifier = classOf[FakeAgentIdentifierAction]).build()

  "ClientDeleteConfirmation Controller" - {

    "when the user is an agent" - {

      "must return OK and show the client identifier for a GET" in {
        val application = agentApplication

        running(application) {
          val request = FakeRequest(GET, routes.ClientDeleteConfirmationController.onPageLoad("ACY-001").url)

          val result = route(application, request).value
          val page   = contentAsString(result)

          status(result) mustEqual OK
          page must include("Client deleted")
          page must include("You have deleted the client ACY-001")
          page must include("It may take up to 4 days for the change to appear.")
          page must include("View clients")
        }
      }

      "must HTML-escape the client identifier" in {
        val application = agentApplication

        running(application) {
          val request = FakeRequest(GET, routes.ClientDeleteConfirmationController.onPageLoad("ACY & Co").url)

          val result = route(application, request).value

          status(result) mustEqual OK
          contentAsString(result) must include("ACY &amp; Co")
        }
      }
    }

    "when the user is not an agent" - {

      "must redirect to UnauthorisedController" in {
        // default FakeIdentifierAction has no agent reference
        val application = applicationBuilder().build()

        running(application) {
          val request = FakeRequest(GET, routes.ClientDeleteConfirmationController.onPageLoad("ACY-001").url)

          val result = route(application, request).value

          status(result) mustEqual SEE_OTHER
          redirectLocation(result).value mustEqual controllers.routes.UnauthorisedController.onPageLoad().url
        }
      }
    }
  }
}
