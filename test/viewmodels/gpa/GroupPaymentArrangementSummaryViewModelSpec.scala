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

import helpers.gpa.GroupPaymentsHelper
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec
import play.api.i18n.Messages
import play.api.test.Helpers.stubMessages

import java.time.LocalDate

class GroupPaymentArrangementSummaryViewModelSpec extends AnyWordSpec with Matchers with GroupPaymentsHelper {
  implicit val messages: Messages = stubMessages()

  "GroupPaymentArrangementViewModel.toViewModel" should {
    "check format for each amount in payments and taxes from the response" in {
      val viewModel = GroupPaymentArrangementViewModel.toViewModel(groupPaymentDetailsResponse)

      viewModel.rows(0).groupPayments.amount mustBe -13.02
      viewModel.rows(0).groupPayments.amountAsString mustBe "-£13.02"
      viewModel.rows(0).groupTaxes.amount mustBe -11.01
      viewModel.rows(0).groupTaxes.amountAsString mustBe "-£11.01"

      viewModel.rows(1).groupPayments.amount mustBe 742.16
      viewModel.rows(1).groupPayments.amountAsString mustBe "£742.16"
      viewModel.rows(1).groupTaxes.amount mustBe -532.86
      viewModel.rows(1).groupTaxes.amountAsString mustBe "-£532.86"

      viewModel.rows(2).groupPayments.amount mustBe -217.62
      viewModel.rows(2).groupPayments.amountAsString mustBe "-£217.62"
      viewModel.rows(2).groupTaxes.amount mustBe -384.09
      viewModel.rows(2).groupTaxes.amountAsString mustBe "-£384.09"

      viewModel.rows(3).groupPayments.amount mustBe 12.73
      viewModel.rows(3).groupPayments.amountAsString mustBe "£12.73"
      viewModel.rows(3).groupTaxes.amount mustBe 832.52
      viewModel.rows(3).groupTaxes.amountAsString mustBe "£832.52"

      viewModel.rows(4).groupPayments.amount mustBe -431.56
      viewModel.rows(4).groupPayments.amountAsString mustBe "-£431.56"
      viewModel.rows(4).groupTaxes.amount mustBe 743.32
      viewModel.rows(4).groupTaxes.amountAsString mustBe "£743.32"

    }

    "check formatting for each date from the response" in {
      val viewModel = GroupPaymentArrangementViewModel.toViewModel(groupPaymentDetailsResponse)

      viewModel.rows(0).periodOfAccountEnding mustBe LocalDate.of(2026, 1, 7)

      viewModel.rows(1).periodOfAccountEnding mustBe LocalDate.of(2025, 1, 4)

      viewModel.rows(2).periodOfAccountEnding mustBe LocalDate.of(2024, 1, 9)

      viewModel.rows(3).periodOfAccountEnding mustBe LocalDate.of(2023, 1, 7)

      viewModel.rows(4).periodOfAccountEnding mustBe LocalDate.of(2022, 1, 12)
    }

    "get the correct status" in {
      val viewModel = GroupPaymentArrangementViewModel.toViewModel(groupPaymentDetailsResponse)

      viewModel.rows(0).status mustBe messages("groupPaymentsArrangements.status.open")

      viewModel.rows(1).status mustBe messages("groupPaymentsArrangements.status.earlierCleared")

      viewModel.rows(2).status mustBe messages("groupPaymentsArrangements.status.earlierCleared")

      viewModel.rows(3).status mustBe messages("groupPaymentsArrangements.status.earlierCleared")

      viewModel.rows(4).status mustBe messages("groupPaymentsArrangements.status.earlierCleared")
    }

  }
}
