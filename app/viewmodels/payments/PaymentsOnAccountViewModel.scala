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

import models.payments.PaymentsOnAccountResponse
import play.api.i18n.Messages
import uk.gov.hmrc.govukfrontend.views.Aliases.Pagination
import utils.PaginationHelper
import viewmodels.Paginated
import views.ViewUtils.formatDate
import utils.PaymentsDescriptionHelper.getPaymentsDescription;

import java.time.LocalDate

case class PaymentsOnAccountViewModel(
  paymentsOnAccountRows: Seq[PaymentsOnAccountRow],
  page: Paginated[PaymentsOnAccountRow],
  pagination: Option[Pagination],
  paginationInfo: Option[String],
  totalNumOfRecords: Int
)

case class PaymentsOnAccountRow(
  paymentDate: LocalDate,
  paymentAllocationDate: Option[LocalDate],
  description: String,
  amount: BigDecimal
)(implicit messages: Messages) {
  val paymentDateAsString: String = formatDate(paymentDate, messages.lang)

  val paymentAllocationDateAsString: String = paymentAllocationDate.map(formatDate(_, messages.lang)).getOrElse("")
}

object PaymentsOnAccountViewModel {
  def toViewModel(
    paymentsOnAccountResponse: PaymentsOnAccountResponse,
    requestedPage: Option[Int],
    pageUrl: Int => String
  )(implicit messages: Messages): PaymentsOnAccountViewModel = {
    val rows = paymentsOnAccountResponse.paymentsOnAccountRows.map { row =>
      PaymentsOnAccountRow(
        paymentDate = row.effectiveDateOfPayment,
        paymentAllocationDate = row.apEndDate,
        description = getPaymentsDescription(row.paymentType),
        amount = row.postingAmount
      )
    }

    val page = Paginated.alreadyPaginated(rows, requestedPage, paymentsOnAccountResponse.totalNumOfRecords)

    PaymentsOnAccountViewModel(
      paymentsOnAccountRows = page.items,
      page = page,
      pagination = PaginationHelper.toPagination(page, pageUrl),
      paginationInfo = PaginationHelper.paginationText(page),
      totalNumOfRecords = paymentsOnAccountResponse.totalNumOfRecords
    )
  }
}
