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

import connectors.AdjustmentsAccountingPeriodConnector
import helpers.AdjustmentsDataHelper
import org.mockito.Mockito
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.{verify, when}
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.mockito.MockitoSugar
import org.scalatestplus.mockito.MockitoSugar.mock
import uk.gov.hmrc.http.HeaderCarrier

import scala.concurrent.Future

class AdjustmentsAccountingPeriodServiceSpec
    extends AnyWordSpec
    with Matchers
    with ScalaFutures
    with MockitoSugar
    with AdjustmentsDataHelper {

  private trait Setup {
    implicit val hc: HeaderCarrier = HeaderCarrier()

    val mockConnector: AdjustmentsAccountingPeriodConnector = mock[AdjustmentsAccountingPeriodConnector]
    val service                                             = new AdjustmentsAccountingPeriodService(mockConnector)
  }

  "getAdjustmentTransactions" should {

    "delegate to connector and successfully return AdjustmentTransactionsList" in new Setup {
      when(mockConnector.getAdjustmentTransactions(any(), any())(any[HeaderCarrier]))
        .thenReturn(Future.successful(adjustmentTransactionsListWithOneItem))

      val result = service.getAdjustmentTransactions(1L, 2L).futureValue

      result shouldBe adjustmentTransactionsListWithOneItem

      verify(mockConnector).getAdjustmentTransactions(1L, 2L)
    }

    "propagate any errors or exceptions from connector" in new Setup {
      when(mockConnector.getAdjustmentTransactions(any(), any())(any[HeaderCarrier]))
        .thenReturn(Future.failed(new RuntimeException("error")))

      val ex = intercept[RuntimeException] {
        service.getAdjustmentTransactions(2L, 3L).futureValue
      }

      ex.getMessage should include("error")

      verify(mockConnector).getAdjustmentTransactions(2L, 3L)
    }
  }

}
