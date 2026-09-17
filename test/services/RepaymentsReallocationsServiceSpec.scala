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

import connectors.RepaymentsReallocationsConnector
import helpers.RepaymentsReallocationsHelper
import models.RepayReallocationSummary
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.{verify, when}
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.mockito.MockitoSugar.mock
import play.api.i18n.Messages
import play.api.test.Helpers
import play.api.test.Helpers.stubMessages
import uk.gov.hmrc.http.HeaderCarrier

import scala.concurrent.Future

class RepaymentsReallocationsServiceSpec
    extends AnyWordSpec
    with RepaymentsReallocationsHelper
    with Matchers
    with ScalaFutures {

  private trait Fixture {
    val mockRepaymentsRealloctionsConnector: RepaymentsReallocationsConnector = mock[RepaymentsReallocationsConnector]

    val cc                          = Helpers.stubControllerComponents()
    implicit val messages: Messages = stubMessages()
    implicit val hc: HeaderCarrier  = HeaderCarrier()

    val service = new RepaymentsReallocationService(mockRepaymentsRealloctionsConnector)
  }

  "getRepayReallocationSummary returns correct Repayment summary transaction" in new Fixture {

    when(
      mockRepaymentsRealloctionsConnector.getRepayReallocationSummary(any[Long], any[Long])(any[HeaderCarrier])
    )
      .thenReturn(Future.successful(repaymentSummary))

    val result: RepayReallocationSummary = service.getRepayReallocationSummary(1L, 1L).futureValue

    result.transactions shouldBe repaymentSummary.transactions

    verify(mockRepaymentsRealloctionsConnector).getRepayReallocationSummary(1L, 1L)(hc)
  }

  "getRepayReallocationSummary returns correct Reallocation summary transaction" in new Fixture {

    when(
      mockRepaymentsRealloctionsConnector.getRepayReallocationSummary(any[Long], any[Long])(any[HeaderCarrier])
    )
      .thenReturn(Future.successful(reallocationSummary))

    val result: RepayReallocationSummary = service.getRepayReallocationSummary(1L, 1L).futureValue

    result.transactions shouldBe reallocationSummary.transactions

    verify(mockRepaymentsRealloctionsConnector).getRepayReallocationSummary(1L, 1L)(hc)
  }

  "getRepayReallocationSummary returns correct multiple summary transactions, including repayment and reallocations" in new Fixture {

    when(
      mockRepaymentsRealloctionsConnector.getRepayReallocationSummary(any[Long], any[Long])(any[HeaderCarrier])
    )
      .thenReturn(Future.successful(multipleSummaries))

    val result: RepayReallocationSummary = service.getRepayReallocationSummary(1L, 1L).futureValue

    result.transactions shouldBe multipleSummaries.transactions

    verify(mockRepaymentsRealloctionsConnector).getRepayReallocationSummary(1L, 1L)(hc)
  }

  "getAccountingPeriods propagate any errors from connector" in new Fixture {
    when(
      mockRepaymentsRealloctionsConnector.getRepayReallocationSummary(any[Long], any[Long])(any[HeaderCarrier])
    )
      .thenReturn(Future.failed(new RuntimeException("Error while retrieving repayment reallocation summary")))

    val ex: Exception = intercept[Exception] {
      service.getRepayReallocationSummary(1L, 1L).futureValue
    }

    ex.getMessage should include("Error while retrieving repayment reallocation summary")

    verify(mockRepaymentsRealloctionsConnector).getRepayReallocationSummary(1L, 1L)(hc)
  }

}
