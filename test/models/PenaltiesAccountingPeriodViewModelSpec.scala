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

package models

import helpers.PenaltiesDataHelper
import org.scalatest.flatspec.AnyFlatSpec
import play.api.i18n.Messages
import play.api.test.Helpers.stubMessages

class PenaltiesAccountingPeriodViewModelSpec extends AnyFlatSpec with PenaltiesDataHelper {

  private trait Fixture {
    implicit val messages: Messages = stubMessages()
  }

  it should "calculate total as Zero" in new Fixture {
    assert(
      viewModelWithNoRows.total == BigDecimal(0)
    )
    assert(
      viewModelWithNoRows.totalAsString == "£0.00"
    )
  }

  it should "calculate total for viewModel with 2 rows" in new Fixture {
    assert(
      viewModelWithTwoRows.total == BigDecimal(127.32)
    )
    assert(
      viewModelWithTwoRows.totalAsString == "£127.32"
    )
  }

  it should "calculate total for viewModel with single row" in new Fixture {
    assert(
      viewModelWithSingleRow.total == BigDecimal(100.13)
    )
    assert(
      viewModelWithSingleRow.totalAsString == "£100.13"
    )
  }
}
