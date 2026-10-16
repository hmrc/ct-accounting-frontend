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

object SlicedRowPerPageHelper {

  def returnSlicedRowsPerPage[A](
    list: List[A],
    startIndex: Int,
    count: Int
  ): List[A] = {
    val remainingList = list.slice(startIndex, list.length)
    if (remainingList.length > count) {
      val paginationIndex = (startIndex / count) + 1
      val endIndex        = paginationIndex * count
      list.slice(startIndex, endIndex)
    } else {
      remainingList
    }

  }

}
