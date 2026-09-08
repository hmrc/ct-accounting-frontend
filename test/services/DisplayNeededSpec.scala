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

import connectors.DisplayNeededConnector
import helpers.DisplayNeededHelper
import org.mockito.ArgumentMatchers.{any, eq as eqTo}
import models.DisplayNeeded
import org.mockito.Mockito.{times, verify, when}
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.mockito.MockitoSugar
import play.api.mvc.ControllerComponents
import play.api.test.Helpers.stubControllerComponents
import uk.gov.hmrc.http.HeaderCarrier

import scala.concurrent.{ExecutionContext, Future}

class DisplayNeededSpec extends AnyWordSpec with Matchers with ScalaFutures with MockitoSugar with DisplayNeededHelper {

  private trait BaseSetup {
    implicit val hc: HeaderCarrier = HeaderCarrier()

    private val cc: ControllerComponents = stubControllerComponents()
    implicit val ec: ExecutionContext    = cc.executionContext

    val mockConnector: DisplayNeededConnector = mock[DisplayNeededConnector]
    val service                               = new DisplayNeededService(mockConnector)
    val taxPayerReference: Long               = 12L
    val accPeriod: Long                       = 2L
  }

  "DisplayNeededService.getDisplayNeeded" should {

    "delegate to connector and successfully return DisplayNeeded with all flags set to false" in new BaseSetup {

      when(mockConnector.getDisplayNeeded(eqTo(taxPayerReference), eqTo(accPeriod))(any[HeaderCarrier]))
        .thenReturn(Future.successful(displayNeededAllFalse))

      val result: DisplayNeeded =
        service.getDisplayNeeded(taxPayerReference, accPeriod).futureValue

      result shouldBe displayNeededAllFalse

      verify(mockConnector).getDisplayNeeded(taxPayerReference, accPeriod)

      verify(mockConnector, times(1)).getDisplayNeeded(taxPayerReference, accPeriod)

    }

    "delegate to connector and successfully return DisplayNeeded with all flags set to true" in new BaseSetup {

      when(mockConnector.getDisplayNeeded(eqTo(taxPayerReference), eqTo(accPeriod))(any[HeaderCarrier]))
        .thenReturn(Future.successful(displayNeededAllTrue))

      val result: DisplayNeeded =
        service.getDisplayNeeded(taxPayerReference, accPeriod).futureValue

      result shouldBe displayNeededAllTrue

      verify(mockConnector).getDisplayNeeded(taxPayerReference, accPeriod)

      verify(mockConnector, times(1)).getDisplayNeeded(taxPayerReference, accPeriod)

    }

    "delegate to connector and successfully return DisplayNeeded with some flags set to false or true" in new BaseSetup {

      when(mockConnector.getDisplayNeeded(eqTo(taxPayerReference), eqTo(accPeriod))(any[HeaderCarrier]))
        .thenReturn(Future.successful(displayNeededMixed))

      val result: DisplayNeeded =
        service.getDisplayNeeded(taxPayerReference, accPeriod).futureValue

      result shouldBe displayNeededMixed

      verify(mockConnector).getDisplayNeeded(taxPayerReference, accPeriod)

      verify(mockConnector, times(1)).getDisplayNeeded(taxPayerReference, accPeriod)

    }

    "propagate any errors or exceptions from connector" in new BaseSetup {

      when(mockConnector.getDisplayNeeded(eqTo(taxPayerReference), eqTo(accPeriod))(any[HeaderCarrier]))
        .thenReturn(Future.failed(new RuntimeException("Error from downstream")))

      val ex: RuntimeException = intercept[RuntimeException] {
        service.getDisplayNeeded(taxPayerReference, accPeriod).futureValue
      }

      ex.getMessage should include("Error from downstream")

      verify(mockConnector, times(1)).getDisplayNeeded(taxPayerReference, accPeriod)

    }

  }

}
