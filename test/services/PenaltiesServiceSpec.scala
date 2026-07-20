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

import connectors.PenaltiesConnector
import helpers.PenaltiesDataHelper
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.{when, verify}
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.mockito.MockitoSugar.mock
import play.api.i18n.Messages
import play.api.test.Helpers
import play.api.test.Helpers.stubMessages
import uk.gov.hmrc.http.HeaderCarrier
import viewmodels.{PenaltiesAccountingPeriodViewModel, PenaltiesAccountingPeriodViewModelRow}

import java.time.LocalDate
import scala.concurrent.{ExecutionContext, Future}

class PenaltiesServiceSpec extends AnyWordSpec
  with PenaltiesDataHelper
  with Matchers with ScalaFutures {


  private trait Fixture {
    val mockPenaltiesConnector: PenaltiesConnector = mock[PenaltiesConnector]

    val cc                            = Helpers.stubControllerComponents()
    implicit val ec: ExecutionContext = cc.executionContext
    implicit val messages: Messages = stubMessages()
    implicit val hc: HeaderCarrier    = HeaderCarrier()

    val service = new PenaltiesService(mockPenaltiesConnector)
  }

  "getViewModel returns correct viewModel" in new Fixture {

    when(mockPenaltiesConnector.getPenaltyTransactionList(any[Long], any[Long])(any[HeaderCarrier]))
      .thenReturn(Future.successful(penaltyItems))

    val viewModel: PenaltiesAccountingPeriodViewModel = service.getViewModel(1L, 1L).futureValue

    viewModel.rows shouldBe List(
      PenaltiesAccountingPeriodViewModelRow(
        date =LocalDate.of(2025, 5, 1),
        description = "Fixed rate penalty",
        amount = BigDecimal(100.13)
      ),
      PenaltiesAccountingPeriodViewModelRow(
        date =LocalDate.of(2021, 3, 7),
        description = "Tax geared penalty",
        amount = BigDecimal(27.19)
      ),
    )

    verify(mockPenaltiesConnector).getPenaltyTransactionList(1L, 1L)(hc)
  }

  //TODO: add more tests scenarios
}

