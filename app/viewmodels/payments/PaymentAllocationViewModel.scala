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

import uk.gov.hmrc.govukfrontend.views.Aliases.Pagination
import models.AdjustmentTransactionType.*
import models.gpa.PaymentAllocationDetails
import models.payments.{ElectronicPaymentAllocationDetails, PaymentAllocationChequeDetails}
import play.api.i18n.Messages
import play.api.mvc.Call
import utils.{ApportionmentHelper, PaginationHelper, StatusHelper}
import viewmodels.Paginated
import views.ViewUtils.*

case class ElectronicPaymentAllocationViewModel(
  rows: List[ElectronicPaymentAllocationDetailsViewModelRow],
  page: Paginated[ElectronicPaymentAllocationDetailsViewModelRow],
  pagination: Option[Pagination],
  paginationInfo: Option[String]
)


case class ElectronicPaymentAllocationViewModelRow(
  paymentAllocationDate: String,
  accountingPeriodEnding: String,
  amount: String,
  isLink: Boolean,
  href: Option[Call]
)


case class ChequePaymentAllocationViewModelRow(
  paymentAllocationDate: String,
  accountingPeriodEnding: String,
  amount: String,
  isLink: Boolean,
  href: Option[Call]
)

case class ChequePaymentAllocationViewModel(
  rows: List[ChequePaymentAllocationViewModelRow],
  page: Paginated[ChequePaymentAllocationViewModelRow],
  pagination: Option[Pagination],
  paginationInfo: Option[String]
)

object PaymentAllocationViewModel {

  def convertToViewModel(
    paymentAllocation: List[Option[Either[ElectronicPaymentAllocationDetails, PaymentAllocationChequeDetails]]],
    pageUrl: Int => String,
    requestedPage: Option[Int]
  )(implicit messages: Messages): List[Option[Either[ElectronicPaymentAllocationViewModel, ChequePaymentAllocationViewModel]]] = {

    paymentAllocation.map {
      case Some(Left(electronic)) =>
        Some(Left(ElectronicPaymentAllocationViewModel(
          rows = electronic.electronicPaymentAllocationDetails.map { e =>
            ElectronicPaymentAllocationViewModelRow(
              paymentAllocationDate = formatDate(e.effectiveDateOfPayment, messages.lang),
              accountingPeriodEnding = formatDate(e.apEndDate, messages.lang),
              amount = formatCurrency(e.amount),
              isLink = false,
              href = None
            )
          }
        )))
      case Some(Right(cheque)) =>
        Some(Right(ChequePaymentAllocationViewModel(
          rows = cheque.paymentAllocationDetails.map { c =>
            ChequePaymentAllocationViewModelRow(
              paymentAllocationDate = formatDate(c.effectiveDateOfPayment, messages.lang),
              accountingPeriodEnding = formatDate(c.apEndDate, messages.lang),
              amount = formatCurrency(c.amount),
              isLink = false,
              href = None
            )
          }
        )))
    }
    )
  }

  private def isHyperLink(amount: BigDecimal): Boolean =
    amount != BigDecimal(0.00)
}
