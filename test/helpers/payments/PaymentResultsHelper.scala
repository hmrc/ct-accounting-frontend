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

package helpers.payments

import controllers.payments.routes
import models.payments.{PaymentSearchResult, PaymentSearchResultResponse}
import play.api.i18n.Messages
import play.api.test.Helpers.stubMessages
import viewmodels.payments.{PaymentResultsViewModel, PaymentResultsViewModelRow}

import java.time.LocalDate

trait PaymentResultsHelper {

  implicit val messages: Messages = stubMessages()

  val emptyPaymentSearchResult: PaymentSearchResultResponse =
    PaymentSearchResultResponse(paymentSearchResult = List.empty)

  val singlePaymentSearchResult: PaymentSearchResultResponse = PaymentSearchResultResponse(paymentSearchResult =
    List(
      PaymentSearchResult(
        accountingPeriod = 1,
        payslipId = 1,
        amount = 38000,
        effectiveDateOfPayment = Some(LocalDate.of(2007, 7, 14)),
        paymentType = "BAC",
        bankSortCode = 114433,
        bankAccountNumber = 1004005,
        chequeNumber = 100301,
        paymentAmount = -31250,
        chequeId = 916
      )
    )
  )

  val multiplePaymentSearchResult: PaymentSearchResultResponse = PaymentSearchResultResponse(paymentSearchResult =
    List(
      PaymentSearchResult(
        accountingPeriod = 1,
        payslipId = 1,
        amount = 37500,
        effectiveDateOfPayment = Some(LocalDate.of(2007, 7, 14)),
        paymentType = "BAC",
        bankSortCode = 114433,
        bankAccountNumber = 1004005,
        chequeNumber = 100301,
        paymentAmount = -31250,
        chequeId = 916
      ),
      PaymentSearchResult(
        accountingPeriod = 1,
        payslipId = 2,
        amount = -37500,
        effectiveDateOfPayment = Some(LocalDate.of(2007, 7, 14)),
        paymentType = "BGP",
        bankSortCode = 114433,
        bankAccountNumber = 1004005,
        chequeNumber = 100301,
        paymentAmount = -30000,
        chequeId = 917
      ),
      PaymentSearchResult(
        accountingPeriod = 1,
        payslipId = 1,
        amount = 37500,
        effectiveDateOfPayment = Some(LocalDate.of(2007, 7, 14)),
        paymentType = "BAC",
        bankSortCode = 114433,
        bankAccountNumber = 1004005,
        chequeNumber = 100301,
        paymentAmount = -31250,
        chequeId = 916
      ),
      PaymentSearchResult(
        accountingPeriod = 1,
        payslipId = 1,
        amount = 37500,
        effectiveDateOfPayment = Some(LocalDate.of(2007, 7, 14)),
        paymentType = "BAC",
        bankSortCode = 114433,
        bankAccountNumber = 1004005,
        chequeNumber = 100301,
        paymentAmount = -31250,
        chequeId = 916
      ),
      PaymentSearchResult(
        accountingPeriod = 1,
        payslipId = 1,
        amount = 37500,
        effectiveDateOfPayment = Some(LocalDate.of(2007, 7, 14)),
        paymentType = "BAC",
        bankSortCode = 114433,
        bankAccountNumber = 1004005,
        chequeNumber = 100301,
        paymentAmount = -31250,
        chequeId = 916
      ),
      PaymentSearchResult(
        accountingPeriod = 1,
        payslipId = 1,
        amount = 37500,
        effectiveDateOfPayment = Some(LocalDate.of(2007, 7, 14)),
        paymentType = "BAC",
        bankSortCode = 114433,
        bankAccountNumber = 1004005,
        chequeNumber = 100301,
        paymentAmount = -31250,
        chequeId = 916
      ),
      PaymentSearchResult(
        accountingPeriod = 1,
        payslipId = 1,
        amount = 37500,
        effectiveDateOfPayment = Some(LocalDate.of(2007, 7, 14)),
        paymentType = "BAC",
        bankSortCode = 114433,
        bankAccountNumber = 1004005,
        chequeNumber = 100301,
        paymentAmount = -31250,
        chequeId = 916
      ),
      PaymentSearchResult(
        accountingPeriod = 1,
        payslipId = 1,
        amount = 37500,
        effectiveDateOfPayment = Some(LocalDate.of(2007, 7, 14)),
        paymentType = "BAC",
        bankSortCode = 114433,
        bankAccountNumber = 1004005,
        chequeNumber = 100301,
        paymentAmount = -31250,
        chequeId = 916
      ),
      PaymentSearchResult(
        accountingPeriod = 1,
        payslipId = 1,
        amount = 37500,
        effectiveDateOfPayment = Some(LocalDate.of(2007, 7, 14)),
        paymentType = "BAC",
        bankSortCode = 114433,
        bankAccountNumber = 1004005,
        chequeNumber = 100301,
        paymentAmount = -31250,
        chequeId = 916
      ),
      PaymentSearchResult(
        accountingPeriod = 1,
        payslipId = 1,
        amount = 37500,
        effectiveDateOfPayment = Some(LocalDate.of(2007, 7, 14)),
        paymentType = "BAC",
        bankSortCode = 114433,
        bankAccountNumber = 1004005,
        chequeNumber = 100301,
        paymentAmount = -31250,
        chequeId = 916
      ),
      PaymentSearchResult(
        accountingPeriod = 1,
        payslipId = 1,
        amount = 37500,
        effectiveDateOfPayment = Some(LocalDate.of(2007, 7, 14)),
        paymentType = "BAC",
        bankSortCode = 114433,
        bankAccountNumber = 1004005,
        chequeNumber = 100301,
        paymentAmount = -31250,
        chequeId = 916
      ),
      PaymentSearchResult(
        accountingPeriod = 1,
        payslipId = 1,
        amount = 37500,
        effectiveDateOfPayment = Some(LocalDate.of(2007, 7, 14)),
        paymentType = "BAC",
        bankSortCode = 114433,
        bankAccountNumber = 1004005,
        chequeNumber = 100301,
        paymentAmount = -31250,
        chequeId = 916
      ),
      PaymentSearchResult(
        accountingPeriod = 1,
        payslipId = 1,
        amount = 37500,
        effectiveDateOfPayment = Some(LocalDate.of(2007, 7, 14)),
        paymentType = "BAC",
        bankSortCode = 114433,
        bankAccountNumber = 1004005,
        chequeNumber = 100301,
        paymentAmount = -31250,
        chequeId = 916
      ),
      PaymentSearchResult(
        accountingPeriod = 1,
        payslipId = 1,
        amount = 37500,
        effectiveDateOfPayment = Some(LocalDate.of(2007, 7, 14)),
        paymentType = "BAC",
        bankSortCode = 114433,
        bankAccountNumber = 1004005,
        chequeNumber = 100301,
        paymentAmount = -31250,
        chequeId = 916
      ),
      PaymentSearchResult(
        accountingPeriod = 1,
        payslipId = 1,
        amount = 37500,
        effectiveDateOfPayment = Some(LocalDate.of(2007, 7, 14)),
        paymentType = "BAC",
        bankSortCode = 114433,
        bankAccountNumber = 1004005,
        chequeNumber = 100301,
        paymentAmount = -31250,
        chequeId = 916
      )
    )
  )

