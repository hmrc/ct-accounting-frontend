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

import models.RepayReallocationSummary
import models.payments.{ElectronicPaymentAllocationDetails, PaymentAllocationChequeDetails}
import utils.DeterminePaymentAllocationSerializer._
import play.api.Logging
import uk.gov.hmrc.http.HttpReads.Implicits.*
import uk.gov.hmrc.http.client.HttpClientV2
import uk.gov.hmrc.http.{HeaderCarrier, StringContextOps}
import uk.gov.hmrc.play.bootstrap.config.ServicesConfig

import java.net.URL
import java.time.LocalDate
import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class PaymentAllocationConnector @Inject()(http: HttpClientV2, config: ServicesConfig)(implicit
                                                                                       ec: ExecutionContext
) extends Logging {

  def getPaymentType(taxRef: Long,
                     fromDate: Option[LocalDate],
                     toDate: Option[LocalDate],
                     userEnteredPaymentAmount: BigDecimal
                    )(implicit
    hc: HeaderCarrier
  ): Future[List[Option[Either[ElectronicPaymentAllocationDetails, PaymentAllocationChequeDetails]]]] = {
    val baseUrl  = config.baseUrl("corporation-tax")
    val url: URL = url"$baseUrl/corporation-tax/determine-payment-allocation/$taxRef?fromDate=$fromDate&toDate=$toDate"

    http
      .get(url)
      .execute[List[Option[Either[ElectronicPaymentAllocationDetails, PaymentAllocationChequeDetails]]]]
      .recover { case ex: Throwable =>
        logger.error(
          s"Unable to retrieve payment allocation for: $taxRef, $accPeriod:: - ${ex.getMessage}"
        )
        throw new RuntimeException(ex.getMessage)
      }
  }

}
