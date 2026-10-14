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
import itutils.ApplicationWithWiremock
import models.{AccountPositionResponse, ApAmountRecord}
import org.scalatest.BeforeAndAfterEach
import org.scalatest.concurrent.{IntegrationPatience, ScalaFutures}
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec
import play.api.http.Status.*
import play.api.libs.json.Json
import uk.gov.hmrc.http.HeaderCarrier

import java.time.LocalDate

class AccountPositionConnectorISpec
    extends AnyWordSpec
    with Matchers
    with ScalaFutures
    with IntegrationPatience
    with ApplicationWithWiremock
    with BeforeAndAfterEach {

  implicit val hc: HeaderCarrier = HeaderCarrier()

  private val connector: AccountPositionConnector = app.injector.instanceOf[AccountPositionConnector]

  "getAccountPosition" should {

    def url(taxRef: Long) =
      s"/corporation-tax/account-position/$taxRef"

    val apOne: ApAmountRecord =
      ApAmountRecord(
        accountingPeriod = 1L,
        apEndDate = Some(LocalDate.of(2026, 1, 1)),
        amountDueForAp = 123,
        apStatus = Some("L")
      )

    val apTwo: ApAmountRecord =
      ApAmountRecord(
        accountingPeriod = 2L,
        apEndDate = Some(LocalDate.of(2025, 1, 1)),
        amountDueForAp = 456,
        apStatus = Some("S")
      )

    val emptyResponse = AccountPositionResponse(
      amountDue = 0,
      asOnDate = None,
      gpaLinkFlag = false,
      taxpayerList = List.empty,
      apAmounts = List.empty,
      doesCompanyExist = false
    )

    val responseApAmountRecord = AccountPositionResponse(
      amountDue = 15,
      asOnDate = Some(LocalDate.of(2026, 1, 1)),
      gpaLinkFlag = true,
      taxpayerList = List("123", "456"),
      apAmounts = List(apOne, apTwo),
      doesCompanyExist = true
    )

    "return Account Position empty response from BE with status code OK" in {

      stubFor(
        get(urlPathEqualTo(url(1L)))
          .willReturn(
            aResponse()
              .withStatus(OK)
              .withBody(Json.stringify(Json.toJson(emptyResponse)))
          )
      )

      val result = connector.getAccountPosition(1L).futureValue
      result mustEqual emptyResponse
    }

    "return Account Position list with all fields populated from BE with status code OK" in {
      stubFor(
        get(urlPathEqualTo(url(1L)))
          .willReturn(
            aResponse()
              .withStatus(OK)
              .withBody(Json.stringify(Json.toJson(responseApAmountRecord)))
          )
      )

      val result = connector.getAccountPosition(1L).futureValue
      result mustEqual responseApAmountRecord
    }

    "return failure when downstream returns INTERNAL_SERVER_ERROR" in {
      stubFor(
        get(urlPathEqualTo(url(1L)))
          .willReturn(
            aResponse()
              .withStatus(INTERNAL_SERVER_ERROR)
          )
      )

      val ex = intercept[Exception] {
        connector.getAccountPosition(1L).futureValue
      }

      ex.getMessage must include("500")
    }
  }
}
