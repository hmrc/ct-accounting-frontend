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

package services.payments

import connectors.payments.PaymentsOnAccountConnector
import helpers.payments.PaymentsOnAccountResponseHelper
import models.payments.PaymentsOnAccountResponse
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

class PaymentsOnAccountServiceSpec
    extends AnyWordSpec
    with Matchers
    with ScalaFutures
    with MockitoSugar
    with PaymentsOnAccountResponseHelper {

  private trait BaseSetup {
    implicit val hc: HeaderCarrier = HeaderCarrier()

    private val cc: ControllerComponents = stubControllerComponents()
    implicit val ec: ExecutionContext    = cc.executionContext

    val mockConnector: PaymentsOnAccountConnector = mock[PaymentsOnAccountConnector]
    val service                                   = new PaymentsOnAccountService(mockConnector)
    val taxRef: Long                              = 1L
  }

  "PaymentsOnAccountService.getPaymentsOnAccount" should {

    "delegate to connector and successfully return PaymentsOnAccountService response" in new BaseSetup {

      when(
        mockConnector.getPaymentsOnAccount(eqTo(taxRef))(
          any[HeaderCarrier]
        )
      )
        .thenReturn(Future.successful(paymentsOnAccountMultipleList))

      val result: PaymentsOnAccountResponse =
        service.getPaymentsOnAccount(taxRef).futureValue

      result shouldBe paymentsOnAccountMultipleList

      verify(mockConnector).getPaymentsOnAccount(taxRef)

      verify(mockConnector, times(1)).getPaymentsOnAccount(taxRef)

    }

    "propagate any errors or exceptions from connector" in new BaseSetup {

      when(
        mockConnector.getPaymentsOnAccount(eqTo(taxRef))(
          any[HeaderCarrier]
        )
      )
        .thenReturn(Future.failed(new RuntimeException("Error")))

      val ex: RuntimeException = intercept[RuntimeException] {
        service.getPaymentsOnAccount(taxRef).futureValue
      }

      ex.getMessage should include("Error")

      verify(mockConnector, times(1)).getPaymentsOnAccount(taxRef)

    }

  }

}
