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

import models.gpa.GpaGroupTaxCharges
import play.api.Logging
import uk.gov.hmrc.http.HttpReads.Implicits.*
import uk.gov.hmrc.http.client.HttpClientV2
import uk.gov.hmrc.http.{HeaderCarrier, StringContextOps, UpstreamErrorResponse}
import uk.gov.hmrc.play.bootstrap.config.ServicesConfig
import uk.gov.hmrc.{http as StringContextOps, *}

import java.net.URL
import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class GroupTaxChargesConnector @Inject() (http: HttpClientV2, config: ServicesConfig)(implicit
                                                                                              ec: ExecutionContext
) extends Logging {

  def getGpaGroupTaxCharges( pGpaUtr: Long,
                             pGppContractVersion: Int,
                             pStartIndex: Int,
                             pCount: Int)(implicit
                                                                 hc: HeaderCarrier
  ): Future[GpaGroupTaxCharges] = {
    val baseUrl  = config.baseUrl("corporation-tax")
    val url: URL = url"$baseUrl/corporation-tax/group-tax-charges/$pGpaUtr/$pGppContractVersion"

    http
      .get(url)
      .execute[GpaGroupTaxCharges]
      .recover {
        case u: UpstreamErrorResponse =>
          logger.error(
            s"Upstream error: $pGpaUtr :: $pGppContractVersion - ${u.getMessage}"
          )
          throw u
        case ex: Throwable            =>
          logger.error(
            s"Unexpected Error: $pGpaUtr :: $pGppContractVersion - ${ex.getMessage}"
          )
          throw new RuntimeException(ex.getMessage)
      }
  }

}
