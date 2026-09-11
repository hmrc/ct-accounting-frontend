package services

import com.google.inject.Inject
import connectors.{ReallocationsConnector, RepaymentsConnector}
import play.api.Logging
import play.api.i18n.Messages
import uk.gov.hmrc.http.HeaderCarrier
import viewmodels.{ReallocationRepaymentsViewModel, ReallocationRepaymentsViewModelRow}

import java.time.LocalDate
import scala.concurrent.{ExecutionContext, Future}
import models.PenaltyTransactionType.*
import models.{Reallocations, Repayments}

class ReallocationsRepaymentsService @Inject() (
                                                 reallocationsConnector: ReallocationsConnector,
                                                 repaymentsConnector: RepaymentsConnector
                                 )(implicit ec: ExecutionContext)
  extends Logging {

  def getReallocationsRepayments(taxRef: Long, accPeriod: Long)(implicit
                                                                 hc: HeaderCarrier
  ): Future[ReallocationRepaymentsViewModel] = {
    logger.info(
      s"[ReallocationsRepaymentsService][getAccountingPeriodResponse]:Calling reallocations and repayments connectors for taxRef: $taxRef and accPeriod: $accPeriod"
    )
    val reallocationsList = reallocationsConnector.getByAccountingPeriod(taxRef, accPeriod).map { response => {
      
    }}
    val repaymentsList = repaymentsConnector.getRepayments(taxRef, accPeriod).map { response => {

    }}
    
    val vm: ReallocationRepaymentsViewModel = ReallocationRepaymentsViewModel(reallocationsList, repaymentsList)
  }
  
}
