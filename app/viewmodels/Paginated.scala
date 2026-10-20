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

package viewmodels

case class Paginated[A](
  items: Seq[A],
  page: Int,
  pageSize: Int,
  totalItems: Int
) {
  val totalPages: Int          = math.max(1, (totalItems + pageSize - 1) / pageSize)
  val needsPagination: Boolean = totalItems > pageSize
  val firstItemNumber: Int     = (page - 1) * pageSize + 1
  val lastItemNumber: Int      = math.min(page * pageSize, totalItems)

}

object Paginated {
  val defaultPageSize = 2 // TODO get from config

  def alreadyPaginated[A](
    items: Seq[A],
    requestedPage: Option[Int],
    totalNumberRecords: Int,
    pageSize: Int = defaultPageSize
  ): Paginated[A] = {
    val totalPages = math.max(1, (totalNumberRecords + pageSize - 1) / pageSize)
    val page       = requestedPage.getOrElse(1).max(1).min(totalPages)
    Paginated(items, page, pageSize, totalNumberRecords)
  }
}
