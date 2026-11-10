package controllers.payments

import base.SpecBase
import helpers.payments.PaymentsOnAccountResponseHelper
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.when
import org.scalatestplus.mockito.MockitoSugar
import play.api.Application
import play.api.i18n.Messages
import play.api.inject.bind
import play.api.test.FakeRequest
import play.api.test.Helpers.*
import services.payments.PaymentsOnAccountService
import uk.gov.hmrc.http.HeaderCarrier
import viewmodels.payments.PaymentsOnAccountViewModel
import views.html.payments.PaymentsOnAccountView

import scala.concurrent.Future

class PaymentsOnAccountControllerSpec extends SpecBase with MockitoSugar with PaymentsOnAccountResponseHelper {

  private trait Fixture {
    implicit val hc: HeaderCarrier            = HeaderCarrier()
    val mockService: PaymentsOnAccountService = mock[PaymentsOnAccountService]
    val application: Application              =
      applicationBuilder()
        .overrides(bind[PaymentsOnAccountService].toInstance(mockService))
        .build()

    implicit val msgs: Messages = messages(application)
  }

  "PaymentsOnAccountControler" - {

    "must return OK, with Payments On Account correct view showing a single record" in new Fixture {
      when(mockService.getPaymentsOnAccount(any())(any[HeaderCarrier]))
        .thenReturn(Future.successful(paymentsOnAccountSingleList))

      running(application) {
        val request   = FakeRequest(GET, routes.PaymentsOnAccountController.onPageLoad(Some(1)).url)
        val result    = route(application, request).value
        val viewModel = PaymentsOnAccountViewModel.toViewModel(
          paymentsOnAccountSingleList,
          Some(1),
          pageUrl = pageNumber => routes.PaymentsOnAccountController.onPageLoad(Some(1)).url
        )
        val view      = application.injector.instanceOf[PaymentsOnAccountView]

        status(result) mustEqual OK
        contentAsString(result) mustEqual
          view(viewModel)(
            request,
            messages(application)
          ).toString
      }
    }

    "must redirect when exception occurs from BE occurs" in new Fixture {
      when(mockService.getPaymentsOnAccount(any())(any[HeaderCarrier]))
        .thenReturn(Future.failed(new RuntimeException("Error")))

      running(application) {
        val request = FakeRequest(GET, routes.PaymentsOnAccountController.onPageLoad(Some(1)).url)
        val result  = route(application, request).value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual controllers.routes.JourneyRecoveryController.onPageLoad().url
      }
    }

  }
}
