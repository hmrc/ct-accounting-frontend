package connectors

import models.DisplayNeeded
import play.api.Logging
import uk.gov.hmrc.http.client.HttpClientV2
import uk.gov.hmrc.http.{HeaderCarrier, StringContextOps}
import uk.gov.hmrc.play.bootstrap.config.ServicesConfig
import uk.gov.hmrc.http.HttpReads.Implicits.*

import java.net.URL
import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class DisplayNeededConnector @Inject() (http: HttpClientV2, config: ServicesConfig)(implicit
                                                                                        ec: ExecutionContext
) extends Logging {

  def getDisplayNeeded(taxRef: Long, accPeriod: Long)(implicit
                                         hc: HeaderCarrier
  ): Future[DisplayNeeded] = {
    val baseUrl  = config.baseUrl("corporation-tax")
    val url: URL = url"$baseUrl/corporation-tax/display-needed/$taxRef/$accPeriod"

    http
      .get(url)
      .execute[DisplayNeeded]
      .recover { case ex: Throwable =>
        logger.error(
          s"Retrieve Display Needed : $taxRef :: $accPeriod - ${ex.getMessage}"
        )
        throw new RuntimeException(ex.getMessage)
      }
  }
}