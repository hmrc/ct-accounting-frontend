package connectors.payments

import models.payments.PaymentsOnAccountResponse
import play.api.Logging
import uk.gov.hmrc.http.{HeaderCarrier, StringContextOps}
import uk.gov.hmrc.http.client.HttpClientV2
import uk.gov.hmrc.play.bootstrap.config.ServicesConfig
import uk.gov.hmrc.http.HttpReads.Implicits.*

import java.net.URL
import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class PaymentsOnAccountConnector @Inject() (http: HttpClientV2, config: ServicesConfig)(implicit
  ec: ExecutionContext
) extends Logging {

  def getPaymentsOnAccount(taxRef: Long, startIndex: Int, count: Int)(implicit
    hc: HeaderCarrier
  ): Future[PaymentsOnAccountResponse] = {
    val baseUrl  = config.baseUrl("corporation-tax")
    val url: URL = url"$baseUrl/corporation-tax/payments-on-account/$taxRef?startIndex=$startIndex&count=$count"

    http
      .get(url)
      .execute[PaymentsOnAccountResponse]
      .recover { case e: Throwable =>
        logger.error(s"Calling PaymentsOnAccountConnector $taxRef- ${e.getMessage}")
        throw new RuntimeException(e.getMessage)
      }
  }

}
