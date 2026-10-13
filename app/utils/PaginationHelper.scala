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

package utils

import play.api.i18n.Messages
import uk.gov.hmrc.govukfrontend.views.Aliases.{Pagination, PaginationItem, PaginationLink}
import viewmodels.Paginated

object PaginationHelper {

  def toPagination(paginated: Paginated[_], pageUrl: Int => String)(implicit messages: Messages): Option[Pagination] =
    if (!paginated.needsPagination) None
    else Some(
      Pagination(
        items = Some(pageItems(paginated, pageUrl)),
        previous = Option.when(paginated.page > 1)(PaginationLink(href = pageUrl(paginated.page - 1))),
        next = Option.when(paginated.page < paginated.totalPages)(PaginationLink(href = pageUrl(paginated.page + 1))),
        landmarkLabel = Some(messages("pagination.landmarkLabel"))
      )
    )

  private def pageItems(paginated: Paginated[_], pageUrl: Int => String): Seq[PaginationItem] = {
    val pages = (Set(1, paginated.totalPages) ++ (paginated.page - 1 to paginated.page + 1))
      .filter(pageNumber => pageNumber > 0 && pageNumber <= paginated.totalPages)
      .toList
      .sorted

    pages.zip(pages.head +: pages).flatMap { case (page, previous) => 
      val ellipsis = Option.when(page - previous > 1)(PaginationItem(ellipsis = Some(true)))
      ellipsis.toSeq :+ PaginationItem(
        href = pageUrl(page),
        number = Some(page.toString),
        current = Some(page == paginated.page)
      )
    }
  }
}
