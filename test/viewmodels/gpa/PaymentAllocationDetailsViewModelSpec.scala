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

package viewmodels.gpa

import helpers.gpa.PaymentAllocationDetailsHelper
import org.scalatest.wordspec.AnyWordSpec
import play.api.i18n.Messages
import play.api.test.Helpers.stubMessages
import controllers.routes
import org.scalatest.matchers.must.Matchers.mustBe
import viewmodels.gpa.PaymentAllocationDetailsViewModel.convertToViewModel

class PaymentAllocationDetailsViewModelSpec extends AnyWordSpec with PaymentAllocationDetailsHelper {

  implicit val messages: Messages = stubMessages()

  "AccountingPeriodOverviewRow.toViewModel" should {
    "create the expected rows in AccountingPeriodOverviewViewModel from AccountingPeriodOverview" in {
      val viewModel = convertToViewModel(fullPaymentAllocationDetails)

      viewModel.rows mustBe
        List(PaymentAllocationDetailsViewModelRow(
          rowName = messages("paymentAllocationDetails.gpaReference"),
          content = "10",
          isLink = false,
          href = None
        ),
          PaymentAllocationDetailsViewModelRow(
            rowName = messages("paymentAllocationDetails.participatingCompany"),
            content = "ABC Limited",
            isLink = false,
            href = None
          ),
          PaymentAllocationDetailsViewModelRow(
            rowName = messages("paymentAllocationDetails.accountingPeriodEnding"),
            content = "24 Jul 2024",
            isLink = false,
            href = None
          ),
          PaymentAllocationDetailsViewModelRow(
            rowName = messages("paymentAllocationDetails.periodAccountEnding"),
            content = "24 Jul 2024",
            isLink = false,
            href = None
          ),
          PaymentAllocationDetailsViewModelRow(
            rowName = messages("paymentAllocationDetails.groupPeriodAccountStatus"),
            content = "O",
            isLink = false,
            href = None
          ),
          PaymentAllocationDetailsViewModelRow(
            rowName = messages("paymentAllocationDetails.taxCharge"),
            content = "£10.00",
            isLink = false,
            href = None
          ),
          PaymentAllocationDetailsViewModelRow(
            rowName = messages("paymentAllocationDetails.paymentAllocatedBy"),
            content = "O",
            isLink = false,
            href = None
          ),
          PaymentAllocationDetailsViewModelRow(
            rowName = messages("paymentAllocationDetails.paymentAllocated"),
            content = "£10.00",
            isLink = false,
            href = None
          ),
          PaymentAllocationDetailsViewModelRow(
            rowName = messages("paymentAllocationDetails.groupTaxTotal"),
            content = "£10.00",
            isLink = true,
            href = Some(routes.TaxTransactionsController.onPageLoad())
          ))

    }
  }
}
