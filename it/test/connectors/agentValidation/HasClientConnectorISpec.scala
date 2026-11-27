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

package connectors.agentValidation

import com.github.tomakehurst.wiremock.client.WireMock.*
import itutils.ApplicationWithWiremock
import org.scalatest.BeforeAndAfterEach
import org.scalatest.concurrent.{IntegrationPatience, ScalaFutures}
import org.scalatest.matchers.must.Matchers.mustEqual
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import play.api.http.Status.*
import uk.gov.hmrc.http.HeaderCarrier

class HasClientConnectorISpec
    extends AnyWordSpec
    with Matchers
    with ScalaFutures
    with IntegrationPatience
    with ApplicationWithWiremock
    with BeforeAndAfterEach {

  implicit val hc: HeaderCarrier = HeaderCarrier()

  private val connector: HasClientConnector = app.injector.instanceOf[HasClientConnector]

  "hasClient" should {

    def url(utr:Long) = s"/corporation-tax/has-client/$utr"

    "return hasClient status as true" in {
      stubFor(
        get(urlPathEqualTo(url(123456L)))
          .willReturn(
            aResponse()
              .withStatus(OK)
              .withBody(
                s"""{
                   |"status" : true
                   |}""".stripMargin
              )
          )
      )

      val response = connector.hasClient(123456L).futureValue
      response.status mustEqual true
    }
    "return hasClient status as false" in {
      stubFor(
        get(urlPathEqualTo(url(123456L)))
          .willReturn(
            aResponse()
              .withStatus(OK)
              .withBody(
                s"""{
                   |"status" : false
                   |}""".stripMargin
              )
          )
      )

      val response = connector.hasClient(123456L).futureValue
      response.status mustEqual false
    }

    "return INTERNAL_ERROR when service failed" in {
      stubFor(
        get(urlPathEqualTo(url(7363L)))
          .willReturn(
            aResponse()
              .withStatus(INTERNAL_SERVER_ERROR)
              .withBody(
                s"""{
                   |error" : "Failed to retrieve adjustment transactions from the BE"
                   |}""".stripMargin
              )
          )
      )

      val ex = intercept[Exception] {
        connector.hasClient(7363L).futureValue
      }
      ex.getMessage.toLowerCase should include("error")
    }
  }
}
