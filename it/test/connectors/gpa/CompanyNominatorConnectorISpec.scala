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
import helpers.gpa.CompanyNominatorHelper
import com.github.tomakehurst.wiremock.client.WireMock.*
import itutils.ApplicationWithWiremock
import org.scalatest.BeforeAndAfterEach
import org.scalatest.concurrent.{IntegrationPatience, ScalaFutures}
import org.scalatest.matchers.must.Matchers
import org.scalatest.matchers.should.Matchers.should
import org.scalatest.wordspec.AnyWordSpec
import play.api.http.Status.{INTERNAL_SERVER_ERROR, OK}
import uk.gov.hmrc.http.HeaderCarrier

class CompanyNominatorConnectorISpec
    extends AnyWordSpec
    with Matchers
    with ScalaFutures
    with IntegrationPatience
    with ApplicationWithWiremock
    with BeforeAndAfterEach
    with CompanyNominatorHelper {

  implicit val hc: HeaderCarrier = HeaderCarrier()

  private val connector: CompanyNominatorConnector = app.injector.instanceOf[CompanyNominatorConnector]

  "getCompanyNominator" should {

    def url(gpaUtr: Long, nominatedCompanyUtr: Long) =
      s"/corporation-tax/is-company-nominator/$gpaUtr/$nominatedCompanyUtr"

    "return a CompanyNominator from BE with isParticipator = false" in {
      stubFor(
        get(urlPathEqualTo(url(1L, 5L)))
          .willReturn(
            aResponse()
              .withStatus(OK)
              .withBody(
                s"""{
                   | "isParticipator": false
                   |}""".stripMargin
              )
          )
      )

      val result = connector.getCompanyNominator(1L, 5L).futureValue
      result should be(companyNominatorWithFalseValue)
    }
    "return a CompanyNominator from BE with isParticipator = true" in {
      stubFor(
        get(urlPathEqualTo(url(1L, 5L)))
          .willReturn(
            aResponse()
              .withStatus(OK)
              .withBody(
                s"""{
                   | "isParticipator": true
                   |}""".stripMargin
              )
          )
      )

      val result = connector.getCompanyNominator(1L, 5L).futureValue
      result should be(companyNominatorWithTrueValue)
    }

    "return INTERNAL_ERROR when service failed" in {
      stubFor(
        get(urlPathEqualTo(url(1L, 2L)))
          .willReturn(
            aResponse()
              .withStatus(INTERNAL_SERVER_ERROR)
              .withBody(
                s"""{
                   |error" : "Failed to retrieve CompanyNominator from BE"
                   |}""".stripMargin
              )
          )
      )

      val ex = intercept[Exception] {
        connector.getCompanyNominator(1L, 2L).futureValue
      }
      ex.getMessage.toLowerCase must include("error")
    }
  }

}
