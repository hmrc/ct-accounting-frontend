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

package connectors.gpa

import com.github.tomakehurst.wiremock.client.WireMock.{stubFor, urlPathEqualTo}
import itutils.ApplicationWithWiremock
import com.github.tomakehurst.wiremock.client.WireMock.*
import helpers.gpa.PeriodWithinRangeHelper
import org.scalatest.BeforeAndAfterEach
import org.scalatest.concurrent.{IntegrationPatience, ScalaFutures}
import org.scalatest.matchers.must.Matchers
import org.scalatest.matchers.should.Matchers.should
import org.scalatest.wordspec.AnyWordSpec
import play.api.http.Status.{INTERNAL_SERVER_ERROR, OK}
import uk.gov.hmrc.http.HeaderCarrier

class GroupPaymentPeriodInRangeConnectorISpec
    extends AnyWordSpec
    with Matchers
    with ScalaFutures
    with IntegrationPatience
    with ApplicationWithWiremock
    with BeforeAndAfterEach
    with PeriodWithinRangeHelper {

  implicit val hc: HeaderCarrier = HeaderCarrier()

  private val connector: GroupPaymentPeriodInRangeConnector =
    app.injector.instanceOf[GroupPaymentPeriodInRangeConnector]

  "getGroupPaymentPeriodInRange" should {

    def url(gpaUTR: Long, nominatedCompanyUTR: Long, pPeriod: Int, pMonthRestriction: Int) =
      s"/corporation-tax/group-payment-periods-in-range/$gpaUTR/$nominatedCompanyUTR/$pPeriod/$pMonthRestriction"

    "return a PeriodWithinRange with true value " in {
      stubFor(
        get(urlPathEqualTo(url(1L, 5L, 12, 13)))
          .willReturn(
            aResponse()
              .withStatus(OK)
              .withBody(
                s"""{
                   |"isPeriodWithinRange" : true
                   |}""".stripMargin
              )
          )
      )

      val result = connector.getGroupPaymentPeriodInRange(1L, 5L, 12, 13).futureValue
      result should be(periodWithinRangeTrueValue)
    }
    "return a PeriodWithinRange with false value " in {
      stubFor(
        get(urlPathEqualTo(url(1L, 5L, 12, 13)))
          .willReturn(
            aResponse()
              .withStatus(OK)
              .withBody(
                s"""{
                   |"isPeriodWithinRange" : false
                   |}""".stripMargin
              )
          )
      )

      val result = connector.getGroupPaymentPeriodInRange(1L, 5L, 12, 13).futureValue
      result should be(periodWithinRangeFalseValue)
    }

    "return INTERNAL_ERROR when service failed" in {
      stubFor(
        get(urlPathEqualTo(url(1L, 2L, 16, 19)))
          .willReturn(
            aResponse()
              .withStatus(INTERNAL_SERVER_ERROR)
              .withBody(
                s"""{
                   |error" : "Failed to retrieve GroupPaymentPeriodInRange from the BE"
                   |}""".stripMargin
              )
          )
      )

      val ex = intercept[Exception] {
        connector.getGroupPaymentPeriodInRange(1L, 2L, 16, 19).futureValue
      }
      ex.getMessage.toLowerCase must include("error")
    }
  }

}
