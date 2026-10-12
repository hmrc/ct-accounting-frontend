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

package services

import connectors.AccountPositionConnector
import helpers.AccountPositionHelper
import models.AccountPositionResponse
import org.mockito.ArgumentMatchers.{any, eq as eqTo}
import org.mockito.Mockito.{times, verify, when}
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.mockito.MockitoSugar
import play.api.mvc.ControllerComponents
import play.api.test.Helpers.stubControllerComponents
import uk.gov.hmrc.http.HeaderCarrier

import scala.concurrent.{ExecutionContext, Future}

class AccountingPositionServiceSpec
    extends AnyWordSpec
    with Matchers
    with ScalaFutures
    with MockitoSugar
    with AccountPositionHelper {

  private trait BaseSetup {
    implicit val hc: HeaderCarrier = HeaderCarrier()

    private val cc: ControllerComponents = stubControllerComponents()
    implicit val ec: ExecutionContext    = cc.executionContext

    val mockConnector: AccountPositionConnector = mock[AccountPositionConnector]
    val service                                 = new AccountPositionService(mockConnector)
    val taxPayerReference: Long                 = 1L
  }

  "AccountPositionService.getAccountPosition" should {

    "delegate to connector and successfully return AccountPositionResponse" in new BaseSetup {

      when(mockConnector.getAccountPosition(eqTo(taxPayerReference))(any[HeaderCarrier]))
        .thenReturn(Future.successful(accountPositionResponse))

      val result: AccountPositionResponse =
        service.getAccountPosition(taxPayerReference).futureValue

      result shouldBe accountPositionResponse

      verify(mockConnector).getAccountPosition(taxPayerReference)

      verify(mockConnector, times(1)).getAccountPosition(taxPayerReference)

    }

    "propagate any errors or exceptions from connector" in new BaseSetup {

      when(mockConnector.getAccountPosition(eqTo(taxPayerReference))(any[HeaderCarrier]))
        .thenReturn(Future.failed(new RuntimeException("boom")))

      val ex: RuntimeException = intercept[RuntimeException] {
        service.getAccountPosition(taxPayerReference).futureValue
      }

      ex.getMessage should include("boom")

      verify(mockConnector, times(1)).getAccountPosition(taxPayerReference)

    }

  }

}
