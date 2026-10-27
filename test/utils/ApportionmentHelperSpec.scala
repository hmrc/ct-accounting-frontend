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

import base.SpecBase
import play.api.Application
import play.api.i18n.{Lang, Messages, MessagesApi, MessagesImpl}
import utils.ApportionmentHelper.getApportionmentMethod

class ApportionmentHelperSpec extends SpecBase {

  val application: Application = applicationBuilder().build()

  implicit val messagesApi: MessagesApi = application.injector.instanceOf[MessagesApi]
  implicit val messages: Messages       = MessagesImpl(Lang.defaultLang, messagesApi)

  "return HMRC when the apportionment is R" in {
    val result: String = messages(getApportionmentMethod("R"))
    result mustBe "HMRC"
  }

  "return Group when the status is C" in {
    val result: String = messages(getApportionmentMethod("C"))
    result mustBe "Group"
  }

  "return an empty string when anything else is given" in {
    val result: String = messages(getApportionmentMethod(""))
    result mustBe ""
  }

}
