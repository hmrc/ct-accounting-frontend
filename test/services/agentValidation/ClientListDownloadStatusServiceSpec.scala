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

package services.agentValidation

import connectors.agentValidation.ClientListDownloadStatusConnector
import models.agentValidation.ClientListDownloadStatusTransformed.{Failed, InProgress, InitiateDownload, Succeeded}
import models.agentValidation.{ClientListDownloadStatus, ClientListDownloadStatusTransformed}
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.{times, verify, when}
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.mockito.MockitoSugar
import play.api.mvc.ControllerComponents
import play.api.test.Helpers.stubControllerComponents
import uk.gov.hmrc.http.HeaderCarrier

import scala.concurrent.{ExecutionContext, Future}

class ClientListDownloadStatusServiceSpec extends AnyWordSpec with Matchers with ScalaFutures with MockitoSugar {

  private trait BaseSetup {
    implicit val hc: HeaderCarrier = HeaderCarrier()

    private val cc: ControllerComponents = stubControllerComponents()
    implicit val ec: ExecutionContext    = cc.executionContext

    val mockConnector: ClientListDownloadStatusConnector = mock[ClientListDownloadStatusConnector]
    val service                                          = new ClientListDownloadStatusService(mockConnector)
  }

  "ClientListDownloadStatusService.getClientListDownloadStatus" should {

    "delegate to connector and successfully return InitiateDownload when the ClientListDownloadStatus(result = -1)" in new BaseSetup {

      val clientListDownloadStatus: ClientListDownloadStatus = ClientListDownloadStatus(result = -1)

      when(mockConnector.getDownloadStatus(any[HeaderCarrier]))
        .thenReturn(Future.successful(clientListDownloadStatus))

      val result: ClientListDownloadStatusTransformed = service.getClientListDownloadStatus.futureValue

      result shouldBe InitiateDownload

      verify(mockConnector).getDownloadStatus

      verify(mockConnector, times(1)).getDownloadStatus

    }
    "delegate to connector and successfully return InitiateDownload when the ClientListDownloadStatus(result = 0)" in new BaseSetup {

      val clientListDownloadStatus: ClientListDownloadStatus = ClientListDownloadStatus(result = 0)

      when(mockConnector.getDownloadStatus(any[HeaderCarrier]))
        .thenReturn(Future.successful(clientListDownloadStatus))

      val result: ClientListDownloadStatusTransformed = service.getClientListDownloadStatus.futureValue

      result shouldBe InProgress

      verify(mockConnector).getDownloadStatus

      verify(mockConnector, times(1)).getDownloadStatus

    }
    "delegate to connector and successfully return InitiateDownload when the ClientListDownloadStatus(result = 1)" in new BaseSetup {

      val clientListDownloadStatus: ClientListDownloadStatus = ClientListDownloadStatus(result = 1)

      when(mockConnector.getDownloadStatus(any[HeaderCarrier]))
        .thenReturn(Future.successful(clientListDownloadStatus))

      val result: ClientListDownloadStatusTransformed = service.getClientListDownloadStatus.futureValue

      result shouldBe Succeeded

      verify(mockConnector).getDownloadStatus

      verify(mockConnector, times(1)).getDownloadStatus

    }
    "delegate to connector and successfully return InitiateDownload when the ClientListDownloadStatus(result = 2)" in new BaseSetup {

      val clientListDownloadStatus: ClientListDownloadStatus = ClientListDownloadStatus(result = 2)

      when(mockConnector.getDownloadStatus(any[HeaderCarrier]))
        .thenReturn(Future.successful(clientListDownloadStatus))

      val result: ClientListDownloadStatusTransformed = service.getClientListDownloadStatus.futureValue

      result shouldBe Failed

      verify(mockConnector).getDownloadStatus

      verify(mockConnector, times(1)).getDownloadStatus

    }
    "throw a Runtime exception for unknown  ClientListDownloadStatus" in new BaseSetup {

      val clientListDownloadStatus: ClientListDownloadStatus = ClientListDownloadStatus(result = 6)

      when(mockConnector.getDownloadStatus(any[HeaderCarrier]))
        .thenReturn(Future.successful(clientListDownloadStatus))

      val ex: RuntimeException = intercept[RuntimeException] {
        service.getClientListDownloadStatus.futureValue
      }

      ex.getMessage should include("Unknown status")

      verify(mockConnector, times(1)).getDownloadStatus

    }

    "propagate any errors or exceptions from connector" in new BaseSetup {

      when(mockConnector.getDownloadStatus(any[HeaderCarrier]))
        .thenReturn(Future.failed(new RuntimeException("boom")))

      val ex: RuntimeException = intercept[RuntimeException] {
        service.getClientListDownloadStatus.futureValue
      }

      ex.getMessage should include("boom")

      verify(mockConnector, times(1)).getDownloadStatus

    }

  }

}
