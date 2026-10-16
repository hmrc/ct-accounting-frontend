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
        val result = route(application, request).value

        status(result) mustEqual OK
        contentAsString(result) must include("Find your payment")
      }
    }
  }

  "must display the amount field and its hint for a GET" in {
    val application = applicationBuilder().build()

    running(application) {
      val request =
        FakeRequest(GET, routes.FindYourPaymentController.onPageLoad().url)

      val result = route(application, request).value
      val page = contentAsString(result)

      status(result) mustEqual OK
      page must include("Amount")
      page must include("Enter the exact amount, including pence. For example, 2020.67")
      page must include("include pence value exactly as shown")
      page must include("do not include currency symbol")
    }
  }

}


