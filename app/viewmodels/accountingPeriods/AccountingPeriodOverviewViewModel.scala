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

package viewmodels.accountingPeriods

import models.AccountingPeriodOverview
import play.api.i18n.Messages
import play.api.mvc.Call
import uk.gov.hmrc.govukfrontend.views.Aliases.{TableRow, Text}
import views.ViewUtils.formatCurrency
import controllers.routes

import java.time.LocalDate

case class AccountingPeriodOverviewRow(
  description: String,
  amount: BigDecimal,
  isLink: Option[Boolean],
  href: Option[Call],
  classes: String = ""
) {
  val amountAsString: String = formatCurrency(amount)
}

case class AccountingPeriodOverviewViewModel(total: BigDecimal, accountingPeriodEndDate: LocalDate, rows: Seq[AccountingPeriodOverviewRow]) {


  def totalRow(label: String): Seq[TableRow] =
    Seq(
      TableRow(content = Text(label), classes = "govuk-!-font-weight-bold"),
      TableRow(content = Text(formatCurrency(total)), classes = "govuk-!-font-weight-bold govuk-table__cell govuk-table__cell--numeric")
    )

}

object AccountingPeriodOverviewViewModel {
  def toViewModel(
    accountingPeriodEndDate: LocalDate,
    accountingPeriodOverview: AccountingPeriodOverview
  )(implicit messages: Messages): AccountingPeriodOverviewViewModel = {
    val subTotal = accountingPeriodOverview.taxTotal + accountingPeriodOverview.interestTotal + accountingPeriodOverview.penaltyTotal
    AccountingPeriodOverviewViewModel(
      total = accountingPeriodOverview.taxTotal + accountingPeriodOverview.interestTotal + accountingPeriodOverview.penaltyTotal + accountingPeriodOverview.payslipTotal + accountingPeriodOverview.repayReallocTotal + accountingPeriodOverview.adjustmentTotal,
      accountingPeriodEndDate = accountingPeriodEndDate,
      rows = (
        Option.when(accountingPeriodOverview.taxIsDisplayNeededFlag){ AccountingPeriodOverviewRow(
          description = messages("accountingPeriodOverview.taxes"),
          amount = accountingPeriodOverview.taxTotal,
          isLink = Some(isHyperLink(accountingPeriodOverview.taxTotal)),
          href = Some(routes.TaxTransactionsController.onPageLoad())
      ) } ++
        Option.when(accountingPeriodOverview.interestIsDisplayNeededFlag){ AccountingPeriodOverviewRow(
          description = messages("accountingPeriodOverview.interest"),
          amount = accountingPeriodOverview.interestTotal,
          isLink = Some(isHyperLink(accountingPeriodOverview.interestTotal)),
          href = Some(routes.InterestController.onPageLoad())
        ) } ++
          Seq(
            AccountingPeriodOverviewRow(
            description = messages("accountingPeriodOverview.penalties"),
            amount = accountingPeriodOverview.penaltyTotal,
            isLink = Some(isHyperLink(accountingPeriodOverview.penaltyTotal)),
            href = Some(routes.PenaltiesAccountingPeriodController.onPageLoad())
          ),
            AccountingPeriodOverviewRow(
            description = messages("accountingPeriodOverview.subtotal"),
            amount = subTotal,
            isLink = None,
            href = None,
            classes = "govuk-!-font-weight-bold"
          )
          ) ++
          Option.when(accountingPeriodOverview.paymentIsDisplayNeededFlag) { AccountingPeriodOverviewRow(
            description = messages("accountingPeriodOverview.payments"),
            amount = accountingPeriodOverview.payslipTotal,
            isLink = Some(isHyperLink(accountingPeriodOverview.payslipTotal)),
            href = Some(routes.PaymentsController.onPageLoad())
          )} ++
          Option.when(accountingPeriodOverview.repayReallocIsDisplayNeededFlag) { AccountingPeriodOverviewRow(
            description = messages("accountingPeriodOverview.repayments"),
            amount = accountingPeriodOverview.repayReallocTotal,
            isLink = Some(isHyperLink(accountingPeriodOverview.repayReallocTotal)),
            href = Some(routes.RepaymentsReallocationsController.onPageLoad())
          )} ++
          Seq(
            AccountingPeriodOverviewRow(
              description = messages("accountingPeriodOverview.adjustments"),
              amount = accountingPeriodOverview.adjustmentTotal,
              isLink = Some(isHyperLink(accountingPeriodOverview.adjustmentTotal)),
              href = Some(routes.AdjustmentsAccountingPeriodController.onPageLoad())
            ),
          )
        ).toSeq

    )
  }

  private def isHyperLink(amount: BigDecimal): Boolean =
    amount != BigDecimal(0.00)
}
