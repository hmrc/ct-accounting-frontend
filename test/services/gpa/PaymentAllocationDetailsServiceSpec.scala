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

package services.gpa

import connectors.gpa.PaymentAllocationDetailsConnector
import helpers.gpa.PaymentAllocationDetailsHelper
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito
import org.mockito.Mockito.{verify, when}
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.mockito.MockitoSugar
import org.scalatestplus.mockito.MockitoSugar.mock
import uk.gov.hmrc.http.HeaderCarrier

import scala.concurrent.Future

class PaymentAllocationDetailsServiceSpec
    extends AnyWordSpec
    with Matchers
    with ScalaFutures
    with MockitoSugar
    with PaymentAllocationDetailsHelper {

  private trait Setup {
    implicit val hc: HeaderCarrier = HeaderCarrier()

    val mockConnector: PaymentAllocationDetailsConnector = mock[PaymentAllocationDetailsConnector]
    val service                                             = new PaymentAllocationDetailsService(mockConnector)
  }

  "getGPAPaymentAllocationDetail" should {

    "delegate to connector and successfully return payment allocation details" in new Setup {
      when(mockConnector.getGPAPaymentAllocationDetail(any(), any(), any(), any(), any(), any())(any[HeaderCarrier]))
        .thenReturn(Future.successful(fullPaymentAllocationDetails))

      val result = service.getGPAPaymentAllocationDetail(1L, 2L, 3L, 4L, 5, 6).futureValue

      result shouldBe fullPaymentAllocationDetails

      verify(mockConnector).getGPAPaymentAllocationDetail(1L, 2L, 3L, 4L, 5, 6)
    }

    "propagate any errors or exceptions from connector" in new Setup {
      when(mockConnector.getGPAPaymentAllocationDetail(any(), any(), any(), any(), any(), any())(any[HeaderCarrier]))
        .thenReturn(Future.failed(new RuntimeException("error")))

      val ex = intercept[RuntimeException] {
        service.getGPAPaymentAllocationDetail(1L, 2L, 3L, 4L, 5, 6).futureValue
      }

      ex.getMessage should include("error")

      verify(mockConnector).getGPAPaymentAllocationDetail(1L, 2L, 3L, 4L, 5, 6)
    }
  }

}
