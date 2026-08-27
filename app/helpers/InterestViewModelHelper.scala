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

package helpers

import models.{AccountingPeriodsRowResponse, MissingAccountingPeriodError, MissingDataError}
import play.api.i18n.Messages
import services.{AccountingPeriodsService, InterestService}
import uk.gov.hmrc.http.HeaderCarrier
import viewmodels.InterestViewModel

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class InterestViewModelHelper @Inject() (
  interestService: InterestService,
  accountingPeriodService: AccountingPeriodsService
)(implicit ec: ExecutionContext, hc: HeaderCarrier, messages: Messages) {

  def deriveInterestViewModel(taxRef: Long, accPeriod: Long): Future[Either[MissingDataError, InterestViewModel]] =
    for {
      accountingPeriodsDetails <- interestService.getAccountingPeriodResponse(taxRef, accPeriod)
      accountingPeriod         <- accountingPeriodService.getAccountingPeriods(taxRef)
    } yield accountingPeriod.accountingPeriods
      .find(_.accountingPeriod == accPeriod)
      .toRight(MissingAccountingPeriodError(s"Cannot find the matching accounting period for taxRef:$taxRef"))
      .map { accPeriodWithValue =>
        val clericalCalculationFlag = findClericalFlag(accPeriodWithValue)
        InterestViewModel.toViewModel(accountingPeriodsDetails, clericalCalculationFlag)
      }

  private def findClericalFlag(accPeriodRowResponse: AccountingPeriodsRowResponse): Boolean =
    accPeriodRowResponse.clericalIntSig || accPeriodRowResponse.creditDebitInterestInd

}
