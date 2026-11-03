package viewmodels.payments

import controllers.payments.PaymentsOnAccountController
import models.payments.{PaymentsOnAccountResponse, PaymentsOnAccountRow}
import play.api.i18n.Messages
import uk.gov.hmrc.govukfrontend.views.Aliases.{Pagination, Text}
import uk.gov.hmrc.govukfrontend.views.viewmodels.summarylist.Key
import utils.{PaginationHelper, StatusHelper}
import viewmodels.Paginated
import views.ViewUtils.{formatCurrency, formatDate}

import java.time.LocalDate

case class PaymentsOnAccountViewModel(
  paymentsOnAccountRows: Seq[PaymentsOnAccountRow],
  page: Paginated[PaymentsOnAccountRow],
  pagination: Option[Pagination],
  paginationInfo: Option[String]
)

case class PaymentsOnAccountRow(
  paymentDate: LocalDate,
  paymentAllocationDate: LocalDate,
  description: String,
  amount: BigDecimal
)

object PaymentsOnAccountViewModel {
  def toViewModel(
    paymentsOnAccountResponse: PaymentsOnAccountResponse,
    requestedPage: Option[Int],
    pageUrl: Int => String
  )(implicit messages: Messages): PaymentsOnAccountViewModel = {
    val rows = paymentsOnAccountResponse.paymentsOnAccountRows.map { row =>
      PaymentsOnAccountRow(
        paymentDate = row.paymentDate,
        paymentAllocationDate = row.paymentAllocationDate,
        description = row.description,
        amount = row.amount
      )
    }

    val page = Paginated.alreadyPaginated(rows, requestedPage, paymentsOnAccountResponse.totalNumberRows.getOrElse(0))

    PaymentsOnAccountViewModel(
      paymentsOnAccountRows = rows,
      page = page,
      pagination = PaginationHelper.toPagination(page, pageUrl),
      paginationInfo = PaginationHelper.paginationText(page)
    )
  }
}
