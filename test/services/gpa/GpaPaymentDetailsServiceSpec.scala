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

import helpers.gpa.GpaPaymentDetailsHelper
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.mockito.MockitoSugar
import play.api.i18n.Messages
import play.api.test.Helpers.stubMessages

import java.time.LocalDate

class GpaPaymentDetailsServiceSpec
    extends AnyWordSpec
    with Matchers
    with ScalaFutures
    with MockitoSugar
    with GpaPaymentDetailsHelper {

  private trait BaseSetup {
    implicit val messages: Messages = stubMessages()

    val service                    = new GpaPaymentDetailsService()
    val utr: Long                  = 1L
    val contractEndDate: LocalDate = LocalDate.of(2026, 1, 1)

  }

  "GpaPaymentDetailsService.GpaPaymentDescription" should {

    "Return miscellaneous transfer when participator is false for rfr and rto payments" in new BaseSetup {

      val resultRFR: String =
        service.gpaPaymentDescription(gpaPaymentsDefault.copy(participatorPresent = Some(false)), utr, contractEndDate)

      val resultRTO: String =
        service.gpaPaymentDescription(
          gpaPaymentsDefault.copy(tablename = Some("RTOReallocation"), participatorPresent = Some(false)),
          utr,
          contractEndDate
        )

      resultRFR shouldBe "gpaPayment.Description.miscellaneous"

      resultRTO shouldBe "gpaPayment.Description.miscellaneous"

    }

    "Return miscellaneous transfer when participator is None for rfr and rto payments" in new BaseSetup {

      val resultRFR: String =
        service.gpaPaymentDescription(gpaPaymentsDefault.copy(participatorPresent = None), utr, contractEndDate)

      val resultRTO: String =
        service.gpaPaymentDescription(
          gpaPaymentsDefault.copy(tablename = Some("RTOReallocation"), participatorPresent = None),
          utr,
          contractEndDate
        )

      resultRFR shouldBe "gpaPayment.Description.miscellaneous"

      resultRTO shouldBe "gpaPayment.Description.miscellaneous"

    }

    "Return miscellaneous transfer when target accounting period is 0 for rfr and rto payments" in new BaseSetup {

      val resultRFR: String =
        service.gpaPaymentDescription(gpaPaymentsDefault.copy(targetApNo = Some(0)), utr, contractEndDate)

      val resultRTO: String =
        service.gpaPaymentDescription(
          gpaPaymentsDefault.copy(tablename = Some("RTOReallocation"), targetApNo = Some(0)),
          utr,
          contractEndDate
        )

      resultRFR shouldBe "gpaPayment.Description.miscellaneous"

      resultRTO shouldBe "gpaPayment.Description.miscellaneous"

    }

    "Return miscellaneous transfer when target accounting period is None for rfr and rto payments" in new BaseSetup {

      val resultRFR: String =
        service.gpaPaymentDescription(gpaPaymentsDefault.copy(targetApNo = None), utr, contractEndDate)

      val resultRTO: String =
        service.gpaPaymentDescription(
          gpaPaymentsDefault.copy(tablename = Some("RTOReallocation"), targetApNo = None),
          utr,
          contractEndDate
        )

      resultRFR shouldBe "gpaPayment.Description.miscellaneous"

      resultRTO shouldBe "gpaPayment.Description.miscellaneous"

    }

    "Return Reallocation TO {0}, AP ending {1} for rfr payments when reallocation tax reference does not equal utr" in new BaseSetup {

      val result: String =
        service.gpaPaymentDescription(
          gpaPaymentsDefault.copy(targetTaxpayerReference = Some("99")),
          utr,
          contractEndDate
        )

      result shouldBe "gpaPayment.Description.rfr.taxRef"

    }

    "Return Reallocation TO AP ending accountingPeriodEndDate for rfr payments when contractDate is not present" in new BaseSetup {

      val result: String =
        service.gpaPaymentDescription(gpaPaymentsDefault.copy(contractEndDate = None), utr, contractEndDate)

      result shouldBe "gpaPayment.Description.rfr.date"

    }

    "Return Reallocation TO AP ending contractDate for rfr payments when contractDate is present" in new BaseSetup {

      val result: String =
        service.gpaPaymentDescription(gpaPaymentsDefault, utr, contractEndDate)

      result shouldBe "gpaPayment.Description.rfr.date"

    }

    "Return Reallocation FROM {0}, AP ending {1} for rto payments when reallocation tax reference does not equal utr" in new BaseSetup {

      val result: String =
        service.gpaPaymentDescription(
          gpaPaymentsDefault.copy(tablename = Some("RTOReallocation"), targetTaxpayerReference = Some("99")),
          utr,
          contractEndDate
        )

      result shouldBe "gpaPayment.Description.rto.taxRef"

    }

    "Return Reallocation FROM AP ending accountingPeriodEndDate for rto payments when contractDate is not present" in new BaseSetup {

      val result: String =
        service.gpaPaymentDescription(
          gpaPaymentsDefault.copy(tablename = Some("RTOReallocation"), contractEndDate = None),
          utr,
          contractEndDate
        )

      result shouldBe "gpaPayment.Description.rto.date"

    }

    "Return Reallocation FROM AP ending contractDate for rto payments when contractDate is present" in new BaseSetup {

      val result: String =
        service.gpaPaymentDescription(
          gpaPaymentsDefault.copy(tablename = Some("RTOReallocation")),
          utr,
          contractEndDate
        )

      result shouldBe "gpaPayment.Description.rto.date"

    }

    "Return payment description when tableName is Payslip" in new BaseSetup {

      val result: String =
        service.gpaPaymentDescription(gpaPaymentsDefault.copy(tablename = Some("Payslip")), utr, contractEndDate)

      result shouldBe "payments.description.IRC"

    }

    "Return empty string Repayment or CancelledRepayment" in new BaseSetup {

      val resultRepayment: String          =
        service.gpaPaymentDescription(gpaPaymentsDefault.copy(tablename = Some("Repayment")), utr, contractEndDate)
      val resultCancelledRepayment: String =
        service.gpaPaymentDescription(
          gpaPaymentsDefault.copy(tablename = Some("CancelledRepayment")),
          utr,
          contractEndDate
        )

      resultRepayment          shouldBe ""
      resultCancelledRepayment shouldBe ""

    }
  }

}
