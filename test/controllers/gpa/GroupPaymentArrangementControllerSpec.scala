package controllers.gpa

import base.SpecBase
import connectors.AccountingPeriodOverviewConnector
import helpers.gpa.GroupPaymentArrangementHelper
import org.mockito.ArgumentMatchers.{any, eq as eqTo}
import org.mockito.Mockito.when
import org.scalatestplus.mockito.MockitoSugar
import play.api.inject.bind
import play.api.test.FakeRequest
import play.api.test.Helpers.*
import uk.gov.hmrc.http.HeaderCarrier
import views.html.gpa.GroupPaymentArrangementView
import play.api.i18n.{Lang, Messages, MessagesApi, MessagesImpl}

import scala.concurrent.Future

class GroupPaymentArrangementControllerSpec extends SpecBase with MockitoSugar with GroupPaymentArrangementHelper {
  implicit val hc: HeaderCarrier                       = HeaderCarrier()
  val mockConnector: AccountingPeriodOverviewConnector = mock[AccountingPeriodOverviewConnector]

  val application = applicationBuilder()
    .overrides(bind[AccountingPeriodOverviewConnector].toInstance(mockConnector))
    .build()

  implicit val messagesApi: MessagesApi = application.injector.instanceOf[MessagesApi]
  implicit val messages: Messages       = MessagesImpl(Lang.defaultLang, messagesApi)

  // TODO: hardcoded value in the controller until it's wired up to session data

  "GroupPaymentArrangement Controller" - {

    "must return OK and the correct accounting period overview view for a GET" in {

      when(mockConnector.getAccountingPeriodOverview(eqTo(1L), eqTo(1L))(any[HeaderCarrier]))
        .thenReturn(Future.successful(groupPaymentArrangementViewModel))

      val testApplication = applicationBuilder()
        .overrides(bind[AccountingPeriodOverviewConnector].toInstance(mockConnector))
        .build()

      running(testApplication) {
        val request = FakeRequest(GET, controllers.gpa.routes.GroupPaymentArrangementController.onPageLoad().url)
        val result  = route(application, request).value
        val view    = application.injector.instanceOf[GroupPaymentArrangementView]

        status(result) mustEqual OK
        contentAsString(result) mustEqual
          view(groupPaymentArrangementViewModel)(
            request,
            messages(application)
          ).toString
      }
    }
    
  }
}
