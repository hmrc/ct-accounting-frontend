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

import models.InterestAccrualList
import play.api.Logging
import uk.gov.hmrc.http.HttpReads.Implicits.*
import uk.gov.hmrc.http.client.HttpClientV2
import uk.gov.hmrc.http.{HeaderCarrier, StringContextOps, UpstreamErrorResponse}
import uk.gov.hmrc.play.bootstrap.config.ServicesConfig

import java.net.URL
import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class DebitInterestConnector @Inject() (http: HttpClientV2, config: ServicesConfig)(implicit
  ec: ExecutionContext
) extends Logging {

  // TODO: below to be confirmed
  // INTEREST_BREAKDOWN_DEBIT_DATABASE = "IDB"
  // https://github.com/hmrc/ct-core/blob/55cfafad551cbf548e2eb7a1969f58555b417419/ct-core-business/src/main/java/uk/gov/hmrc/portal/ct/business/CTBusinessConstants.java#L4
  private val interestDebitType: String = "IDB"

  def getDebitInterest(taxRef: Long, accPeriod: Long)(implicit
    hc: HeaderCarrier
  ): Future[InterestAccrualList] = {
    val baseUrl = config.baseUrl("corporation-tax")

    val url: URL = url"$baseUrl/corporation-tax/interest-accrual-list/$taxRef/$accPeriod/$interestDebitType"

    http
      .get(url)
      .execute[InterestAccrualList]
      .recover {
        case u: UpstreamErrorResponse =>
          logger.error(
            s"[DebitInterestConnector][getInterest] Upstream error: $taxRef :: $accPeriod - ${u.getMessage}"
          )
          throw u
        case ex: Throwable            =>
          logger.error(
            s"[DebitInterestConnector][getInterest]: $taxRef :: $accPeriod - ${ex.getMessage}"
          )
          throw new RuntimeException(ex.getMessage)
      }
  }

}
