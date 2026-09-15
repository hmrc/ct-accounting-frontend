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

import connectors.InterestAccrualListConnector
import helpers.InterestAccrualListHelper
import models.InterestAccrualListWithInterestAccruedDays
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

class LatePaymentInterestServiceSpec
    extends AnyWordSpec
    with InterestAccrualListHelper
    with Matchers
    with ScalaFutures {

  private trait Fixture {
    val mockConnector: InterestAccrualListConnector = mock[InterestAccrualListConnector]

    val cc                          = Helpers.stubControllerComponents()
    implicit val messages: Messages = stubMessages()
    implicit val hc: HeaderCarrier  = HeaderCarrier()

    val service = new LatePaymentInterestService(mockConnector)
  }

  "getAccountingPeriods returns correct AccountingPeriods with multiple items" in new Fixture {

    when(
      mockConnector.getInterestAccrualList(any(), any(), any())(any[HeaderCarrier])
    )
      .thenReturn(Future.successful(interestAccrualMultipleObjects))

    val result: InterestAccrualListWithInterestAccruedDays = service.getInterestAccrualList(1L, 2L, "IDB").futureValue

    result.interestAccruals shouldBe interestAccrualMultipleObjects.interestAccruals

    verify(mockConnector).getInterestAccrualList(1L, 2L, "IDE")(hc)
  }
  "getAccountingPeriods propagate any errors from connector" in new Fixture {
    when(
      mockConnector.getInterestAccrualList(any(), any(), any())(any[HeaderCarrier])
    )
      .thenReturn(Future.failed(new RuntimeException("Boom")))

    val ex: Exception = intercept[Exception] {
      service.getInterestAccrualList(1L, 2L, "RIN").futureValue
    }

    ex.getMessage should include("Boom")

    verify(mockConnector).getInterestAccrualList(1L, 2L, "RIN")(hc)
  }

}
