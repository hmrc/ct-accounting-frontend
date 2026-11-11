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

package viewmodels.payments

import helpers.payments.PaymentAllocationHelper
import org.scalatest.matchers.must.Matchers.mustBe
import org.scalatest.wordspec.AnyWordSpec
import play.api.i18n.Messages
import play.api.test.Helpers.stubMessages
import viewmodels.payments.PaymentAllocationViewModel.convertToViewModel

class PaymentAllocationViewModelSpec extends AnyWordSpec with PaymentAllocationHelper {

  implicit val messages: Messages = stubMessages()
  val page                        = 1
  val pageUrl: Int => String      = page => s"/ct-accounting/find-your-payment/payment-results/payment-allocation?page=$page"

  "PaymentAllocationViewModelRow.convertToViewModel" should {
    "create the expected rows in PaymentAllocationViewModel from PaymentAllocation" in {
      val viewModel = convertToViewModel(paymentAllocation, pageUrl, Some(page))

      viewModel mustBe paymentAllocationToVM(paymentAllocation, pageUrl)

    }

    "create the expected non paginated for rows in PaymentAllocationViewModel from PaymentAllocation" in {
      val viewModel = convertToViewModel(nonPaginatedPaymentAllocation, pageUrl, Some(page))

      viewModel mustBe paymentAllocationToVM(nonPaginatedPaymentAllocation, pageUrl)
    }

    "create the expected paginated for rows in PaymentAllocationViewModel from PaymentAllocation" in {
      val viewModel = convertToViewModel(paginatedPaymentAllocation, pageUrl, Some(page))

      viewModel mustBe paymentAllocationToVM(paginatedPaymentAllocation, pageUrl)
    }
  }
}
