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

import helpers.gpa.GpaDetailsGroupPaymentHelper
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec
import play.api.i18n.Messages
import play.api.test.Helpers.stubMessages
import uk.gov.hmrc.govukfrontend.views.viewmodels.content.{HtmlContent, Text}
import views.ViewUtils.{formatCurrency, formatDate}

class GpaDetailsGroupPaymentArrangementViewModelSpec
    extends AnyWordSpec
    with Matchers
    with GpaDetailsGroupPaymentHelper {

  "GpaDetailsGroupPaymentArrangementViewModel.summary" should {
    implicit val messages: Messages      = stubMessages()
    val groupSummaryList                 = viewModelResponseRfrReallocation.summary
    val groupSummaryListWithTaxTotalZero = viewModelResponseRfrReallocationWithTaxTotalZero.summary

    "create GroupSummaryList containing 5 rows with relevant row name " in {
      groupSummaryList.rows.size mustEqual 5
    }

    "create GroupSummaryList containing first row with arrangementReference key and relevant value" in {
      groupSummaryList.rows.head.key.content mustEqual Text(messages("groupPaymentsArrangements.arrangementReference"))
      groupSummaryList.rows.head.value.content mustEqual Text(viewModelResponseRfrReallocation.arrangementReference)

    }
    "create GroupSummaryList containing second row with periodAccountEnding value is defined key and value" in {
      val expectedDate =
        viewModelResponseRfrReallocation.accountEnding.map(d => formatDate(d, messages.lang)).getOrElse("")

      groupSummaryList.rows(1).key.content mustEqual Text(messages("groupPaymentsArrangements.periodAccountEnding"))
      groupSummaryList.rows(1).value.content mustEqual Text(expectedDate)

    }
    "create GroupSummaryList containing third row with periodAccountStatus key and value " in {
      groupSummaryList.rows(2).key.content mustEqual Text(messages("groupPaymentsArrangements.periodAccountStatus"))
      groupSummaryList.rows(2).value.content mustEqual Text(viewModelResponseRfrReallocation.accountStatus)

    }
    "create GroupSummaryList containing fourth row with paymentTotal key and value" in {
      groupSummaryList.rows(3).key.content mustEqual Text(messages("groupPaymentsArrangements.paymentTotal"))
      groupSummaryList.rows(3).value.content mustEqual Text(
        formatCurrency(viewModelResponseRfrReallocation.paymentTotal)
      )

    }
    "create GroupSummaryList containing fifth row with taxTotal key and value with hyperlink if taxTotal > 0" in {
      groupSummaryList.rows(4).key.content mustEqual Text(messages("groupPaymentsArrangements.taxTotal"))

      val value = groupSummaryList.rows(4).value.content
      value mustBe a[HtmlContent]

      val html = value.asHtml.toString
      html must include(formatCurrency(viewModelResponseRfrReallocation.taxTotal))
      html must include("href=\"#\"")

    }
    "create GroupSummaryList containing fifth row with taxTotal key and value without hyperlink if taxTotal = 0" in {
      groupSummaryListWithTaxTotalZero.rows(4).key.content mustEqual Text(
        messages("groupPaymentsArrangements.taxTotal")
      )

      val value = groupSummaryListWithTaxTotalZero.rows(4).value.content
      value mustBe a[HtmlContent]

      val html = value.asHtml.toString
      html mustNot include("href=\"#\"")

    }
  }

}
