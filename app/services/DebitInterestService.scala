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

import play.api.Logging
import uk.gov.hmrc.http.HeaderCarrier
import viewmodels.{DebitInterestRow, DebitInterestViewModel}

import java.time.LocalDate
import javax.inject.Inject
import scala.concurrent.Future

class DebitInterestService @Inject()(

                                    ) extends Logging {

  def getAccountingPeriods(taxRef: Long, accPeriod: Long)
                          (implicit hc: HeaderCarrier): Future[DebitInterestViewModel] = {
    // TODO: acquire data from relevant sources and build viewModel :: clarify optionality
    val viewModel = DebitInterestViewModel(
      interest = None,
      rows = List(
        DebitInterestRow(
          unpaidAmount = BigDecimal(17.01),
          fromDate = LocalDate.of(2026, 1, 1),
          toDate = LocalDate.of(2026, 1, 1),
          noOfDays = 1,
          rate = BigDecimal(1.5),
          interestAmount = BigDecimal(99.11)
        ),
        DebitInterestRow(
          unpaidAmount = BigDecimal(7.01),
          fromDate = LocalDate.of(2025, 2, 1),
          toDate = LocalDate.of(2025, 2, 1),
          noOfDays = 11,
          rate = BigDecimal(4.5),
          interestAmount = BigDecimal(278.13)
        )
      )
    )
    Future.successful( viewModel )
  }

}
