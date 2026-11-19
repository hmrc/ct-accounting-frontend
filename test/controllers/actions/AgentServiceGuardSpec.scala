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

package controllers.actions

import base.SpecBase
import controllers.routes
import models.requests.IdentifierRequest
import play.api.mvc.{AnyContent, Result, Results}
import play.api.test.FakeRequest
import play.api.test.Helpers.*

import java.util.concurrent.atomic.AtomicBoolean
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class AgentServiceGuardSpec extends SpecBase {

  private val guard = new AgentServiceGuard()

  private def request(utr: Option[String], agentRef: Option[String]): IdentifierRequest[AnyContent] =
    IdentifierRequest(FakeRequest(), "internal-id", utr, agentRef)

  private def run(req: IdentifierRequest[AnyContent]): (Future[Result], AtomicBoolean) = {
    val blockCalled = new AtomicBoolean(false)
    val result      = guard.invokeBlock(
      req,
      (_: IdentifierRequest[AnyContent]) => {
        blockCalled.set(true)
        Future.successful(Results.Ok)
      }
    )
    (result, blockCalled)
  }

  "AgentServiceGuard" - {

    "must let an agent through to the block" in {
      val (result, blockCalled) = run(request(utr = None, agentRef = Some("N12A36")))

      status(result) mustBe OK
      blockCalled.get() mustBe true
    }

    "must redirect an organisation and not call the block" in {
      val (result, blockCalled) = run(request(utr = Some("1234567890"), agentRef = None))

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(routes.UnauthorisedController.onPageLoad().url)
      blockCalled.get() mustBe false
    }

    "must redirect when neither utr nor agent reference is present" in {
      val (result, blockCalled) = run(request(utr = None, agentRef = None))

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(routes.UnauthorisedController.onPageLoad().url)
      blockCalled.get() mustBe false
    }
  }
}
