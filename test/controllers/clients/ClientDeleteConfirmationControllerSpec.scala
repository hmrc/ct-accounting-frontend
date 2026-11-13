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
import play.api.test.FakeRequest
import play.api.test.Helpers.*

class ClientDeleteConfirmationControllerSpec extends SpecBase {

  "ClientDeleteConfirmation Controller" - {

    "must return OK and show the client identifier for a GET" in {
      val application = applicationBuilder().build()

      running(application) {

        val request = FakeRequest(GET, routes.ClientDeleteConfirmationController.onPageLoad("ACY-001").url)

        val result = route(application, request).value
        val page   = contentAsString(result)

        status(result) mustEqual OK
        page must include("Client deleted")
        page must include("You have deleted the client ACY-001")
        page must include("It may take up to 4 days for the change to appear.")
        page must include("Your current client")
      }
    }

    "must HTML-escape the client identifier" in {
      val application = applicationBuilder().build()

      running(application) {
        val request = FakeRequest(GET, routes.ClientDeleteConfirmationController.onPageLoad("ACY & Co").url)

        val result = route(application, request).value

        status(result) mustEqual OK
        contentAsString(result) must include("ACY &amp; Co")
      }
    }

  }
}
