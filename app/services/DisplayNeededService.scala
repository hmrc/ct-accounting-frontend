package services

import connectors.DisplayNeededConnector
import models.DisplayNeeded
import play.api.Logging
import uk.gov.hmrc.http.HeaderCarrier

import javax.inject.Inject
import scala.concurrent.Future

class DisplayNeededService @Inject() (
  connector: DisplayNeededConnector
) extends Logging {

  def getDisplayNeeded(taxRef: Long, accPeriod: Long)(implicit
    hc: HeaderCarrier
  ): Future[DisplayNeeded] = {
    logger.info(
      s"[DisplayNeededService][getDisplayNeeded]:Calling DisplayNeededConnector for taxRef: $taxRef and accPeriod: $accPeriod"
    )
    connector
      .getDisplayNeeded(taxRef, accPeriod)
  }
}
