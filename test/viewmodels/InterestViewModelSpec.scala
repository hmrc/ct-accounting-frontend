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

package viewmodels

import helpers.AccountingPeriodResponseHelper
import org.scalatest.matchers.must.Matchers
import org.scalatest.matchers.should.Matchers.should
import org.scalatest.wordspec.AnyWordSpec
import play.api.i18n.Messages
import play.api.mvc.Call
import play.api.test.Helpers.stubMessages
import uk.gov.hmrc.http.HttpVerbs.GET
import views.ViewUtils.formatCurrency

class InterestViewModelSpec extends AnyWordSpec with Matchers with AccountingPeriodResponseHelper {

  implicit val messages: Messages = stubMessages()

  "InterestViewModel.toViewModel" should {

    "create the expected rows and descriptions when LpiCalcFlag, creditDebitCalcFlag and clericalCalculationFlag are true " in {
      val clericalCalculationFlag: Boolean = true

      val viewModel       = InterestViewModel.toViewModel(accountingPeriodResponseWithAllFlagsTrue, clericalCalculationFlag)
      val dummyCall: Call = Call(GET, "/")

      viewModel.rows mustBe Seq(
        InterestRow(
          description = messages("interest.table.latePayment.accruing"),
          amount = BigDecimal("25.50"),
          isLink = false,
          href = dummyCall
        ),
        InterestRow(
          description = messages("interest.table.repaymentInterest"),
          amount = BigDecimal("0.00"),
          isLink = false,
          href = dummyCall
        ),
        InterestRow(
          description = messages("interest.table.debitInterest.accruing"),
          amount = BigDecimal("15.75"),
          isLink = false,
          href = dummyCall
        ),
        InterestRow(
          description = messages("interest.table.creditInterest.accruing"),
          amount = BigDecimal("0.00"),
          isLink = false,
          href = dummyCall
        )
      )
    }
    "create the expected rows and descriptions when LpiCalcFlag, creditDebitCalcFlag and clericalCalculationFlag are false " in {
      val clericalCalculationFlag: Boolean = false

      val viewModel       = InterestViewModel.toViewModel(accountingPeriodResponseWithAllFlagsFalse, clericalCalculationFlag)
      val dummyCall: Call = Call(GET, "/")

      viewModel.rows mustBe Seq(
        InterestRow(
          description = messages("interest.table.latePayment"),
          amount = BigDecimal("25.50"),
          isLink = true,
          href = dummyCall
        ),
        InterestRow(
          description = messages("interest.table.repaymentInterest"),
          amount = BigDecimal("16.00"),
          isLink = true,
          href = dummyCall
        ),
        InterestRow(
          description = messages("interest.table.debitInterest"),
          amount = BigDecimal("15.75"),
          isLink = true,
          href = dummyCall
        ),
        InterestRow(
          description = messages("interest.table.creditInterest"),
          amount = BigDecimal("0.00"),
          isLink = false,
          href = dummyCall
        )
      )
    }
    "create the expected rows and descriptions without hyperlink when LpiCalcFlag, creditDebitCalcFlag and clericalCalculationFlag are true all amounts are zero" in {
      val clericalCalculationFlag: Boolean = true
      val viewModel                        =
        InterestViewModel.toViewModel(accountingPeriodResponseWithAllFlagsTrueAmountZero, clericalCalculationFlag)
      val dummyCall: Call                  = Call(GET, "/")

      viewModel.rows mustBe Seq(
        InterestRow(
          description = messages("interest.table.latePayment.accruing"),
          amount = BigDecimal("0.00"),
          isLink = false,
          href = dummyCall
        ),
        InterestRow(
          description = messages("interest.table.repaymentInterest"),
          amount = BigDecimal("0.00"),
          isLink = false,
          href = dummyCall
        ),
        InterestRow(
          description = messages("interest.table.debitInterest.accruing"),
          amount = BigDecimal("0.00"),
          isLink = false,
          href = dummyCall
        ),
        InterestRow(
          description = messages("interest.table.creditInterest.accruing"),
          amount = BigDecimal("0.00"),
          isLink = false,
          href = dummyCall
        )
      )
    }
    "calculate the total correctly" in {
      val clericalCalculationFlag: Boolean = false
      val viewModel                        = InterestViewModel.toViewModel(accountingPeriodResponseWithAllFlagsTrue, clericalCalculationFlag)

      viewModel.total mustBe BigDecimal("41.25")
    }
    "format the total correctly" in {

      val clericalCalculationFlag: Boolean = false

      val viewModel = InterestViewModel.toViewModel(accountingPeriodResponseWithAllFlagsTrue, clericalCalculationFlag)

      viewModel.totalAsString mustBe formatCurrency(BigDecimal("41.25"))
    }
    "format each row amount correctly" in {
      val clericalCalculationFlag: Boolean = false
      val viewModel                        = InterestViewModel.toViewModel(accountingPeriodResponseWithAllFlagsTrue, clericalCalculationFlag)
      viewModel.rows.map(_.amountAsString) mustBe Seq(
        formatCurrency(BigDecimal("25.50")),
        formatCurrency(BigDecimal("0.00")),
        formatCurrency(BigDecimal("15.75")),
        formatCurrency(BigDecimal("0.00"))
      )
    }
  }
}
