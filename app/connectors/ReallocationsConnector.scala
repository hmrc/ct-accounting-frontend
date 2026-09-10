package connectors


import models.Repayments
import play.api.Logging
import uk.gov.hmrc.*
import uk.gov.hmrc.http.HttpReads.Implicits.*
import uk.gov.hmrc.http.client.HttpClientV2
import uk.gov.hmrc.http.{HeaderCarrier, StringContextOps}
import uk.gov.hmrc.play.bootstrap.config.ServicesConfig

import java.net.URL
import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class ReallocationsConnector @Inject() (http: HttpClientV2, config: ServicesConfig)(implicit ec: ExecutionContext)
  extends Logging {

  def getByAccountingPeriod(taxRef: Long, accPeriod: Long)(implicit hc: HeaderCarrier): Future[Repayments] = {
    val url: URL = url"${config.baseUrl("corporation-tax")}/corporation-tax/reallocation-to-accounting-period/$taxRef/$accPeriod"
    http
      .get(url)
      .execute[Repayments]
      .recover { case e: Throwable =>
        logger.error(s"[ReallocationsConnector][getByAccountingPeriod]: $taxRef :: $accPeriod - ${e.getMessage}")
        throw new RuntimeException(e.getMessage)
      }
  }

}