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

package controllers

import base.SpecBase
import connectors.AccountingPeriodOverviewConnector
import helpers.AccountingPeriodOverviewHelper
import org.mockito.ArgumentMatchers.{any, eq as eqTo}
import org.mockito.Mockito.when
import org.scalatestplus.mockito.MockitoSugar
import play.api.inject.bind
import play.api.test.FakeRequest
import play.api.test.Helpers.*
import uk.gov.hmrc.http.HeaderCarrier
import views.html.accountingPeriods.AccountingPeriodsOverviewView
import play.api.i18n.{Lang, Messages, MessagesApi, MessagesImpl}

import scala.concurrent.Future

class AccountingPeriodOverviewControllerSpec extends SpecBase with MockitoSugar with AccountingPeriodOverviewHelper {
  implicit val hc: HeaderCarrier                       = HeaderCarrier()
  val mockConnector: AccountingPeriodOverviewConnector = mock[AccountingPeriodOverviewConnector]

  val application = applicationBuilder()
    .overrides(bind[AccountingPeriodOverviewConnector].toInstance(mockConnector))
    .build()

  implicit val messagesApi: MessagesApi = application.injector.instanceOf[MessagesApi]
  implicit val messages: Messages       = MessagesImpl(Lang.defaultLang, messagesApi)

  // TODO: hardcoded value in the controller until it's wired up to session data

  "AccountingPeriodOverview Controller" - {

    "must return OK and the correct accounting period overview view for a GET" in {

      when(mockConnector.getAccountingPeriodOverview(eqTo(1L), eqTo(1L))(any[HeaderCarrier]))
        .thenReturn(Future.successful(accountingPeriodOverviewResponse))

      val testApplication = applicationBuilder()
        .overrides(bind[AccountingPeriodOverviewConnector].toInstance(mockConnector))
        .build()

      running(testApplication) {
        val request = FakeRequest(GET, routes.AccountingPeriodOverviewController.onPageLoad().url)
        val result  = route(application, request).value
        val view    = application.injector.instanceOf[AccountingPeriodsOverviewView]

        status(result) mustEqual OK
        contentAsString(result) mustEqual
          view(viewModel)(
            request,
            messages(application)
          ).toString
      }
    }

    "must redirect when exception from BE occurs" in {

      when(mockConnector.getAccountingPeriodOverview(eqTo(1L), eqTo(1L))(any[HeaderCarrier]))
        .thenReturn(Future.failed(RuntimeException("Error")))

      val testApplication = applicationBuilder()
        .overrides(bind[AccountingPeriodOverviewConnector].toInstance(mockConnector))
        .build()

      running(testApplication) {
        val request = FakeRequest(GET, routes.AccountingPeriodOverviewController.onPageLoad().url)
        val result  = route(application, request).value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual routes.JourneyRecoveryController.onPageLoad().url
      }

    }
  }
}
