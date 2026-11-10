package viewmodels.payments


import controllers.payments.routes
import helpers.payments.PaymentsOnAccountResponseHelper
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec
import play.api.i18n.Messages
import play.api.test.Helpers.stubMessages
import utils.PaymentsDescriptionHelper
import viewmodels.payments.{PaymentsOnAccountViewModel, PaymentsOnAccountRow}
import views.ViewUtils.{formatCurrency, formatDate}

import java.time.LocalDate

class PaymentsOnAccountViewModelSpec extends AnyWordSpec with Matchers with PaymentsOnAccountResponseHelper {
  implicit val messages: Messages = stubMessages()

  "PaymentsOnAccount.toViewModel" should {

    "check Payments On Account from the response, based on a list with multiple items" in {
      val viewModel = PaymentsOnAccountViewModel.toViewModel(
        paymentsOnAccountMultipleList,
        Some(0),
        pageUrl = pageNumber => routes.PaymentsOnAccountController.onPageLoad(Some(0)).url
      )

      viewModel.paymentsOnAccountRows mustBe Seq(
        PaymentsOnAccountRow(
          paymentDate = paymentsOnAccountMultipleList.paymentsOnAccountRows(0).effectiveDateOfPayment,
          paymentAllocationDate = paymentsOnAccountMultipleList.paymentsOnAccountRows(0).apEndDate,
          description = PaymentsDescriptionHelper.getPaymentsDescription(paymentsOnAccountMultipleList.paymentsOnAccountRows(0).paymentType),
          amount =  paymentsOnAccountMultipleList.paymentsOnAccountRows(0).postingAmount
        ),
        PaymentsOnAccountRow(
          paymentDate = paymentsOnAccountMultipleList.paymentsOnAccountRows(1).effectiveDateOfPayment,
          paymentAllocationDate = paymentsOnAccountMultipleList.paymentsOnAccountRows(1).apEndDate,
          description = PaymentsDescriptionHelper.getPaymentsDescription(paymentsOnAccountMultipleList.paymentsOnAccountRows(1).paymentType),
          amount =  paymentsOnAccountMultipleList.paymentsOnAccountRows(1).postingAmount
        ),
        PaymentsOnAccountRow(
          paymentDate = paymentsOnAccountMultipleList.paymentsOnAccountRows(2).effectiveDateOfPayment,
          paymentAllocationDate = paymentsOnAccountMultipleList.paymentsOnAccountRows(2).apEndDate,
          description = PaymentsDescriptionHelper.getPaymentsDescription(paymentsOnAccountMultipleList.paymentsOnAccountRows(2).paymentType),
          amount =  paymentsOnAccountMultipleList.paymentsOnAccountRows(2).postingAmount
        ),
        PaymentsOnAccountRow(
          paymentDate = paymentsOnAccountMultipleList.paymentsOnAccountRows(3).effectiveDateOfPayment,
          paymentAllocationDate = paymentsOnAccountMultipleList.paymentsOnAccountRows(3).apEndDate,
          description = PaymentsDescriptionHelper.getPaymentsDescription(paymentsOnAccountMultipleList.paymentsOnAccountRows(3).paymentType),
          amount =  paymentsOnAccountMultipleList.paymentsOnAccountRows(3).postingAmount
        ),
        PaymentsOnAccountRow(
          paymentDate = paymentsOnAccountMultipleList.paymentsOnAccountRows(4).effectiveDateOfPayment,
          paymentAllocationDate = paymentsOnAccountMultipleList.paymentsOnAccountRows(4).apEndDate,
          description = PaymentsDescriptionHelper.getPaymentsDescription(paymentsOnAccountMultipleList.paymentsOnAccountRows(4).paymentType),
          amount =  paymentsOnAccountMultipleList.paymentsOnAccountRows(4).postingAmount
        ),
        PaymentsOnAccountRow(
          paymentDate = paymentsOnAccountMultipleList.paymentsOnAccountRows(5).effectiveDateOfPayment,
          paymentAllocationDate = paymentsOnAccountMultipleList.paymentsOnAccountRows(5).apEndDate,
          description = PaymentsDescriptionHelper.getPaymentsDescription(paymentsOnAccountMultipleList.paymentsOnAccountRows(5).paymentType),
          amount =  paymentsOnAccountMultipleList.paymentsOnAccountRows(5).postingAmount
        )
      )
    }
  }
}
