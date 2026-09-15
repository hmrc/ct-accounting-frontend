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

import models.InterestAccrualListWithInterestAccruedDays
import play.api.Logging
import uk.gov.hmrc.*
import uk.gov.hmrc.http.HttpReads.Implicits.*
import uk.gov.hmrc.http.client.HttpClientV2
import uk.gov.hmrc.http.{HeaderCarrier, StringContextOps}
import uk.gov.hmrc.play.bootstrap.config.ServicesConfig

import java.net.URL
import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class InterestAccrualConnector @Inject()(http: HttpClientV2, config: ServicesConfig)(implicit ec: ExecutionContext)
    extends Logging {
  def getInterestAccrual(taxRef: Long, accPeriod: Long, interestType: String)(implicit hc: HeaderCarrier): Future[InterestAccrualListWithInterestAccruedDays] = {
    val url: URL = url"${config.baseUrl("corporation-tax")}/corporation-tax/interest-accrual-list/$taxRef/$accPeriod/$interestType"
    http
      .get(url)
      .execute[InterestAccrualListWithInterestAccruedDays]
      .recover { case e: Throwable =>
        logger.error(s"Error calling repayment interest accrual list for: $taxRef :: $accPeriod - ${e.getMessage}")
        throw new RuntimeException(e.getMessage)
      }
  }

}
