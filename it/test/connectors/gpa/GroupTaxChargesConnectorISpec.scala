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

import com.github.tomakehurst.wiremock.client.WireMock.*
import helpers.gpa.GroupTaxChargesHelper
import itutils.ApplicationWithWiremock
import models.gpa.GpaGroupTaxCharges
import org.scalatest.BeforeAndAfterEach
import org.scalatest.concurrent.{IntegrationPatience, ScalaFutures}
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec
import play.api.http.Status.*
import uk.gov.hmrc.http.HeaderCarrier

class GroupTaxChargesConnectorISpec
    extends AnyWordSpec
    with Matchers
    with ScalaFutures
    with IntegrationPatience
    with ApplicationWithWiremock
    with BeforeAndAfterEach
    with GroupTaxChargesHelper {

  implicit val hc: HeaderCarrier = HeaderCarrier()

  private val connector: GroupTaxChargesConnector = app.injector.instanceOf[GroupTaxChargesConnector]

  private val gpaUTR: Long         = 1L;
  private val contractVersion: Int = 1;
  private val startIndex: Int      = 0;
  private val count: Int           = 10;

  "getGpaGroupTaxCharges" should {

    def url(pGpaUtr: Long, pGppContractVersion: Int, pStartIndex: Int, pCount: Int) =
      s"/corporation-tax/group-tax-charges/$pGpaUtr/$pGppContractVersion"

    "return Group Tax Charges with status code OK" in {
      stubFor(
        get(urlPathEqualTo(url(gpaUTR, contractVersion, startIndex, count)))
          .willReturn(
            aResponse()
              .withStatus(OK)
              .withBody(
                s"""
                   |{
                   |  "pGppEndDate": "2026-03-31",
                   |  "pGppTotalGroupPayment": 125000.5,
                   |  "pGppTotalGroupTax": 31250.25,
                   |  "pGppStatus": "L",
                   |  "pGppCni": "2026-01-01",
                   |  "pGppApportionmentMethod": "E",
                   |  "pGpaUtr2": 23456789012,
                   |  "pTotalNumOfRecords": 2,
                   |  "pGroupPaymentRecordCount": 2,
                   |  "pCurGroupTaxCharges": [
                   |    {
                   |      "participatorName": "Example Holdings Ltd",
                   |      "participatorReference": 34567890123,
                   |      "participatorApEndDate": "2026-03-31",
                   |      "participatorTaxCharge": 18750.125,
                   |      "participatorTaxChargePrsnt": "Y",
                   |      "participatorAccountingPeriod": 2026001,
                   |      "contractVersion": 1,
                   |      "allocatedPayment": 62500.25,
                   |      "allocatedPaymentRecordCount": 1
                   |    },
                   |    {
                   |      "participatorName": "Example Trading Ltd",
                   |      "participatorReference": 45678901234,
                   |      "participatorApEndDate": "2026-03-31",
                   |      "participatorTaxCharge": 12500.125,
                   |      "participatorTaxChargePrsnt": "Y",
                   |      "participatorAccountingPeriod": 2026001,
                   |      "contractVersion": 1,
                   |      "allocatedPayment": 62500.25,
                   |      "allocatedPaymentRecordCount": 1
                   |    }
                   |  ]
                   |}
                   |""".stripMargin
              )
          )
      )

      val result = connector.getGpaGroupTaxCharges(gpaUTR, contractVersion, startIndex, count).futureValue
      result mustEqual gpaGroupTaxChargesWithTwoParticipators
    }

    "return INTERNAL_ERROR when BE failed" in {
      stubFor(
        get(urlPathEqualTo(url(gpaUTR, contractVersion, startIndex, count)))
          .willReturn(
            aResponse()
              .withStatus(INTERNAL_SERVER_ERROR)
              .withBody("Error")
          )
      )

      val ex = intercept[Exception] {
        connector.getGpaGroupTaxCharges(gpaUTR, contractVersion, startIndex, count).futureValue
      }
      ex.getMessage.toLowerCase must include("error")
    }

    "return 400 when BE returns BAD_REQUEST " in {
      stubFor(
        get(urlPathEqualTo(url(gpaUTR, contractVersion, startIndex, count)))
          .willReturn(
            aResponse()
              .withStatus(BAD_REQUEST)
              .withBody("Invalid Request")
          )
      )

      val ex = intercept[Exception] {
        connector.getGpaGroupTaxCharges(gpaUTR, contractVersion, startIndex, count).futureValue
      }
      ex.getMessage must include("Invalid Request")
    }

    "return 404 when BE returns NOT_FOUND " in {
      stubFor(
        get(urlPathEqualTo(url(gpaUTR, contractVersion, startIndex, count)))
          .willReturn(
            aResponse()
              .withStatus(NOT_FOUND)
              .withBody("Not found")
          )
      )

      val ex = intercept[Exception] {
        connector.getGpaGroupTaxCharges(gpaUTR, contractVersion, startIndex, count).futureValue
      }
      ex.getMessage must include("Not found")
    }
  }
}
