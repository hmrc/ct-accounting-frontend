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

class GroupPaymentArrangementViewModelSpec extends AnyWordSpec with Matchers with GroupPaymentsHelper {
  implicit val messages: Messages = stubMessages()

  "GroupPaymentArrangementViewModel.toViewModel" should {
    "check format for each amount in payments and taxes from the response" in {
      val viewModel = GroupPaymentArrangementViewModel.toViewModel(groupPaymentDetailsResponse)

      viewModel.rows(0).groupPayments.amount mustBe -11.01
      viewModel.rows(0).groupPayments.amountAsString mustBe "-£11.01"
      viewModel.rows(0).groupTaxes.amount mustBe 13.02
      viewModel.rows(0).groupTaxes.amountAsString mustBe "-£13.02"

      viewModel.rows(1).groupPayments.amount mustBe -430.32
      viewModel.rows(1).groupPayments.amountAsString mustBe "-£430.32"
      viewModel.rows(1).groupTaxes.amount mustBe -700.53
      viewModel.rows(1).groupTaxes.amountAsString mustBe "-£700.53"

      viewModel.rows(2).groupPayments.amount mustBe -952.12
      viewModel.rows(2).groupPayments.amountAsString mustBe "-£952.12"
      viewModel.rows(2).groupTaxes.amount mustBe -691.76
      viewModel.rows(2).groupTaxes.amountAsString mustBe "-£691.76"
    }

    "check formatting for each date from the response" in {
      val viewModel = GroupPaymentArrangementViewModel.toViewModel(groupPaymentDetailsResponse)

      viewModel.rows(0).periodOfAccountEnding mustBe LocalDate.of(2026,1,1)

      viewModel.rows(1).periodOfAccountEnding mustBe LocalDate.of(2026,5,4)

      viewModel.rows(2).periodOfAccountEnding mustBe LocalDate.of(2026,8,12)
    }

    "get the correct status" in {
      val viewModel = GroupPaymentArrangementViewModel.toViewModel(groupPaymentDetailsResponse)

      viewModel.rows(0).status mustBe "ACTIVE"

      viewModel.rows(1).status mustBe "CLOSED"

      viewModel.rows(2).status mustBe "CLOSED"
    }

  }
}
