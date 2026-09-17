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

package views
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.must.Matchers
import views.ViewUtils.*

class ViewUtilsSpec extends AnyFreeSpec with Matchers {

  ".formatPercentage" - {

    "must keep both decimal places unchanged when the rate already has two" in {
      val rate   = BigDecimal(2.36)
      val result = formatPercentage(rate)
      result mustBe "2.36%"
    }
    "must keep a zero in the first decimal place" in {
      val rate   = BigDecimal(2.06)
      val result = formatPercentage(rate)
      result mustBe "2.06%"
    }
    "must pad a rate with one decimal place with a trailing zero" in {
      val rate   = BigDecimal(6.9)
      val result = formatPercentage(rate)
      result mustBe "6.90%"
    }
    "must pad a whole-number rate to two decimal places" in {
      val rate   = BigDecimal(2)
      val result = formatPercentage(rate)
      result mustBe "2.00%"
    }
    "must keep the leading zero for rates below 1" in {
      val rate   = BigDecimal(0.25)
      val result = formatPercentage(rate)
      result mustBe "0.25%"
    }
  }

}
