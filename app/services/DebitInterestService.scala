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

package services

import connectors.DebitInterestConnector
import play.api.Logging
import uk.gov.hmrc.http.HeaderCarrier
import viewmodels.{DebitInterestRow, DebitInterestViewModel}

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class DebitInterestService @Inject(
  connector: DebitInterestConnector
) (implicit ec: ExecutionContext) extends Logging {

  def getAccountingPeriods(taxRef: Long, accPeriod: Long, interestType: String)(implicit
    hc: HeaderCarrier
  ): Future[DebitInterestViewModel] = {
    connector
      .getDebitInterest(taxRef = taxRef, accPeriod = accPeriod, interestType = interestType)
      .map(response =>

        DebitInterestViewModel(
          interest = None,
          rows =
            response.interestAccruals.map(item =>
              DebitInterestRow(
                unpaidAmount = item.computationAmount,
                fromDate = item.interestAccrualFromDate,
                toDate = item.interestAccrualFromDate,
                noOfDays = 0, // TODO: computed by backend
                rate = item.interestRate,
                interestAmount = item.interestAmount
              )
            )
        )

      )
  }

}
