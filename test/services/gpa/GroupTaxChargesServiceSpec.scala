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

import connectors.gpa.GroupTaxChargesConnector
import helpers.gpa.GroupTaxChargesHelper
import models.gpa.GpaGroupTaxCharges
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

class GroupTaxChargesServiceSpec
    extends AnyWordSpec
    with Matchers
    with ScalaFutures
    with MockitoSugar
    with GroupTaxChargesHelper {

  private trait BaseSetup {
    implicit val hc: HeaderCarrier = HeaderCarrier()

    private val cc: ControllerComponents = stubControllerComponents()
    implicit val ec: ExecutionContext    = cc.executionContext

    val mockConnector: GroupTaxChargesConnector = mock[GroupTaxChargesConnector]
    val service                                 = new GroupTaxChargesService(mockConnector)
    val gpaUTR: Long                            = 1L
    val contractVersion: Int                    = 1
    val startIndex: Int                         = 0
    val count: Int                              = 10
  }

  "GroupTaxChargesService.getGpaGroupTaxCharges" should {

    "delegate to connector and successfully return GroupTaxChargesService response" in new BaseSetup {

      when(
        mockConnector.getGpaGroupTaxCharges(eqTo(gpaUTR), eqTo(contractVersion), eqTo(startIndex), eqTo(count))(
          any[HeaderCarrier]
        )
      )
        .thenReturn(Future.successful(gpaGroupTaxChargesWithTwoParticipators))

      val result: GpaGroupTaxCharges =
        service.getGpaGroupTaxCharges(gpaUTR, contractVersion, startIndex, count).futureValue

      result shouldBe gpaGroupTaxChargesWithTwoParticipators

      verify(mockConnector).getGpaGroupTaxCharges(gpaUTR, contractVersion, startIndex, count)

      verify(mockConnector, times(1)).getGpaGroupTaxCharges(gpaUTR, contractVersion, startIndex, count)

    }

    "propagate any errors or exceptions from connector" in new BaseSetup {

      when(
        mockConnector.getGpaGroupTaxCharges(eqTo(gpaUTR), eqTo(contractVersion), eqTo(startIndex), eqTo(count))(
          any[HeaderCarrier]
        )
      )
        .thenReturn(Future.failed(new RuntimeException("Error")))

      val ex: RuntimeException = intercept[RuntimeException] {
        service.getGpaGroupTaxCharges(gpaUTR, contractVersion, startIndex, count).futureValue
      }

      ex.getMessage should include("Error")

      verify(mockConnector, times(1)).getGpaGroupTaxCharges(gpaUTR, contractVersion, startIndex, count)

    }

  }

}
