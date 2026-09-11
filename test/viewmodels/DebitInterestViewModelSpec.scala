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

import helpers.DebitInterestHelper
import org.scalatest.flatspec.AnyFlatSpec

class DebitInterestViewModelSpec extends AnyFlatSpec with DebitInterestHelper {

  it should "calculate total as £99.11" in {
    assert(
      defaultViewModel.total == BigDecimal(99.11)
    )
    assert(
      defaultViewModel.totalAsString == "£99.11"
    )
  }


  it should "calculate total as zero" in {
    assert(
      viewModelWithZeroTotal.total == BigDecimal(0)
    )
    assert(
      viewModelWithZeroTotal.totalAsString == "£0.00"
    )
  }

}
