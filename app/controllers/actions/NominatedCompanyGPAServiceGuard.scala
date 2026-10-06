package controllers.actions

import config.FrontendAppConfig
import connectors.gpa.{CompanyNominatorConnector, GroupPaymentPeriodInRangeConnector}
import models.AuthenticatedRequest
import play.api.Logging
import play.api.mvc.Results.Redirect
import play.api.mvc.{ActionFilter, Result}
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.play.http.HeaderCarrierConverter

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class NominatedCompanyGPAServiceGuard @Inject() (
  companyNominatorConnector: CompanyNominatorConnector,
  groupPaymentPeriodInRangeConnector: GroupPaymentPeriodInRangeConnector,
  config: FrontendAppConfig
)(implicit val executionContext: ExecutionContext)
    extends ActionFilter[AuthenticatedRequest]
    with Logging {

  override protected def filter[A](request: AuthenticatedRequest[A]): Future[Option[Result]] = {

    implicit val hc: HeaderCarrier = HeaderCarrierConverter.fromRequestAndSession(request, request.session)

    val gpaUtr              = request.gpaUtr
    val nominatedCompanyUtr = request.nominatedCompanyUtr
    val pPeriod             = request.pPeriod
    val pMonthRestriction   = request.pMonthRestriction

    // TODO Change this redirectOnError to point to Error Page
    val redirectOnError = Redirect(controllers.routes.JourneyRecoveryController.onPageLoad())

    if (pMonthRestriction <= config.pMonthsRestriction) {
      for {
        companyNominatorResponse   <- companyNominatorConnector.getCompanyNominator(gpaUtr, nominatedCompanyUtr)
        groupPaymentPeriodResponse <-
          groupPaymentPeriodInRangeConnector
            .getGroupPaymentPeriodInRange(gpaUtr, nominatedCompanyUtr, pPeriod, pMonthRestriction)
      } yield
        if (companyNominatorResponse.isParticipator && groupPaymentPeriodResponse.isPeriodWithinRange) {
          logger.info(
            s"Successfully validated nominatedCompany belongs to Group Payment Arrangement and Group Payment Period"
          )
          None
        } else {
          logger.info(s"Validation failure for nominatedCompany")
          Some(redirectOnError)
        }
    } else {
      logger.error(
        s"Group Payment Period is greater than 5 years :: pMonthsRestriction:: $pMonthRestriction"
      )
      Future.successful(Some(redirectOnError))
    }

  }

}
