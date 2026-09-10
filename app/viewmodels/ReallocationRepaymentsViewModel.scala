package viewmodels

import views.ViewUtils.{formatCurrency, formatDate}
import uk.gov.hmrc.govukfrontend.views.Aliases.TableRow
import uk.gov.hmrc.govukfrontend.views.viewmodels.content.Text
import models.{Reallocations, Repayments}
import play.api.i18n.Lang

import java.time.LocalDate

  case class ReallocationRepaymentsViewModelRow (
                                                date: LocalDate,
                                                description: String,
                                                amount: BigDecimal)

case class ReallocationRepaymentsViewModel (accountingPeriodEnd: LocalDate,
                                            rows: List[ReallocationRepaymentsViewModelRow]) {
  val accountingPeriodEndAsString: String = formatDate(accountingPeriodEnd, Lang.defaultLang)

  val total: BigDecimal = rows.map(_.amount).sum

  val totalAsString: String = formatCurrency(total)
  
  def totalRow(label: String, total: String, blankCells: Int): Seq[TableRow] =
    TableRow(content = Text(label), classes = "govuk-!-font-weight-bold") +:
      Seq.fill(blankCells)(TableRow(content = Text(""))) :+
      TableRow(content = Text(total), classes = "govuk-!-font-weight-bold")
}
