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

package controllers.payments

import base.SpecBase
import play.api.http.Status.OK
import play.api.test.FakeRequest
import play.api.test.Helpers.*

class FindYourPaymentControllerSpec extends SpecBase {

  "FindYourPayment Controller" - {

    "must return OK and display the find your payment page for a GET" in {
      val application = applicationBuilder().build()

      running(application) {
        val request = FakeRequest(GET, routes.FindYourPaymentController.onPageLoad().url)
        val result  = route(application, request).value
        val page = contentAsString(result)

        status(result) mustEqual OK
        page must include("Find your payment")
      }
    }
  }

  "must display the amount field and its hint for a GET" in {
    val application = applicationBuilder().build()

    running(application) {
      val request = FakeRequest(GET, routes.FindYourPaymentController.onPageLoad().url)

      val result = route(application, request).value
      val page   = contentAsString(result)

      status(result) mustEqual OK
      page must include("Amount")
      page must include("Enter the exact amount, including pence. For example, 2020.67")
      page must include("Include the pence value exactly as it appears.")
      page must include("Do not include the currency symbol.")
      page must include("Start date (optional)")
      page must include("End date (optional)")
      page must include("Find payment")
    }
  }

  "must redirect to the payment results page when the submitted form is valid" in {
    val application = applicationBuilder().build()

    running(application) {
      val request =
        FakeRequest(POST, routes.FindYourPaymentController.onSubmit().url)
          .withFormUrlEncodedBody(
            "amount" -> "1570.67",
            "startDate.day" -> "",
            "startDate.month" -> "",
            "startDate.year" -> "",
            "endDate.day" -> "",
            "endDate.month" -> "",
            "endDate.year" -> ""
          )

      val result = route(application, request).value

      status(result) mustEqual SEE_OTHER
      //TODO: replace hardcoded redirect path when payment-results route is implemented
      redirectLocation(result).value mustEqual "/find-your-payment/payment-results"
    }
  }

  "must return BAD_REQUEST and display an error when the submitted form is invalid" in {
    val application = applicationBuilder().build()

    running(application) {
      val request =
        FakeRequest(POST, routes.FindYourPaymentController.onSubmit().url)
          .withFormUrlEncodedBody(
            "amount" -> "",
            "startDate.day" -> "",
            "startDate.month" -> "",
            "startDate.year" -> "",
            "endDate.day" -> "",
            "endDate.month" -> "",
            "endDate.year" -> ""
          )

      val result = route(application, request).value
      val page = contentAsString(result)

      status(result) mustEqual BAD_REQUEST
      page must include(
        "Enter your payment amount in the correct format, like 1570.67 or 1500.00"
      )
    }
  }

}
