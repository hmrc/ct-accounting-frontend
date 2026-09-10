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

package connectors

import com.github.tomakehurst.wiremock.client.WireMock.*
import helpers.DebitInterestHelper
import itutils.ApplicationWithWiremock
import models.{AccountingPeriodDetails, AccountingPeriodDetailsResponse}
import org.scalatest.BeforeAndAfterEach
import org.scalatest.concurrent.{IntegrationPatience, ScalaFutures}
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec
import play.api.http.Status.{BAD_REQUEST, INTERNAL_SERVER_ERROR, NOT_FOUND, OK}
import uk.gov.hmrc.http.HeaderCarrier

class DebitInterestConnectorISpec
  extends AnyWordSpec
    with Matchers
    with ScalaFutures
    with IntegrationPatience
    with ApplicationWithWiremock
    with BeforeAndAfterEach
    with DebitInterestHelper {

  implicit val hc: HeaderCarrier = HeaderCarrier()

  private val interestType: String = "DBI"
  private val taxRef: Long = 1L
  private val accPeriod: Long = 1L

  private val connector: DebitInterestConnector = app.injector.instanceOf[DebitInterestConnector]

  "getDebitInterest" should {

    def url(taxRef: Long, accPeriod: Long, interestType: String) =
      s"/corporation-tax/interest-accrual-list/$taxRef/$accPeriod/$interestType"

    "return DebitInterest with status code OK" in {

      stubFor(
        get(urlPathEqualTo(url(taxRef, accPeriod, interestType)))
          .willReturn(
            aResponse()
              .withStatus(OK)
              .withBody(
                s"""{"interestAccruals":[
                   |  {
                   |  "computationAmount":17.01,
                   |  "interestAccrualFromDate": "2026-01-01",
                   |  "interestAccrualToDate": "2026-12-12",
                   |  "interestRate": 0.75,
                   |  "interestAmount":99.11,
                   |  "apEndDate": "2026-12-31"
                   |  }
                   |  ]
                   | }""".stripMargin
              )
          )
      )

      val result = connector.getDebitInterest(taxRef, accPeriod, interestType).futureValue
      result mustEqual defaultRecord
    }

    /*
    "return INTERNAL_ERROR when BE failed" in {
      stubFor(
        get(urlPathEqualTo(url(1L, 5L)))
          .willReturn(
            aResponse()
              .withStatus(INTERNAL_SERVER_ERROR)
              .withBody("boom")
          )
      )

      val ex = intercept[Exception] {
        connector.getAccountingPeriodResponse(1L, 5L).futureValue
      }
      ex.getMessage.toLowerCase must include("boom")
    }

    "return 400 when BE returns BAD_REQUEST " in {
      stubFor(
        get(urlPathEqualTo(url(1L, 5L)))
          .willReturn(
            aResponse()
              .withStatus(BAD_REQUEST)
              .withBody("Invalid Request")
          )
      )

      val ex = intercept[Exception] {
        connector.getAccountingPeriodResponse(1L, 5L).futureValue
      }
      ex.getMessage must include("Invalid Request")
    }

    "return 404 when BE returns NOT_FOUND " in {
      stubFor(
        get(urlPathEqualTo(url(1L, 5L)))
          .willReturn(
            aResponse()
              .withStatus(NOT_FOUND)
              .withBody("Not found")
          )
      )

      val ex = intercept[Exception] {
        connector.getAccountingPeriodResponse(1L, 5L).futureValue
      }
      ex.getMessage must include("Not found")
    }
     */
  }

}
