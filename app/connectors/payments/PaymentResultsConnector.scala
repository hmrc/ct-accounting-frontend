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

package connectors.payments

import models.payments.PaymentSearchResultResponse
import play.api.Logging
import uk.gov.hmrc.http.HttpReads.Implicits.*
import uk.gov.hmrc.http.client.HttpClientV2
import uk.gov.hmrc.http.{HeaderCarrier, StringContextOps}
import uk.gov.hmrc.play.bootstrap.config.ServicesConfig
import uk.gov.hmrc.{http as StringContextOps, *}

import java.net.URL
import java.time.LocalDate
import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class PaymentResultsConnector @Inject() (http: HttpClientV2, config: ServicesConfig)(implicit
  ec: ExecutionContext
) extends Logging {

  def getPaymentResults(
    taxRef: Long,
    userEnteredPaymentAmount: BigDecimal,
    fromDate: Option[LocalDate],
    toDate: Option[LocalDate]
  )(implicit
    hc: HeaderCarrier
  ): Future[PaymentSearchResultResponse] = {
    val baseUrl  = config.baseUrl("corporation-tax")
    val url: URL =
      url"$baseUrl/corporation-tax/payment-search-result/$taxRef?fromDate=$fromDate&toDate=$toDate&amount=$userEnteredPaymentAmount"

    http
      .get(url)
      .execute[PaymentSearchResultResponse]
      .recover { case ex: Throwable =>
        logger.error(
          s"Retrieve Payment Results: taxRef :: $taxRef, fromDate :: $fromDate, toDate :: $toDate, userEnteredPaymentAmount :: $userEnteredPaymentAmount - ${ex.getMessage}"
        )
        throw new RuntimeException(ex.getMessage)
      }
  }

}
