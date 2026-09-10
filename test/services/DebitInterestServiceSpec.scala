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

import connectors.DebitInterestConnector
import helpers.DebitInterestHelper
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
import viewmodels.DebitInterestViewModel
import scala.concurrent.{ExecutionContext, Future}

class DebitInterestServiceSpec extends AnyWordSpec with DebitInterestHelper with Matchers with ScalaFutures {

  private trait Fixture {
    val mockDebitInterestConnector: DebitInterestConnector = mock[DebitInterestConnector]

    val cc                            = Helpers.stubControllerComponents()
    implicit val messages: Messages   = stubMessages()
    implicit val hc: HeaderCarrier    = HeaderCarrier()
    implicit val ec: ExecutionContext = cc.executionContext

    val service = new DebitInterestService(mockDebitInterestConnector)
  }

  "getDebitInterest returns default list of records" in new Fixture {
    when(
      mockDebitInterestConnector.getDebitInterest(any[Long], any[Long], any[String])(any[HeaderCarrier])
    ).thenReturn(Future.successful(defaultRecord))

    val result: DebitInterestViewModel = service.getDebitInterest(1L, 1L, "DBI").futureValue

    result shouldBe defaultViewModel

    verify(mockDebitInterestConnector).getDebitInterest(1L, 1L, "DBI")(hc)
  }

  /*
  "getAccountingPeriods propagate any errors from connector" in new Fixture {
    when(
      mockAccountingConnector.getAccountingPeriods(any[Long])(any[HeaderCarrier])
    )
      .thenReturn(Future.failed(new RuntimeException("Boom")))

    val ex: Exception = intercept[Exception] {
      service.getAccountingPeriods(1L).futureValue
    }

    ex.getMessage should include("Boom")

    verify(mockAccountingConnector).getAccountingPeriods(1L)(hc)
  }
   */

}
