package services.payments

import connectors.payments.PaymentsOnAccountConnector
import models.payments.PaymentsOnAccountResponse
import play.api.Logging
import uk.gov.hmrc.http.HeaderCarrier

import javax.inject.Inject
import scala.concurrent.Future

class PaymentsOnAccountService @Inject() (
                                         connector: PaymentsOnAccountConnector
                                       ) extends Logging {

  def getPaymentsOnAccount(taxRef: Long, startIndex: Int, count: Int)(implicit
                                                                                                    hc: HeaderCarrier
  ): Future[PaymentsOnAccountResponse] = {
    logger.info(
      s"Calling PaymentsOnAccountConnector for taxRef: $taxRef, StartIndex: $startIndex, pCount: $count"
    )
    connector
      .getPaymentsOnAccount(taxRef, startIndex, count)
  }
}
