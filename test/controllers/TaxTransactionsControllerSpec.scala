package controllers

import base.SpecBase
import connectors.TaxTransactionsConnector
import org.mockito.Mockito.when
import org.scalatestplus.mockito.MockitoSugar
import play.api.inject.bind
import play.api.test.FakeRequest
import play.api.test.Helpers.*
import views.html.TaxTransactionsView
import models.TaxTransactions
import org.mockito.ArgumentMatchers.{any, eq => eqTo}
import uk.gov.hmrc.http.HeaderCarrier

import java.time.LocalDate
import scala.concurrent.Future

class TaxTransactionsControllerSpec extends SpecBase with MockitoSugar {
  implicit val hc: HeaderCarrier = HeaderCarrier()
  val mockConnector: TaxTransactionsConnector = mock[TaxTransactionsConnector]

  val emptyTaxTransactionsResponse = TaxTransactions(List.empty)

  // TODO: hardcoded value in the controller until it's wired up to session data
  val expectedAccountPeriod: LocalDate = LocalDate.of(2026, 1, 1)

  "TaxTransactions Controller" - {

    "must return OK and the correct view for a GET" in {

      when(mockConnector.getTaxTransactions(eqTo(1L), eqTo(1L))(any[HeaderCarrier]))
        .thenReturn(Future.successful(emptyTaxTransactionsResponse))

      val application = applicationBuilder()
        .overrides(bind[TaxTransactionsConnector].toInstance(mockConnector))
        .build()

      running(application) {
        val request = FakeRequest(GET, routes.TaxTransactionsController.onPageLoad().url)
        val result = route(application, request).value
        val view = application.injector.instanceOf[TaxTransactionsView]

        status(result) mustEqual OK
        contentAsString(result) mustEqual
          view(emptyTaxTransactionsResponse.taxTransactions, expectedAccountPeriod)(request, messages(application)).toString
      }
    }
  }
}
