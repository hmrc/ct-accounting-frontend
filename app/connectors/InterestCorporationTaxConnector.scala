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

import models.AccountingPeriodResponse
import play.api.Logging
import uk.gov.hmrc.http.HttpReads.Implicits.*
import uk.gov.hmrc.http.client.HttpClientV2
import uk.gov.hmrc.http.{HeaderCarrier, StringContextOps, UpstreamErrorResponse}
import uk.gov.hmrc.play.bootstrap.config.ServicesConfig
import uk.gov.hmrc.{http as StringContextOps, *}

import java.net.URL
import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class InterestCorporationTaxConnector @Inject() (http: HttpClientV2, config: ServicesConfig)(implicit
  ec: ExecutionContext
) extends Logging {

  def getAccountingPeriodResponse(taxRef: Long, accPeriod: Long)(implicit
    hc: HeaderCarrier
  ): Future[AccountingPeriodResponse] = {
    val baseUrl  = config.baseUrl("corporation-tax")
    val url: URL = url"$baseUrl/corporation-tax/accounting-period-details/$taxRef/$accPeriod"

    http
      .get(url)
      .execute[AccountingPeriodResponse]
      .recover {
        case u: UpstreamErrorResponse =>
          logger.error(
            s"[InterestConnector][getInterest] Upstream error: $taxRef :: $accPeriod - ${u.getMessage}"
          )
          throw u
        case ex: Throwable            =>
          logger.error(
            s"[InterestConnector][getInterest]: $taxRef :: $accPeriod - ${ex.getMessage}"
          )
          throw new RuntimeException(ex.getMessage)
      }
  }

}
