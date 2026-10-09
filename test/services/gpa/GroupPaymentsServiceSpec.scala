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

package services.gpa

import connectors.gpa.GroupPaymentsConnector
import helpers.gpa.GroupPaymentsHelper
import models.gpa.GroupSummaryDetailsResponse
import org.mockito.ArgumentMatchers.{any, eq as eqTo}
import org.mockito.Mockito.{times, verify, when}
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.mockito.MockitoSugar
import play.api.mvc.ControllerComponents
import play.api.test.Helpers.stubControllerComponents
import uk.gov.hmrc.http.HeaderCarrier

import scala.concurrent.{ExecutionContext, Future}

class GroupPaymentsServiceSpec
    extends AnyWordSpec
    with Matchers
    with ScalaFutures
    with MockitoSugar
    with GroupPaymentsHelper {

  private trait BaseSetup {
    implicit val hc: HeaderCarrier = HeaderCarrier()

    private val cc: ControllerComponents = stubControllerComponents()
    implicit val ec: ExecutionContext    = cc.executionContext

    val mockConnector: GroupPaymentsConnector = mock[GroupPaymentsConnector]
    val service                               = new GroupPaymentsService(mockConnector)
    val gpaUTR: Long                          = 1L
    val nomCompanyUtr: Long                   = 5L
  }

  "GroupPaymentsService.getGroupSummary" should {

    "delegate to connector and successfully return GroupSummaryDetailsResponse" in new BaseSetup {

      when(mockConnector.getGroupSummary(eqTo(gpaUTR), eqTo(nomCompanyUtr))(any[HeaderCarrier]))
        .thenReturn(Future.successful(groupPaymentDetailsResponse))

      val result: GroupSummaryDetailsResponse =
        service.getGroupSummary(gpaUTR, nomCompanyUtr).futureValue

      result shouldBe groupPaymentDetailsResponse

      verify(mockConnector).getGroupSummary(gpaUTR, nomCompanyUtr)

      verify(mockConnector, times(1)).getGroupSummary(gpaUTR, nomCompanyUtr)

    }

    "propagate any errors or exceptions from connector" in new BaseSetup {

      when(mockConnector.getGroupSummary(eqTo(gpaUTR), eqTo(nomCompanyUtr))(any[HeaderCarrier]))
        .thenReturn(Future.failed(new RuntimeException("Error")))

      val ex: RuntimeException = intercept[RuntimeException] {
        service.getGroupSummary(gpaUTR, nomCompanyUtr).futureValue
      }

      ex.getMessage should include("Error")

      verify(mockConnector, times(1)).getGroupSummary(gpaUTR, nomCompanyUtr)

    }

  }

}