  def paymentSearchResultToVM(
    paymentSearchResultResponse: PaymentSearchResultResponse,
    taxReference: Long,
    pageUrl: Int => String,
    requestedPage: Option[Int]
  )(implicit messages: Messages) =
    PaymentResultsViewModel.toViewModel(paymentSearchResultResponse, taxReference, pageUrl, requestedPage)

  val multiplePaymentSearchResultRows: Seq[PaymentResultsViewModelRow] = Seq(
    PaymentResultsViewModelRow(
      date = Some(LocalDate.of(2007, 7, 14)),
      description = "payments.description.EP",
      amount = 37500,
      href = Some(routes.PaymentAllocationController.onPageLoad(Some(1))),
      isLink = Some(true)
    ),
    PaymentResultsViewModelRow(
      date = Some(LocalDate.of(2007, 7, 14)),
      description = "payments.description.EP",
      amount = -37500,
      href = Some(routes.PaymentAllocationController.onPageLoad(Some(1))),
      isLink = Some(true)
    ),
    PaymentResultsViewModelRow(
      date = Some(LocalDate.of(2007, 7, 14)),
      description = "payments.description.EP",
      amount = 37500,
      href = Some(routes.PaymentAllocationController.onPageLoad(Some(1))),
      isLink = Some(true)
    ),
    PaymentResultsViewModelRow(
      date = Some(LocalDate.of(2007, 7, 14)),
      description = "payments.description.EP",
      amount = 37500,
      href = Some(routes.PaymentAllocationController.onPageLoad(Some(1))),
      isLink = Some(true)
    ),
    PaymentResultsViewModelRow(
      date = Some(LocalDate.of(2007, 7, 14)),
      description = "payments.description.EP",
      amount = 37500,
      href = Some(routes.PaymentAllocationController.onPageLoad(Some(1))),
      isLink = Some(true)
    ),
    PaymentResultsViewModelRow(
      date = Some(LocalDate.of(2007, 7, 14)),
      description = "payments.description.EP",
      amount = 37500,
      href = Some(routes.PaymentAllocationController.onPageLoad(Some(1))),
      isLink = Some(true)
    ),
    PaymentResultsViewModelRow(
      date = Some(LocalDate.of(2007, 7, 14)),
      description = "payments.description.EP",
      amount = 37500,
      href = Some(routes.PaymentAllocationController.onPageLoad(Some(1))),
      isLink = Some(true)
    ),
    PaymentResultsViewModelRow(
      date = Some(LocalDate.of(2007, 7, 14)),
      description = "payments.description.EP",
      amount = 37500,
      href = Some(routes.PaymentAllocationController.onPageLoad(Some(1))),
      isLink = Some(true)
    ),
    PaymentResultsViewModelRow(
      date = Some(LocalDate.of(2007, 7, 14)),
      description = "payments.description.EP",
      amount = 37500,
      href = Some(routes.PaymentAllocationController.onPageLoad(Some(1))),
      isLink = Some(true)
    ),
    PaymentResultsViewModelRow(
      date = Some(LocalDate.of(2007, 7, 14)),
      description = "payments.description.EP",
      amount = 37500,
      href = Some(routes.PaymentAllocationController.onPageLoad(Some(1))),
      isLink = Some(true)
    ),
    PaymentResultsViewModelRow(
      date = Some(LocalDate.of(2007, 7, 14)),
      description = "payments.description.EP",
      amount = 37500,
      href = Some(routes.PaymentAllocationController.onPageLoad(Some(1))),
      isLink = Some(true)
    ),
    PaymentResultsViewModelRow(
      date = Some(LocalDate.of(2007, 7, 14)),
      description = "payments.description.EP",
      amount = 37500,
      href = Some(routes.PaymentAllocationController.onPageLoad(Some(1))),
      isLink = Some(true)
    ),
    PaymentResultsViewModelRow(
      date = Some(LocalDate.of(2007, 7, 14)),
      description = "payments.description.EP",
      amount = 37500,
      href = Some(routes.PaymentAllocationController.onPageLoad(Some(1))),
      isLink = Some(true)
    ),
    PaymentResultsViewModelRow(
      date = Some(LocalDate.of(2007, 7, 14)),
      description = "payments.description.EP",
      amount = 37500,
      href = Some(routes.PaymentAllocationController.onPageLoad(Some(1))),
      isLink = Some(true)
    ),
    PaymentResultsViewModelRow(
      date = Some(LocalDate.of(2007, 7, 14)),
      description = "payments.description.EP",
      amount = 37500,
      href = Some(routes.PaymentAllocationController.onPageLoad(Some(1))),
      isLink = Some(true)
    )
  )
}
