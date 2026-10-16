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

import play.api.i18n.Messages
import uk.gov.hmrc.govukfrontend.views.Aliases.Pagination
import uk.gov.hmrc.govukfrontend.views.viewmodels.summarylist.SummaryList
import utils.PaginationHelper
import viewmodels.Paginated

case class GpaDetailsGroupPaymentArrangementPageViewModel(
  summaryList: SummaryList,
  paginatedRecords: Paginated[GroupPaymentRecord],
  pagination: Option[Pagination],
  paginationInfo: Option[String]
)

object GpaDetailsGroupPaymentArrangementPageViewModel {

  def toPageViewModel(
    viewModel: GpaDetailsGroupPaymentArrangementViewModel,
    requestedPage: Option[Int],
    pageUrl: Int => String
  )(implicit messages: Messages): GpaDetailsGroupPaymentArrangementPageViewModel = {

    val paginated = getPaginationRecord(viewModel, requestedPage)
    GpaDetailsGroupPaymentArrangementPageViewModel(
      summaryList = viewModel.summary,
      paginatedRecords = paginated,
      pagination = PaginationHelper.toPagination(paginated, pageUrl),
      paginationInfo = PaginationHelper.paginationText(paginated)
    )
  }

  private def getPaginationRecord(
    gpaViewModel: GpaDetailsGroupPaymentArrangementViewModel,
    requestedPage: Option[Int]
  ): Paginated[GroupPaymentRecord] =
    Paginated.alreadyPaginated(gpaViewModel.records, requestedPage, gpaViewModel.totalNumberOfRecords)
}
