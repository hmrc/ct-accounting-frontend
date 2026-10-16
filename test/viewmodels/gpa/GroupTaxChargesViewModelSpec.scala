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

import controllers.gpa.routes
import helpers.gpa.GroupTaxChargesHelper
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec
import play.api.i18n.Messages
import play.api.test.Helpers.stubMessages
import uk.gov.hmrc.govukfrontend.views.Aliases.Text
import uk.gov.hmrc.govukfrontend.views.viewmodels.summarylist.Key
import utils.gpa.StatusHelper
import viewmodels.govuk.summarylist.*
import views.ViewUtils.{formatCurrency, formatDate}

import java.time.LocalDate

class GroupTaxChargesViewModelSpec extends AnyWordSpec with Matchers with GroupTaxChargesHelper {
  implicit val messages: Messages = stubMessages()

  "GroupTaxChargesViewModel.toViewModel" should {

    "check summary list rows from the response" in {
      val viewModel = GroupTaxChargesViewModel.toViewModel(
        gpaGroupTaxChargesWithTwoParticipators,
        Some(0),
        pageUrl = pageNumber => routes.GroupTaxChargesController.onPageLoad(Some(0)).url
      )

      viewModel.summaryList.rows mustBe Seq(
        SummaryListRowViewModel(
          key = Key(content = Text(messages("groupTaxCharges.summaryHeader.groupArrangementReference"))),
          value = ValueViewModel(Text(gpaGroupTaxChargesWithTwoParticipators.pGpaUtr2.toString))
        ),
        SummaryListRowViewModel(
          key = Key(content = Text(messages("groupTaxCharges.summaryHeader.periodOfAccountEnding"))),
          value = ValueViewModel(
            Text(
              formatDate(
                gpaGroupTaxChargesWithTwoParticipators.pGppEndDate.getOrElse(LocalDate.of(2026, 1, 1)),
                messages.lang
              )
            )
          )
        ),
        SummaryListRowViewModel(
          key = Key(content = Text(messages("groupTaxCharges.summaryHeader.groupPeriodOfAccountStatus"))),
          value = ValueViewModel(
            Text(messages(StatusHelper.getStatus(gpaGroupTaxChargesWithTwoParticipators.pGppStatus.getOrElse(""))))
          )
        ),
        SummaryListRowViewModel(
          key = Key(content = Text(messages("groupTaxCharges.summaryHeader.groupPaymentTotal"))),
          value = ValueViewModel(
            Text(formatCurrency(gpaGroupTaxChargesWithTwoParticipators.pGppTotalGroupPayment.getOrElse(0.00)))
          )
        ),
        SummaryListRowViewModel(
          key = Key(content = Text(messages("groupTaxCharges.summaryHeader.groupTaxTotal"))),
          value = ValueViewModel(
            Text(formatCurrency(gpaGroupTaxChargesWithTwoParticipators.pGppTotalGroupTax.getOrElse(0.00)))
          )
        )
      )
    }

    "check group tax charges from the response" in {
      val viewModel = GroupTaxChargesViewModel.toViewModel(
        gpaGroupTaxChargesWithTwoParticipators,
        Some(0),
        pageUrl = pageNumber => routes.GroupTaxChargesController.onPageLoad(Some(0)).url
      )

      viewModel.groupTaxRecordsRows mustBe Seq(
        GroupTaxRecordRow(
          participatingCompany = gpaGroupTaxChargesWithTwoParticipators.pCurGroupTaxCharges(0).participatorName,
          uniqueTaxpayerReference = gpaGroupTaxChargesWithTwoParticipators.pCurGroupTaxCharges(0).participatorReference,
          accountingPeriodEnding = gpaGroupTaxChargesWithTwoParticipators.pCurGroupTaxCharges(0).participatorApEndDate,
          taxLiability = AmountRow(
            description = formatCurrency(
              gpaGroupTaxChargesWithTwoParticipators.pCurGroupTaxCharges(0).participatorTaxCharge.getOrElse(0.00)
            ),
            amount =
              gpaGroupTaxChargesWithTwoParticipators.pCurGroupTaxCharges(0).participatorTaxCharge.getOrElse(0.00),
            isLink = false,
            href = routes.GroupTaxChargesController.onPageLoad(None)
          ),
          paymentAllocated = AmountRow(
            description = formatCurrency(
              gpaGroupTaxChargesWithTwoParticipators.pCurGroupTaxCharges(0).allocatedPayment.getOrElse(0.00)
            ),
            amount = gpaGroupTaxChargesWithTwoParticipators.pCurGroupTaxCharges(0).allocatedPayment.getOrElse(0.00),
            isLink = true,
            href = routes.GroupTaxChargesController.onPageLoad(None)
          )
        ),
        GroupTaxRecordRow(
          participatingCompany = gpaGroupTaxChargesWithTwoParticipators.pCurGroupTaxCharges(1).participatorName,
          uniqueTaxpayerReference = gpaGroupTaxChargesWithTwoParticipators.pCurGroupTaxCharges(1).participatorReference,
          accountingPeriodEnding = gpaGroupTaxChargesWithTwoParticipators.pCurGroupTaxCharges(1).participatorApEndDate,
          taxLiability = AmountRow(
            description = formatCurrency(
              gpaGroupTaxChargesWithTwoParticipators.pCurGroupTaxCharges(1).participatorTaxCharge.getOrElse(0.00)
            ),
            amount =
              gpaGroupTaxChargesWithTwoParticipators.pCurGroupTaxCharges(1).participatorTaxCharge.getOrElse(0.00),
            isLink = false,
            href = routes.GroupTaxChargesController.onPageLoad(None)
          ),
          paymentAllocated = AmountRow(
            description = formatCurrency(
              gpaGroupTaxChargesWithTwoParticipators.pCurGroupTaxCharges(1).allocatedPayment.getOrElse(0.00)
            ),
            amount = gpaGroupTaxChargesWithTwoParticipators.pCurGroupTaxCharges(1).allocatedPayment.getOrElse(0.00),
            isLink = true,
            href = routes.GroupTaxChargesController.onPageLoad(None)
          )
        )
      )
    }
  }
}
