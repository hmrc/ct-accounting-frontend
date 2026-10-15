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

import helpers.gpa.GroupPaymentHelper
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec
import play.api.i18n.Messages
import play.api.test.Helpers.stubMessages
import utils.PaginationHelper
import viewmodels.Paginated

import java.time.LocalDate

class GroupPaymentArrangementPageViewModelSpec extends AnyWordSpec with Matchers with GroupPaymentHelper {

  "GroupPaymentArrangementPageViewModel.toViewModel" should {
    implicit val messages: Messages = stubMessages()

    val pageUrl: Int => String = page => s"/group-payment-arrangement?page=$page"

    "create a page viewModel whose summaryList matches the GroupPaymentArrangementViewModel" in {
      val result =
        GroupPaymentArrangementPageViewModel.toPageViewModel(viewModelResponseRfrReallocation, Some(1), pageUrl)

      result.summaryList mustEqual viewModelResponseRfrReallocation.summary
    }

    "create a page view model with paginated records built from the view model records and total" in {
      val result =
        GroupPaymentArrangementPageViewModel.toPageViewModel(viewModelResponseRfrReallocation, Some(1), pageUrl)

      result.paginatedRecords mustEqual
        Paginated.alreadyPaginated(
          viewModelResponseRfrReallocation.records,
          Some(1),
          viewModelResponseRfrReallocation.totalNumberOfRecords
        )
    }

    "create a page view model with pagination built from the paginated records and page url" in {
      val result =
        GroupPaymentArrangementPageViewModel.toPageViewModel(viewModelResponseRfrReallocation, Some(1), pageUrl)

      result.pagination mustEqual PaginationHelper.toPagination(result.paginatedRecords, pageUrl)
    }

    "create a page view model using the requested page when a page is provided" in {
      val result =
        GroupPaymentArrangementPageViewModel.toPageViewModel(viewModelResponseRfrReallocation, Some(2), pageUrl)

      result.paginatedRecords mustEqual
        Paginated.alreadyPaginated(
          viewModelResponseRfrReallocation.records,
          Some(2),
          viewModelResponseRfrReallocation.totalNumberOfRecords
        )
    }

    "create a page view model using the default page when no page is requested" in {
      val result = GroupPaymentArrangementPageViewModel.toPageViewModel(viewModelResponseRfrReallocation, None, pageUrl)

      result.paginatedRecords mustEqual
        Paginated.alreadyPaginated(
          viewModelResponseRfrReallocation.records,
          None,
          viewModelResponseRfrReallocation.totalNumberOfRecords
        )

      result.summaryList mustEqual viewModelResponseRfrReallocation.summary
    }

    "create a page view model with no records when the view model has none" in {
      val emptyViewModel = viewModelResponseRfrReallocation.copy(records = List.empty, totalNumberOfRecords = 0)

      val result = GroupPaymentArrangementPageViewModel.toPageViewModel(emptyViewModel, None, pageUrl)

      result.paginatedRecords mustEqual Paginated.alreadyPaginated(List.empty, None, 0)
      result.pagination mustEqual PaginationHelper.toPagination(result.paginatedRecords, pageUrl)
    }
  }

}
