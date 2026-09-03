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

import helpers.InterestViewModelHelper
import models.{MissingAccountingPeriodError, MissingDataError}
import org.mockito.ArgumentMatchers.{any, eq as eqTo}
import org.mockito.Mockito.when
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.mockito.MockitoSugar.mock
import play.api.i18n.Messages
import play.api.mvc.ControllerComponents
import play.api.test.Helpers
import play.api.test.Helpers.stubMessages
import uk.gov.hmrc.http.HeaderCarrier
import viewmodels.InterestViewModel

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class InterestServiceSpec extends AnyWordSpec with Matchers with ScalaFutures with InterestViewModelHelper {

  private trait Fixture {
    val mockAccountingPeriodDetailsService: AccountingPeriodDetailsService =
      mock[AccountingPeriodDetailsService]
    val mockAccountingPeriodService: AccountingPeriodsService              = mock[AccountingPeriodsService]

    val taxRef: Long           = 12L
    val accountingPeriod: Long = 345L

    val cc: ControllerComponents    = Helpers.stubControllerComponents()
    implicit val messages: Messages = stubMessages()
    implicit val hc: HeaderCarrier  = HeaderCarrier()

    val service = new InterestService(mockAccountingPeriodDetailsService, mockAccountingPeriodService)
  }

  "getInterest should calculate clericalCalcFlag as false and return Right(InterestViewModel) when getClericalIntSig and creditDebitInterestInd are false " in new Fixture {
    when(
      mockAccountingPeriodDetailsService.getAccountingPeriodResponse(eqTo(taxRef), eqTo(accountingPeriod))(
        any[HeaderCarrier]
      )
    ).thenReturn(Future.successful(accountingPeriodDetailsResponseForAccruing))

    when(mockAccountingPeriodService.getAccountingPeriods(eqTo(taxRef))(any[HeaderCarrier]))
      .thenReturn(Future.successful(accountingPeriodsWithFalseClericalIntSigAndCreditDebitInterest))

    val result: Either[MissingDataError, InterestViewModel] = service.getInterest(taxRef, accountingPeriod).futureValue

    result.isRight shouldBe true
    result         shouldBe Right(interestViewModelForAccruing)

  }
  "getInterest should calculate clericalCalcFlag as true and return Right(InterestViewModel) when getClericalIntSig is true " in new Fixture {
    when(
      mockAccountingPeriodDetailsService.getAccountingPeriodResponse(eqTo(taxRef), eqTo(accountingPeriod))(
        any[HeaderCarrier]
      )
    ).thenReturn(Future.successful(accountingPeriodDetailsResponseForAccruing))

    when(mockAccountingPeriodService.getAccountingPeriods(eqTo(taxRef))(any[HeaderCarrier]))
      .thenReturn(Future.successful(accountingPeriodsForTrueClericalIntSig))

    val result: Either[MissingDataError, InterestViewModel] = service.getInterest(taxRef, accountingPeriod).futureValue

    result.isRight shouldBe true
    result         shouldBe Right(interestViewModelForAccruingWithRepaymentRowNotHyperLink)
  }
  "getInterest should calculate clericalCalcFlag as true and return Right(InterestViewModel) when creditDebitInterestInd is true " in new Fixture {
    when(
      mockAccountingPeriodDetailsService.getAccountingPeriodResponse(eqTo(taxRef), eqTo(accountingPeriod))(
        any[HeaderCarrier]
      )
    ).thenReturn(Future.successful(accountingPeriodDetailsResponseForNotAccruing))

    when(mockAccountingPeriodService.getAccountingPeriods(eqTo(taxRef))(any[HeaderCarrier]))
      .thenReturn(Future.successful(accountingPeriodsForTrueCreditDebitInterestInd))

    val result: Either[MissingDataError, InterestViewModel] = service.getInterest(taxRef, accountingPeriod).futureValue

    result.isRight shouldBe true
    result         shouldBe Right(interestViewModelForNotAccruingWithRepaymentRowNotHyperLink)
  }
  "getInterest should return Left(MissingAccountingPeriodError) when accountingPeriod in AccountingPeriodDetailsResponse is not present in AccountingPeriods" in new Fixture {

    when(
      mockAccountingPeriodDetailsService.getAccountingPeriodResponse(eqTo(taxRef), eqTo(accountingPeriod))(
        any[HeaderCarrier]
      )
    ).thenReturn(Future.successful(accountingPeriodDetailsResponseForNotAccruing))

    when(mockAccountingPeriodService.getAccountingPeriods(eqTo(taxRef))(any[HeaderCarrier]))
      .thenReturn(Future.successful(accountingPeriodsWithNoMatchingAccountingPeriods))

    val result: Either[MissingDataError, InterestViewModel] = service.getInterest(taxRef, accountingPeriod).futureValue

    result.isLeft shouldBe true
    result        shouldBe Left(MissingAccountingPeriodError(s"Cannot find the matching accounting period for taxRef:$taxRef"))
  }
  "getInterest should propagate errors from AccountingPeriodDetailsService" in new Fixture {
    when(
      mockAccountingPeriodDetailsService.getAccountingPeriodResponse(eqTo(taxRef), eqTo(accountingPeriod))(
        any[HeaderCarrier]
      )
    ).thenReturn(Future.failed(new RuntimeException("Boom")))

    when(mockAccountingPeriodService.getAccountingPeriods(eqTo(taxRef))(any[HeaderCarrier]))
      .thenReturn(Future.successful(accountingPeriodsWithNoMatchingAccountingPeriods))
    val ex: Exception = intercept[Exception] {
      service.getInterest(taxRef, accountingPeriod).futureValue
    }

    ex.getMessage should include("Boom")
  }
  "getInterest should propagate errors from AccountingPeriodsService" in new Fixture {
    when(
      mockAccountingPeriodDetailsService.getAccountingPeriodResponse(eqTo(taxRef), eqTo(accountingPeriod))(
        any[HeaderCarrier]
      )
    ).thenReturn(Future.successful(accountingPeriodDetailsResponseForNotAccruing))

    when(mockAccountingPeriodService.getAccountingPeriods(eqTo(taxRef))(any[HeaderCarrier]))
      .thenReturn(Future.failed(new RuntimeException("Boom")))

    val ex: Exception = intercept[Exception] {
      service.getInterest(taxRef, accountingPeriod).futureValue
    }

    ex.getMessage should include("Boom")
  }

}
