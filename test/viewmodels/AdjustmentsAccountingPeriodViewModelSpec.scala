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

import helpers.AdjustmentsDataHelper
import org.scalatest.flatspec.AnyFlatSpec

class AdjustmentsAccountingPeriodViewModelSpec extends AnyFlatSpec with AdjustmentsDataHelper {

  it should "calculate total as zero" in {
    assert(
      emptyAdjustmentsViewModel.total == BigDecimal(0)
    )
    assert(
      emptyAdjustmentsViewModel.totalAsString == "£0.00"
    )
  }

  it should "calculate total for adjustments with two items" in {
    assert(
      adjustmentsViewModelWithMultipleItems.total == BigDecimal(250.00)
    )
    assert(
      adjustmentsViewModelWithMultipleItems.totalAsString == "£250.00"
    )
  }

  it should "calculate total for adjustments with single item" in {
    assert(
      adjustmentsViewModelWithOneItem.total == BigDecimal(50.00)
    )
    assert(
      adjustmentsViewModelWithOneItem.totalAsString == "£50.00"
    )
  }
}
