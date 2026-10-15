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

import connectors.gpa.GroupPaymentsConnector
import helpers.gpa.GroupPaymentHelper
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.{times, verify, verifyNoInteractions, when}
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.mockito.MockitoSugar
import play.api.i18n.Messages
import play.api.mvc.ControllerComponents
import play.api.test.Helpers.{stubControllerComponents, stubMessages}
import services.gpa.{GpaPaymentDetailsService, GroupPaymentService}
import uk.gov.hmrc.http.HeaderCarrier
import utils.Constants.emptyString
import viewmodels.gpa.GroupPaymentArrangementViewModel

import scala.concurrent.{ExecutionContext, Future}

class GroupPaymentServiceSpec
    extends AnyWordSpec
    with Matchers
    with ScalaFutures
    with MockitoSugar
    with GroupPaymentHelper {

  private trait BaseSetup {
    implicit val hc: HeaderCarrier = HeaderCarrier()

    private val cc: ControllerComponents = stubControllerComponents()
    implicit val ec: ExecutionContext    = cc.executionContext

    implicit val messages: Messages = stubMessages()

    val mockConnector: GroupPaymentsConnector               = mock[GroupPaymentsConnector]
    val mockGpaDescriptionService: GpaPaymentDetailsService = mock[GpaPaymentDetailsService]
    val service                                             = new GroupPaymentService(mockConnector, mockGpaDescriptionService)
    val taxPayerReference: Long                             = 12L
    val accPeriod: Long                                     = 2L
  }

  "GroupPaymentService.getViewModel" should {
    "delegate to connector and successfully return GroupPaymentArrangementViewModel with appropriate description for RFRReallocation, status for O and S" in new BaseSetup {
      when(mockConnector.getPaymentDetails(any(), any(), any(), any())(any[HeaderCarrier]))
        .thenReturn(Future.successful(responseRfrReallocation))

      when(mockGpaDescriptionService.gpaPaymentDescription(any(), any(), any())(any[Messages]))
        .thenReturn("Miscellaneous Transfer")

      val result: GroupPaymentArrangementViewModel =
        service.getViewModel(taxPayerReference, accPeriod, 0, 20).futureValue

      result shouldBe viewModelResponseRfrReallocation

      result.records.head.description shouldBe "Miscellaneous Transfer"

      verify(mockConnector).getPaymentDetails(any(), any(), any(), any())(any[HeaderCarrier])

      verify(mockGpaDescriptionService, times(1)).gpaPaymentDescription(any(), any(), any())(any[Messages])

      verify(mockConnector, times(1)).getPaymentDetails(any(), any(), any(), any())(any[HeaderCarrier])

    }
    "delegate to connector and successfully return GroupPaymentArrangementViewModel with appropriate description for RTOReallocation, status for C" in new BaseSetup {

      when(mockConnector.getPaymentDetails(any(), any(), any(), any())(any[HeaderCarrier]))
        .thenReturn(Future.successful(responseRtoReallocation))

      when(mockGpaDescriptionService.gpaPaymentDescription(any(), any(), any())(any[Messages]))
        .thenReturn("Reallocation FROM 99, AP ending 2026-01-01")

      val result: GroupPaymentArrangementViewModel =
        service.getViewModel(taxPayerReference, accPeriod, 0, 20).futureValue

      result shouldBe viewModelResponseRtoReallocation

      result.records.head.description shouldBe "Reallocation FROM 99, AP ending 2026-01-01"

      verify(mockConnector).getPaymentDetails(any(), any(), any(), any())(any[HeaderCarrier])

      verify(mockGpaDescriptionService, times(1)).gpaPaymentDescription(any(), any(), any())(any[Messages])

      verify(mockConnector, times(1)).getPaymentDetails(any(), any(), any(), any())(any[HeaderCarrier])

    }
    "delegate to connector and successfully return GroupPaymentArrangementViewModel with appropriate description for Payslip, status for L" in new BaseSetup {
      when(mockConnector.getPaymentDetails(any(), any(), any(), any())(any[HeaderCarrier]))
        .thenReturn(Future.successful(responsePayslipReallocation))

      when(mockGpaDescriptionService.gpaPaymentDescription(any(), any(), any())(any[Messages]))
        .thenReturn("HMRC Credit")

      val result: GroupPaymentArrangementViewModel =
        service.getViewModel(taxPayerReference, accPeriod, 0, 20).futureValue

      result shouldBe viewModelResponsePayslipReallocation

      result.records.head.description shouldBe "HMRC Credit"

      verify(mockConnector).getPaymentDetails(any(), any(), any(), any())(any[HeaderCarrier])

      verify(mockGpaDescriptionService, times(1)).gpaPaymentDescription(any(), any(), any())(any[Messages])

      verify(mockConnector, times(1)).getPaymentDetails(any(), any(), any(), any())(any[HeaderCarrier])

    }
    "delegate to connector and successfully return GroupPaymentArrangementViewModel with appropriate description for Repayment or CancelledPayment status for E" in new BaseSetup {
      when(mockConnector.getPaymentDetails(any(), any(), any(), any())(any[HeaderCarrier]))
        .thenReturn(Future.successful(responseRepaymentReallocation))

      when(mockGpaDescriptionService.gpaPaymentDescription(any(), any(), any())(any[Messages]))
        .thenReturn(emptyString)

      val result: GroupPaymentArrangementViewModel =
        service.getViewModel(taxPayerReference, accPeriod, 0, 20).futureValue

      result shouldBe viewModelRepaymentReallocation

      result.records.head.description shouldBe emptyString

      verify(mockConnector).getPaymentDetails(any(), any(), any(), any())(any[HeaderCarrier])

      verify(mockGpaDescriptionService, times(1)).gpaPaymentDescription(any(), any(), any())(any[Messages])

      verify(mockConnector, times(1)).getPaymentDetails(any(), any(), any(), any())(any[HeaderCarrier])

    }
    "propagate any errors or exceptions from connector" in new BaseSetup {
      when(mockConnector.getPaymentDetails(any(), any(), any(), any())(any[HeaderCarrier]))
        .thenReturn(Future.failed(new RuntimeException("Boom")))

      when(mockGpaDescriptionService.gpaPaymentDescription(any(), any(), any())(any[Messages]))
        .thenReturn(emptyString)

      val exception: Exception = intercept[Exception] {
        service.getViewModel(taxPayerReference, accPeriod, 0, 20).futureValue
      }

      exception.getMessage should include("Boom")

      verify(mockConnector).getPaymentDetails(any(), any(), any(), any())(any[HeaderCarrier])
      verifyNoInteractions(mockGpaDescriptionService)

    }

  }

}
