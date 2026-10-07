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

package controllers.actions

import base.SpecBase
import config.FrontendAppConfig
import connectors.gpa.{CompanyNominatorConnector, GroupPaymentPeriodInRangeConnector}
import models.AuthenticatedRequest
import models.gpa.{CompanyNominator, PeriodWithinRange}
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.{reset, times, verify, verifyNoInteractions, when}
import org.scalatest.matchers.should.Matchers.should
import play.api.mvc.{AnyContentAsEmpty, Result}
import play.api.test.FakeRequest
import play.api.test.Helpers.*
import play.api.{Application, inject}
import uk.gov.hmrc.http.HeaderCarrier

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class NominatedCompanyGPAServiceGuardSpec extends SpecBase {

  "NominatedCompanyGPAServiceGuard" - {
    "redirect to Error Page when pMonthRestriction > 60 " in new Setup {
      running(application) {
        when(mockCompanyNominatorConnector.getCompanyNominator(any(), any())(any[HeaderCarrier]))
          .thenReturn(Future.successful(companyNominatorTrueValue))

        when(
          mockGroupPaymentPeriodInRangeConnector.getGroupPaymentPeriodInRange(any(), any(), any(), any())(
            any[HeaderCarrier]
          )
        )
          .thenReturn(Future.successful(periodWithinRangeTrueValue))

        val result = nominatedServiceGuard.filter(invalidMonthRestrictionAuthenticatedRequest).map(_.value)

        status(result) mustBe SEE_OTHER
        redirectLocation(result) mustBe Some(redirectOnError)

        verifyNoInteractions(mockCompanyNominatorConnector)
        verifyNoInteractions(mockGroupPaymentPeriodInRangeConnector)
      }
    }
    "successfully validate nominated company when CompanyNominator and PeriodWithinRange both return true" in new Setup {
      running(application) {
        when(mockCompanyNominatorConnector.getCompanyNominator(any(), any())(any[HeaderCarrier]))
          .thenReturn(Future.successful(companyNominatorTrueValue))

        when(
          mockGroupPaymentPeriodInRangeConnector.getGroupPaymentPeriodInRange(any(), any(), any(), any())(
            any[HeaderCarrier]
          )
        )
          .thenReturn(Future.successful(periodWithinRangeTrueValue))

        val result = await(nominatedServiceGuard.filter(authenticatedRequest))

        result mustBe None

        verify(mockCompanyNominatorConnector, times(1)).getCompanyNominator(any(), any())(any[HeaderCarrier])
        verify(mockGroupPaymentPeriodInRangeConnector, times(1))
          .getGroupPaymentPeriodInRange(any(), any(), any(), any())(any[HeaderCarrier])

      }
    }
    "redirect to Error page when CompanyNominator is true but PeriodWithinRange is false" in new Setup {
      running(application) {
        when(mockCompanyNominatorConnector.getCompanyNominator(any(), any())(any[HeaderCarrier]))
          .thenReturn(Future.successful(companyNominatorTrueValue))

        when(
          mockGroupPaymentPeriodInRangeConnector.getGroupPaymentPeriodInRange(any(), any(), any(), any())(
            any[HeaderCarrier]
          )
        )
          .thenReturn(Future.successful(periodWithinRangeFalseValue))

        val result = nominatedServiceGuard.filter(authenticatedRequest).map(_.value)

        status(result) mustBe SEE_OTHER
        redirectLocation(result) mustBe Some(redirectOnError)

        verify(mockCompanyNominatorConnector, times(1)).getCompanyNominator(any(), any())(any[HeaderCarrier])
        verify(mockGroupPaymentPeriodInRangeConnector, times(1))
          .getGroupPaymentPeriodInRange(any(), any(), any(), any())(any[HeaderCarrier])
      }
    }
    "redirect to Error page when CompanyNominator is false but PeriodWithinRange is true" in new Setup {
      running(application) {
        when(mockCompanyNominatorConnector.getCompanyNominator(any(), any())(any[HeaderCarrier]))
          .thenReturn(Future.successful(companyNominatorFalseValue))

        when(
          mockGroupPaymentPeriodInRangeConnector.getGroupPaymentPeriodInRange(any(), any(), any(), any())(
            any[HeaderCarrier]
          )
        )
          .thenReturn(Future.successful(periodWithinRangeTrueValue))

        val result = nominatedServiceGuard.filter(authenticatedRequest).map(_.value)

        status(result) mustBe SEE_OTHER
        redirectLocation(result) mustBe Some(redirectOnError)

        verify(mockCompanyNominatorConnector, times(1)).getCompanyNominator(any(), any())(any[HeaderCarrier])
        verify(mockGroupPaymentPeriodInRangeConnector, times(1))
          .getGroupPaymentPeriodInRange(any(), any(), any(), any())(any[HeaderCarrier])
      }
    }
    "redirect to Error page when both CompanyNominator and PeriodWithinRange are false " in new Setup {
      running(application) {
        when(mockCompanyNominatorConnector.getCompanyNominator(any(), any())(any[HeaderCarrier]))
          .thenReturn(Future.successful(companyNominatorFalseValue))

        when(
          mockGroupPaymentPeriodInRangeConnector.getGroupPaymentPeriodInRange(any(), any(), any(), any())(
            any[HeaderCarrier]
          )
        )
          .thenReturn(Future.successful(periodWithinRangeFalseValue))

        val result = nominatedServiceGuard.filter(authenticatedRequest).map(_.value)

        status(result) mustBe SEE_OTHER
        redirectLocation(result) mustBe Some(redirectOnError)

        verify(mockCompanyNominatorConnector, times(1)).getCompanyNominator(any(), any())(any[HeaderCarrier])
        verify(mockGroupPaymentPeriodInRangeConnector, times(1))
          .getGroupPaymentPeriodInRange(any(), any(), any(), any())(any[HeaderCarrier])
      }
    }
    "redirect to Error page when there is exception from BE" in new Setup {
      running(application) {
        when(mockCompanyNominatorConnector.getCompanyNominator(any(), any())(any[HeaderCarrier]))
          .thenReturn(Future.failed(new RuntimeException("Error in downstream services")))

        when(
          mockGroupPaymentPeriodInRangeConnector.getGroupPaymentPeriodInRange(any(), any(), any(), any())(
            any[HeaderCarrier]
          )
        )
          .thenReturn(Future.successful(periodWithinRangeTrueValue))

        val result = nominatedServiceGuard.filter(authenticatedRequest).map(_.value)

        status(result) mustBe SEE_OTHER
        redirectLocation(result) mustBe Some(redirectOnError)

        verify(mockCompanyNominatorConnector, times(1)).getCompanyNominator(any(), any())(any[HeaderCarrier])
        verifyNoInteractions(mockGroupPaymentPeriodInRangeConnector)
      }
    }
  }

  trait Setup {
    reset(mockCompanyNominatorConnector, mockGroupPaymentPeriodInRangeConnector, mockFrontendAppConfig)
    when(mockFrontendAppConfig.pMonthsRestriction).thenReturn(60)

    implicit lazy val application: Application = applicationBuilder()
      .overrides(
        inject.bind[CompanyNominatorConnector].toInstance(mockCompanyNominatorConnector),
        inject.bind[GroupPaymentPeriodInRangeConnector].toInstance(mockGroupPaymentPeriodInRangeConnector),
        inject.bind[FrontendAppConfig].toInstance(mockFrontendAppConfig)
      )
      .build()

    val nominatedServiceGuard: NominatedCompanyGPAServiceGuard =
      application.injector.instanceOf[NominatedCompanyGPAServiceGuard]

    val gpaUtr: Long                 = 123533L
    val nominatedCompanyUtr: Long    = 8967969L
    val pPeriod: Int                 = 12
    val validMonthRestriction: Int   = 30
    val invalidMonthRestriction: Int = 80

    val companyNominatorTrueValue: CompanyNominator  = CompanyNominator(isParticipator = true)
    val companyNominatorFalseValue: CompanyNominator = CompanyNominator(isParticipator = false)

    val periodWithinRangeTrueValue: PeriodWithinRange  = PeriodWithinRange(isPeriodWithinRange = true)
    val periodWithinRangeFalseValue: PeriodWithinRange = PeriodWithinRange(isPeriodWithinRange = false)

    val redirectOnError: String = controllers.routes.JourneyRecoveryController.onPageLoad().url

    val authenticatedRequest: AuthenticatedRequest[AnyContentAsEmpty.type] =
      AuthenticatedRequest(FakeRequest("GET", "/"), gpaUtr, nominatedCompanyUtr, pPeriod, validMonthRestriction)

    val invalidMonthRestrictionAuthenticatedRequest: AuthenticatedRequest[AnyContentAsEmpty.type] =
      AuthenticatedRequest(FakeRequest("GET", "/"), gpaUtr, gpaUtr, pPeriod, invalidMonthRestriction)
  }
}
