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

package utils.gpa

import base.SpecBase
import play.api.Application
import play.api.i18n.{Lang, Messages, MessagesApi, MessagesImpl}

class StatusHelperSpec extends SpecBase {

  val application: Application = applicationBuilder().build()

  implicit val messagesApi: MessagesApi = application.injector.instanceOf[MessagesApi]
  implicit val messages: Messages       = MessagesImpl(Lang.defaultLang, messagesApi)

  "return Open when the status is O" in {
    val status         = "Open"
    val result: String = messages(StatusHelper.getStatus("O"))
    result mustBe status
  }

  "return Open when the status is S" in {
    val status         = "Open"
    val result: String = messages(StatusHelper.getStatus("O"))
    result mustBe status
  }

  "return Earlier Cleared when status is L" in {
    val status         = "Earlier Cleared"
    val result: String = messages(StatusHelper.getStatus("C"))
    result mustBe status
  }

  "return Cleared when the status is E" in {
    val status         = "Cleared"
    val result: String = messages(StatusHelper.getStatus("L"))
    result mustBe status

  }
  "return Earlier Exempt when the status is Z" in {
    val status         = "Earlier Exempt"
    val result: String = messages(StatusHelper.getStatus("E"))
    result mustBe status

  }
  "return emptyString for all other status" in {
    val status         = ""
    val result: String = messages(StatusHelper.getStatus("Z"))
    result mustBe status
  }
}
