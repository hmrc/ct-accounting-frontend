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
import connectors.AccountPositionConnector
import helpers.AccountPositionHelper
import org.mockito.Mockito.when
import org.scalatestplus.mockito.MockitoSugar
import play.api.i18n.{Lang, Messages, MessagesApi, MessagesImpl}
import play.api.test.FakeRequest
import play.api.test.Helpers.*
import views.html.AccountingPeriodsView
import uk.gov.hmrc.http.HeaderCarrier
import org.mockito.ArgumentMatchers.{any, eq as eqTo}
import play.api.inject.bind

import scala.concurrent.Future

class AccountingPeriodsControllerSpec extends SpecBase with MockitoSugar with AccountPositionHelper {
  implicit val hc: HeaderCarrier = HeaderCarrier()
  val mockConnector: AccountPositionConnector = mock[AccountPositionConnector]
  
  val application = applicationBuilder()
    .overrides(bind[AccountPositionConnector].toInstance(mockConnector))
    .build()

  implicit val messagesApi: MessagesApi = application.injector.instanceOf[MessagesApi]
  implicit val messages: Messages = MessagesImpl(Lang.defaultLang, messagesApi)


  "AccountingPeriods Controller" - {

    "must return OK and the correct view for a GET" in {
      
      when(mockConnector.getAccountPosition(eqTo(5L))(any[HeaderCarrier]))
        .thenReturn(Future.successful(accountPositionResponse))

      running(application) {
        val request = FakeRequest(GET, routes.AccountingPeriodsController.onPageLoad().url)

        val result = route(application, request).value

        val view = application.injector.instanceOf[AccountingPeriodsView]
        
        status(result) mustEqual OK
        contentAsString(result) mustEqual view(viewModel)(request, messages(application)).toString
      }
    }

    "must redirect when exception from BE occurs" in {

      when(mockConnector.getAccountPosition(eqTo(5L))(any[HeaderCarrier]))
        .thenReturn(Future.failed(RuntimeException("Error")))

      val testApplication = applicationBuilder()
        .overrides(bind[AccountPositionConnector].toInstance(mockConnector))
        .build()

      running(testApplication) {
        val request = FakeRequest(GET, routes.AccountingPeriodsController.onPageLoad().url)
        val result = route(testApplication, request).value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual routes.JourneyRecoveryController.onPageLoad().url
      }

    }
  }
}
