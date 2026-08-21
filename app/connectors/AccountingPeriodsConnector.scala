package connectors

import models.{AccountingPeriods, AdjustmentTransactionsList}
import play.api.Logging
import uk.gov.hmrc.http.{HeaderCarrier, StringContextOps}
import uk.gov.hmrc.http.client.HttpClientV2
import uk.gov.hmrc.play.bootstrap.config.ServicesConfig

import java.net.URL
import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class AccountingPeriodsConnector @Inject() (http: HttpClientV2, config: ServicesConfig)(implicit
  ec: ExecutionContext
) extends Logging {

  def getAccountingPeriods(taxRef: Long, accPeriod: Long)(implicit
    hc: HeaderCarrier
  ): Future[AccountingPeriods] = {
    val baseUrl  = config.baseUrl("corporation-tax")
    val url: URL = url"$baseUrl/corporation-tax/accounting-periods/$taxRef"

    http
      .get(url)
      .execute[AccountingPeriods]
      .recover { case ex: Throwable =>
        logger.error(
          s"Retrieve : $taxRef :: $accPeriod - ${ex.getMessage}"
        )
        throw new RuntimeException(ex.getMessage)
      }
  }
}
