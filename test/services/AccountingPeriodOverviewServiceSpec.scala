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

import connectors.AccountingPeriodOverviewConnector
import helpers.AccountingPeriodOverviewHelper
import models.AccountingPeriodOverview
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

import java.time.LocalDate
import scala.concurrent.Future

class AccountingPeriodOverviewServiceSpec
    extends AnyWordSpec
    with AccountingPeriodOverviewHelper
    with Matchers
    with ScalaFutures {

  private trait Fixture {
    val mockConnector: AccountingPeriodOverviewConnector = mock[AccountingPeriodOverviewConnector]

    val cc                          = Helpers.stubControllerComponents()
    implicit val messages: Messages = stubMessages()
    implicit val hc: HeaderCarrier  = HeaderCarrier()

    val service = new AccountingPeriodOverviewService(mockConnector)
  }

  "getAccountingPeriodOverview returns correct AccountingPeriodsOverview" in new Fixture {

    when(
      mockConnector.getAccountingPeriodOverview(any(), any())(any[HeaderCarrier])
    )
      .thenReturn(Future.successful(accountingPeriodOverviewResponse))

    val result: AccountingPeriodOverview =
      service.getAccountingPeriodOverview(1L, 1L, LocalDate.of(2026, 1, 1)).futureValue

    result shouldBe accountingPeriodOverviewResponse

    verify(mockConnector).getAccountingPeriodOverview(any(), any())(any[HeaderCarrier])
  }
  "getAccountingPeriodOverview propagate any errors from connector" in new Fixture {
    when(
      mockConnector.getAccountingPeriodOverview(any(), any())(any[HeaderCarrier])
    )
      .thenReturn(Future.failed(new RuntimeException("Error")))

    val ex: Exception = intercept[Exception] {
      service.getAccountingPeriodOverview(1L, 2L, LocalDate.of(2026, 1, 1)).futureValue
    }

    ex.getMessage should include("Error")

    verify(mockConnector).getAccountingPeriodOverview(1L, 2L)(hc)
  }

}
