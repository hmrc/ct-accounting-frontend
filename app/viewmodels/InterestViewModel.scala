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

import models.AccountingPeriodDetailsResponse
import play.api.i18n.Messages
import play.api.mvc.Call
import uk.gov.hmrc.govukfrontend.views.Aliases.Text
import uk.gov.hmrc.govukfrontend.views.viewmodels.table.*
import uk.gov.hmrc.http.HttpVerbs.GET
import views.ViewUtils.formatCurrency

case class InterestRow(description: String, amount: BigDecimal, isLink:Boolean, href: Call) {
  val amountAsString: String = formatCurrency(amount)
}

case class InterestViewModel(rows: Seq[InterestRow]) {
  val total: BigDecimal = rows.map(_.amount).sum

  val totalAsString: String = formatCurrency(total)

  def totalRow(total: String, label: String): Seq[TableRow] =
    Seq(
      TableRow(content = Text(label), classes = "govuk-!-font-weight-bold"),
      TableRow(content = Text(total), classes = "govuk-!-font-weight-bold govuk-table__cell govuk-table__cell--numeric")
    )
}

object InterestViewModel {
  // TODO change the call to unique link for each of the rows
  val dummyCall: Call = Call(GET, "/")
  def toViewModel(
    response: AccountingPeriodDetailsResponse,
    clericalCalculationFlag:Boolean
  )(implicit messages: Messages): InterestViewModel =
    val accDetails = response.accountingPeriodDetails
    InterestViewModel(
      rows = Seq(
        InterestRow(
          description = deriveDescription(messages("interest.table.latePayment"),accDetails.lpiCalcFlag),
          amount = accDetails.latePaymentInterestAmount,
          isLink = isHyperLink(accDetails.latePaymentInterestAmount, accDetails.lpiCalcFlag),
          href = dummyCall
        ),
        InterestRow(
          description = messages("interest.table.repaymentInterest"),
          amount = accDetails.repaymentInterestAmount,
          isLink = isHyperLink(accDetails.repaymentInterestAmount,clericalCalculationFlag),
          href = dummyCall
        ),
        InterestRow(
          deriveDescription(messages("interest.table.debitInterest"),accDetails.crDbCalcFlag),
          amount = accDetails.debitInterestAmount,
          isLink = isHyperLink(accDetails.debitInterestAmount, accDetails.crDbCalcFlag),
          href = dummyCall
        ),
        InterestRow(
          description = deriveDescription(messages("interest.table.creditInterest"),accDetails.crDbCalcFlag),
          amount = accDetails.creditInterestAmount,
          isLink = isHyperLink(accDetails.creditInterestAmount, accDetails.crDbCalcFlag),
          href = dummyCall
        )
      )
    )

  private def isHyperLink(amount:BigDecimal, flag:Boolean):Boolean = {
    amount != BigDecimal(0.00) && !flag
  }

  private def deriveDescription(key:String, accruing:Boolean)(implicit messages:Messages):String =
    if(accruing) messages(s"$key.accruing") else messages(key)


}
