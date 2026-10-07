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
import scala.util.control.NonFatal

@Singleton
class NominatedCompanyGPAServiceGuard @Inject() (
  companyNominatorConnector: CompanyNominatorConnector,
  groupPaymentPeriodInRangeConnector: GroupPaymentPeriodInRangeConnector,
  config: FrontendAppConfig
)(implicit val executionContext: ExecutionContext)
    extends ActionFilter[AuthenticatedRequest]
    with Logging {

  def filter[A](request: AuthenticatedRequest[A]): Future[Option[Result]] = {

    implicit val hc: HeaderCarrier = HeaderCarrierConverter.fromRequestAndSession(request, request.session)

    val gpaUtr              = request.gpaUtr
    val nominatedCompanyUtr = request.nominatedCompanyUtr
    val pPeriod             = request.pPeriod
    val pMonthRestriction   = request.pMonthRestriction

    // TODO Change this redirectOnError to point to Error Page
    val redirectOnError = Redirect(controllers.routes.JourneyRecoveryController.onPageLoad())

    if (pMonthRestriction <= config.pMonthsRestriction) {
      (for {
        companyNominatorResponse   <- companyNominatorConnector.getCompanyNominator(gpaUtr, nominatedCompanyUtr)
        groupPaymentPeriodResponse <-
          groupPaymentPeriodInRangeConnector
            .getGroupPaymentPeriodInRange(gpaUtr, nominatedCompanyUtr, pPeriod, pMonthRestriction)
      } yield
        if (companyNominatorResponse.isParticipator && groupPaymentPeriodResponse.isPeriodWithinRange) {
          logger.info(
            s"Successfully validated nominatedCompany gpaUTR :: $gpaUtr, nominatedCompanyUtr :: $nominatedCompanyUtr"
          )
          None
        } else {
          logger.info(
            s"Validation failure for nominatedCompany gpaUTR :: $gpaUtr, nominatedCompanyUtr :: $nominatedCompanyUtr"
          )
          Some(redirectOnError)
        }).recover { case NonFatal(e) =>
        logger.error(
          s"Error while validating nominated company gpaUTR :: $gpaUtr, nominatedCompanyUtr :: $nominatedCompanyUtr"
        )
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
