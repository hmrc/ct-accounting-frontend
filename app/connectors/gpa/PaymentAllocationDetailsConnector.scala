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

import models.gpa.PaymentAllocationDetails
import play.api.Logging
import uk.gov.hmrc.http.HttpReads.Implicits.*
import uk.gov.hmrc.http.client.HttpClientV2
import uk.gov.hmrc.http.{HeaderCarrier, StringContextOps}
import uk.gov.hmrc.play.bootstrap.config.ServicesConfig
import uk.gov.hmrc.{http as StringContextOps, *}

import java.net.URL
import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class PaymentAllocationDetailsConnector @Inject() (http: HttpClientV2, config: ServicesConfig)(implicit
  ec: ExecutionContext
) extends Logging {

  def getGPAPaymentAllocationDetail(
    gpaUtr: Long,
    gppContractVersion: Long,
    participatorUtr: Long,
    participatorAp: Long,
    startIndex: Int,
    count: Int
  )(implicit
    hc: HeaderCarrier
  ): Future[PaymentAllocationDetails] = {
    val baseUrl  = config.baseUrl("corporation-tax")
    val url: URL =
      url"$baseUrl/corporation-tax/gpa-payment-allocation-details/$gpaUtr/$gppContractVersion/$participatorUtr/$participatorAp?startIndex=$startIndex&count=$count"

    http
      .get(url)
      .execute[PaymentAllocationDetails]
      .recover { case ex: Throwable =>
        logger.error(
          s"Error receiving payment allocation details for: $gpaUtr :: $gppContractVersion :: $participatorUtr :: $participatorAp :: $startIndex :: $count - ${ex.getMessage}"
        )
        throw new RuntimeException(ex.getMessage)
      }
  }
}
