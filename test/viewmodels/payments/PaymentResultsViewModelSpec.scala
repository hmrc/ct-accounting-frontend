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

import helpers.payments.PaymentResultsHelper
import org.scalatest.matchers.must.Matchers
import org.scalatest.matchers.must.Matchers.mustBe
import org.scalatest.wordspec.AnyWordSpec
import viewmodels.Paginated

import utils.PaginationHelper
import viewmodels.payments.PaymentResultsViewModel.toViewModel

class PaymentResultsViewModelSpec extends AnyWordSpec with Matchers with PaymentResultsHelper {

  val taxRef: Long           = 1111444444L
  val page                   = 1
  val pageUrl: Int => String = page => s"/ct-accounting/find-your-payment/payment-results?page=$page"

  "PaymentResultsViewModel.toViewModel" should {
    "create the expected rows in PaymentResultsViewModel from Payment Search Results" in {
      val viewModel = toViewModel(multiplePaymentSearchResult, taxRef, pageUrl, Some(1))

      viewModel.rows mustBe multiplePaymentSearchResultRows.take(10)
      viewModel.rows.size mustBe 10
    }

    "not create rows in PaymentResultsViewModel from empty Payment Search Results" in {
      val viewModel = toViewModel(emptyPaymentSearchResult, taxRef, pageUrl, Some(1))

      viewModel.rows.size mustBe 0
    }

    "create the expected pagination for rows in PaymentResultsViewModel from Payment Search Result" in {
      val viewModel = toViewModel(multiplePaymentSearchResult, taxRef, pageUrl, Some(2))

      viewModel.page mustBe
        Paginated.paginate(
          multiplePaymentSearchResultRows,
          Some(2),
          10
        )

      viewModel.pagination mustEqual
        PaginationHelper.toPagination(viewModel.page, pageUrl)

      viewModel.paginationInfo mustEqual
        PaginationHelper.paginationText(viewModel.page)
    }

  }
}
