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

import models.payments.PaymentsOnAccountResponse
import play.api.Logging
import uk.gov.hmrc.http.{HeaderCarrier, StringContextOps}
import uk.gov.hmrc.http.client.HttpClientV2
//import uk.gov.hmrc.play.bootstrap.config.ServicesConfig
import uk.gov.hmrc.http.HttpReads.Implicits.*

import java.net.URL
import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

// todo: restore "config: ServicesConfig" as parameter
class PaymentsOnAccountConnector @Inject() (http: HttpClientV2)(implicit
  ec: ExecutionContext
) extends Logging {

  def getPaymentsOnAccount(taxRef: Long, startIndex: Int, count: Int)(implicit
    hc: HeaderCarrier
  ): Future[PaymentsOnAccountResponse] = {
    //val baseUrl  = config.baseUrl("corporation-tax")

    // todo: remove temporary url
    val baseUrl  = "http://localhost:11203/corporation-tax-stubs"
    val url: URL = url"$baseUrl/corporation-tax/payments-on-account/$taxRef"

    http
      .get(url)
      .execute[PaymentsOnAccountResponse]
      .recover { case e: Throwable =>
        logger.error(s"Calling PaymentsOnAccountConnector $taxRef- ${e.getMessage}")
        throw new RuntimeException(e.getMessage)
      }
  }

}
