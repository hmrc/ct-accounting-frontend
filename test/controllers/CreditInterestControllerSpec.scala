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

package controllers

import base.SpecBase
import models.{InterestAccrualListWithInterestAccruedDays, InterestAccrualWithInterestAccruedDays}
import org.mockito.ArgumentMatchers.{any, eq as eqTo}
import org.mockito.Mockito.when
import org.scalatestplus.mockito.MockitoSugar
import play.api.inject.bind
import play.api.test.FakeRequest
import play.api.test.Helpers.*
import connectors.InterestAccrualListConnector
import uk.gov.hmrc.http.HeaderCarrier
import views.html.CreditInterestView

import java.time.LocalDate
import scala.concurrent.Future

class CreditInterestControllerSpec extends SpecBase with MockitoSugar {
  implicit val hc: HeaderCarrier              = HeaderCarrier()
  val mockConnector: InterestAccrualListConnector = mock[InterestAccrualListConnector]

  val creditInterestResponse: InterestAccrualListWithInterestAccruedDays =
    InterestAccrualListWithInterestAccruedDays(
      List(
        InterestAccrualWithInterestAccruedDays(
          computationAmount = BigDecimal(1230.44),
          interestAccrualFromDate = LocalDate.of(2026, 3, 1),
          interestAccrualToDate = LocalDate.of(2026, 4, 1),
          interestRate = BigDecimal(4),
          interestAmount = BigDecimal(103.12),
          apEndDate = LocalDate.of(2026, 1, 1),
          noOfDays = 5
        ),
        InterestAccrualWithInterestAccruedDays(
          computationAmount = BigDecimal(2344.44),
          interestAccrualFromDate = LocalDate.of(2026, 3, 1),
          interestAccrualToDate = LocalDate.of(2026, 4, 1),
          interestRate = BigDecimal(5.6),
          interestAmount = BigDecimal(3041.34),
          apEndDate = LocalDate.of(2026, 1, 1),
          noOfDays = 1
        ),
        InterestAccrualWithInterestAccruedDays(
          computationAmount = BigDecimal(3125.43),
          interestAccrualFromDate = LocalDate.of(2026, 3, 1),
          interestAccrualToDate = LocalDate.of(2026, 4, 1),
          interestRate = BigDecimal(42),
          interestAmount = BigDecimal(535.12),
          apEndDate = LocalDate.of(2026, 1, 1),
          noOfDays = 7
        )
      )
    )

  // TODO: hardcoded value in the controller until it's wired up to session data
  val expectedAccountPeriod: LocalDate = LocalDate.of(2026, 1, 1)
  val total: BigDecimal                = creditInterestResponse.interestAccruals.map(_.interestAmount).sum

  "CreditInterest Controller" - {

    "must return OK and the correct credit interest view for a GET when interest type equals RIN" in {

      when(mockConnector.getInterestAccrual(eqTo(1L), eqTo(1L), eqTo("ICR"))(any[HeaderCarrier]))
        .thenReturn(Future.successful(creditInterestResponse))

      val application = applicationBuilder()
        .overrides(bind[InterestAccrualListConnector].toInstance(mockConnector))
        .build()

      running(application) {
        val request = FakeRequest(GET, routes.CreditInterestController.onPageLoad().url)
        val result  = route(application, request).value
        val view    = application.injector.instanceOf[CreditInterestView]

        status(result) mustEqual OK
        contentAsString(result) mustEqual
          view(creditInterestResponse.interestAccruals, expectedAccountPeriod, total)(
            request,
            messages(application)
          ).toString
      }
    }
  }
}
