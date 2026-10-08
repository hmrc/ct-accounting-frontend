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
import models.gpa.PeriodWithinRange
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.*
import org.scalatest.matchers.should.Matchers.should
import play.api.mvc.AnyContentAsEmpty
import play.api.test.FakeRequest
import play.api.test.Helpers.*
import play.api.{Application, inject}
import uk.gov.hmrc.http.HeaderCarrier

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class GroupPaymentPeriodInRangeServiceGuardSpec extends SpecBase {

  "GroupPaymentPeriodInRangeServiceGuard" - {
    "successfully validate GroupPaymentPeriodInRange when PeriodWithinRange return true" in new Setup {
      running(application) {
        when(
          mockGroupPaymentPeriodInRangeConnector.getGroupPaymentPeriodInRange(any(), any(), any(), any())(
            any[HeaderCarrier]
          )
        )
          .thenReturn(Future.successful(periodWithinRangeTrueValue))

        val result = await(groupPaymentPeriodInRangeServiceGuard.filter(authenticatedRequest))

        result mustBe None

        verify(mockGroupPaymentPeriodInRangeConnector, times(1))
          .getGroupPaymentPeriodInRange(any(), any(), any(), any())(any[HeaderCarrier])

      }
    }
    "redirect to Error page when PeriodWithinRange is false" in new Setup {
      running(application) {
        when(
          mockGroupPaymentPeriodInRangeConnector.getGroupPaymentPeriodInRange(any(), any(), any(), any())(
            any[HeaderCarrier]
          )
        )
          .thenReturn(Future.successful(periodWithinRangeFalseValue))

        val result = groupPaymentPeriodInRangeServiceGuard.filter(authenticatedRequest).map(_.value)

        status(result) mustBe SEE_OTHER
        redirectLocation(result) mustBe Some(redirectOnError)

        verify(mockGroupPaymentPeriodInRangeConnector, times(1))
          .getGroupPaymentPeriodInRange(any(), any(), any(), any())(any[HeaderCarrier])
      }
    }
    "redirect to JourneyRecoveryController when there is exception from BE" in new Setup {
      running(application) {
        when(
          mockGroupPaymentPeriodInRangeConnector.getGroupPaymentPeriodInRange(any(), any(), any(), any())(
            any[HeaderCarrier]
          )
        )
          .thenReturn(Future.failed(new RuntimeException("Error in downstream services")))

        val result = groupPaymentPeriodInRangeServiceGuard.filter(authenticatedRequest).map(_.value)

        status(result) mustBe SEE_OTHER
        redirectLocation(result) mustBe Some(redirectOnError)

        verify(mockGroupPaymentPeriodInRangeConnector, times(1))
          .getGroupPaymentPeriodInRange(any(), any(), any(), any())(any[HeaderCarrier])
      }
    }
  }

  trait Setup {
    reset(mockCompanyNominatorConnector, mockGroupPaymentPeriodInRangeConnector, mockFrontendAppConfig)
    when(mockFrontendAppConfig.pMonthsRestriction).thenReturn(60)
    when(mockFrontendAppConfig.cacheTtl).thenReturn(900L)

    implicit lazy val application: Application = applicationBuilder()
      .overrides(
        inject.bind[GroupPaymentPeriodInRangeConnector].toInstance(mockGroupPaymentPeriodInRangeConnector),
        inject.bind[FrontendAppConfig].toInstance(mockFrontendAppConfig)
      )
      .build()

    val groupPaymentPeriodInRangeServiceGuard: GroupPaymentPeriodInRangeServiceGuard =
      application.injector.instanceOf[GroupPaymentPeriodInRangeServiceGuard]

    val gpaUtr: Long              = 123533L
    val nominatedCompanyUtr: Long = 8967969L
    val pPeriod: Int              = 12

    val periodWithinRangeTrueValue: PeriodWithinRange  = PeriodWithinRange(isPeriodWithinRange = true)
    val periodWithinRangeFalseValue: PeriodWithinRange = PeriodWithinRange(isPeriodWithinRange = false)

    val redirectOnError: String = controllers.routes.JourneyRecoveryController.onPageLoad().url

    val authenticatedRequest: AuthenticatedRequest[AnyContentAsEmpty.type] =
      AuthenticatedRequest(FakeRequest("GET", "/"), gpaUtr, nominatedCompanyUtr, pPeriod)
  }
}
