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

import connectors.GroupPaymentsConnector
import helpers.GroupPaymentHelper
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

class GroupPaymentServiceSpec
    extends AnyWordSpec
    with Matchers
    with ScalaFutures
    with MockitoSugar
    with GroupPaymentHelper {

  private trait BaseSetup {
    implicit val hc: HeaderCarrier = HeaderCarrier()

    private val cc: ControllerComponents = stubControllerComponents()
    implicit val ec: ExecutionContext    = cc.executionContext

    val mockConnector: GroupPaymentsConnector = mock[GroupPaymentsConnector]
    val service                               = new GroupPaymentService(mockConnector)
    val taxPayerReference: Long               = 12L
    val accPeriod: Long                       = 2L
  }

  "GroupPaymentService.getAccountingPeriodResponse" should {

    "delegate to connector and successfully return AccountingPeriodDetailsResponse" in new BaseSetup {

      when(mockConnector.getPaymentDetails(any(), any(), any(), any())(any[HeaderCarrier]))
        .thenReturn(Future.successful(response))

      val result = service.getViewModel(taxPayerReference, accPeriod, 0, 20).futureValue

      result shouldBe viewModel

      verify(mockConnector).getPaymentDetails(taxPayerReference, accPeriod, 0, 20)

      verify(mockConnector, times(1)).getPaymentDetails(taxPayerReference, accPeriod, 0, 20)

    }

    "propagate any errors or exceptions from connector" in new BaseSetup {

      when(mockConnector.getPaymentDetails(any(), any(), any(), any())(any[HeaderCarrier]))
        .thenReturn(Future.failed(new RuntimeException("boom")))

      val ex: RuntimeException = intercept[RuntimeException] {
        service.getViewModel(taxPayerReference, accPeriod, 0, 20).futureValue
      }

      ex.getMessage should include("boom")

      verify(mockConnector, times(1)).getPaymentDetails(taxPayerReference, accPeriod, 0, 20)
    }

  }

}
