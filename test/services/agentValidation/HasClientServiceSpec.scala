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

import connectors.agentValidation.HasClientConnector
import models.agentValidation.HasClient
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

class HasClientServiceSpec extends AnyWordSpec with Matchers with ScalaFutures with MockitoSugar {

  private trait BaseSetup {
    implicit val hc: HeaderCarrier = HeaderCarrier()

    private val cc: ControllerComponents = stubControllerComponents()
    implicit val ec: ExecutionContext    = cc.executionContext

    val mockConnector: HasClientConnector = mock[HasClientConnector]
    val service                           = new HasClientService(mockConnector)
  }

  "HasClientService.getHasClient" should {

    "delegate to connector and successfully return true status" in new BaseSetup {

      val hasClient: HasClient = HasClient(status = true)

      when(mockConnector.hasClient(any())(any[HeaderCarrier]))
        .thenReturn(Future.successful(hasClient))

      val result: HasClient = service.getHasClient(7363L).futureValue

      result shouldBe hasClient

      verify(mockConnector).hasClient(any())(any[HeaderCarrier])

      verify(mockConnector, times(1)).hasClient(any())(any[HeaderCarrier])

    }
    "delegate to connector and successfully return false status" in new BaseSetup {

      val hasClient: HasClient = HasClient(status = false)

      when(mockConnector.hasClient(any())(any[HeaderCarrier]))
        .thenReturn(Future.successful(hasClient))

      val result: HasClient = service.getHasClient(363L).futureValue

      result shouldBe hasClient

      verify(mockConnector).hasClient(any())(any[HeaderCarrier])

      verify(mockConnector, times(1)).hasClient(any())(any[HeaderCarrier])

    }

    "propagate any errors or exceptions from connector" in new BaseSetup {

      when(mockConnector.hasClient(any())(any[HeaderCarrier]))
        .thenReturn(Future.failed(new RuntimeException("Boom")))

      val ex: RuntimeException = intercept[RuntimeException] {
        service.getHasClient(141351L).futureValue
      }

      ex.getMessage should include("Boom")

      verify(mockConnector, times(1)).hasClient(any())(any[HeaderCarrier])

    }

  }

}
