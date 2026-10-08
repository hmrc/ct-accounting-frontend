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
import helpers.GroupPaymentHelper
import itutils.ApplicationWithWiremock
import org.scalatest.BeforeAndAfterEach
import org.scalatest.concurrent.{IntegrationPatience, ScalaFutures}
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec
import play.api.http.Status.{BAD_REQUEST, INTERNAL_SERVER_ERROR, OK}
import uk.gov.hmrc.http.HeaderCarrier



class GroupPaymentConnectorISpec
  extends AnyWordSpec
    with Matchers
    with ScalaFutures
    with IntegrationPatience
    with ApplicationWithWiremock
    with BeforeAndAfterEach with GroupPaymentHelper {

  implicit val hc: HeaderCarrier = HeaderCarrier()

  private val connector: GroupPaymentsConnector = app.injector.instanceOf[GroupPaymentsConnector]


  "getPaymentDetails" should {

    def url(taxRef: Long, accPeriod: Long,
            startIndex: Int, count: Int) =
      s"/corporation-tax/gpa-payment-details/$taxRef?contractVersion=$accPeriod&startIndex=$startIndex&count=$count"

    "return GpaPaymentsDetailsResponse with the status code OK" in {

      stubFor(
        get(urlEqualTo(url(1L, 5L, 0, 20)))
          .willReturn(
            aResponse()
              .withStatus(OK)
              .withBody(
                s"""
                   |{
                   |   "gpaPayments":[{
                   |     "displayDate":"2025-01-01",
                   |     "total":17.01,
                   |     "tablename":"Payslip",
                   |     "paymentType":"BGP",
                   |     "targetApNo":7,
                   |     "participatorPresent":true
                   |   }],
                   |   "totalNumOfRecords":17,
                   |   "gppEndDate":"2026-01-01",
                   |   "gppTotalGroupPayment":1125017,
                   |   "gppTotalGroupTax":1125012,
                   |   "gppStatus":"C",
                   |   "gppApportionmentMethod":"METHOD"
                   |   }
                   |""".stripMargin
              )
          )
      )

      val result = connector.getPaymentDetails(1L, 5L, 0, 20).futureValue
      result mustEqual response
    }


    "return INTERNAL_ERROR when BE failed" in {
      stubFor(
        get(urlEqualTo(url(1L, 5L, 0, 20)))
          .willReturn(
            aResponse()
              .withStatus(INTERNAL_SERVER_ERROR)
              .withBody("boom")
          )
      )

      val ex = intercept[Exception] {
        connector.getPaymentDetails(1L, 5L, 0, 20).futureValue
      }
      ex.getMessage.toLowerCase must include("boom")
    }


    "return 400 when BE returns BAD_REQUEST " in {
      stubFor(
        get(urlEqualTo(url(1L, 5L, 0, 20)))
          .willReturn(
            aResponse()
              .withStatus(BAD_REQUEST)
              .withBody("Invalid Request")
          )
      )

      val ex = intercept[Exception] {
        connector.getPaymentDetails(1L, 5L, 0, 20).futureValue
      }
      ex.getMessage must include("Invalid Request")
    }

  }

}
