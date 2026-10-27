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
import utils.StatusHelper.getStatus

class StatusHelperSpec extends SpecBase {

  val application: Application = applicationBuilder().build()

  implicit val messagesApi: MessagesApi = application.injector.instanceOf[MessagesApi]
  implicit val messages: Messages       = MessagesImpl(Lang.defaultLang, messagesApi)

  "return Open when the status is O" in {
    val result: String = messages(getStatus("O"))
    result mustBe "Open"
  }

  "return Open when the status is S" in {
    val result: String = messages(getStatus("O"))
    result mustBe "Open"
  }

  "return empty string when status is L" in {
    val result: String = messages(getStatus("C"))
    result mustBe ""
  }

  "return Cleared when the status is E" in {
    val result: String = messages(getStatus("L"))
    result mustBe "Cleared"

  }
  "return Earlier Exempt when the status is Z" in {
    val result: String = messages(getStatus("E"))
    result mustBe ""

  }
  "return emptyString for all other status" in {
    val result: String = messages(getStatus("Z"))
    result mustBe ""
  }
}
