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
import viewmodels.{GroupPaymentArrangementViewModel, GroupPaymentRecord}

import java.time.LocalDate
import javax.inject.Inject
import scala.concurrent.Future

class GroupPaymentService @Inject()(
) extends Logging {

  def getViewModel(taxRef: Long, accPeriod: Long)(implicit
    hc: HeaderCarrier
  ): Future[GroupPaymentArrangementViewModel] = {
    logger.info(
      s"Get Group Payment Arrangement ViewModel for taxRef: $taxRef and accPeriod: $accPeriod"
    )
    val record = GroupPaymentArrangementViewModel(
      arrangementReference = "933636936A00104A",
      accountEnding = LocalDate.of(2026, 1, 1),
      accountStatus = "Open",
      paymentTotal = BigDecimal(1125000),
      taxTotal = BigDecimal(1125000),
      records = List(
        GroupPaymentRecord(
          date = LocalDate.of(2025, 1, 1), description = "Electronic payment", amount = BigDecimal(50.17)
        ),
        GroupPaymentRecord(
          date = LocalDate.of(2021, 2, 7), description = "Electronic payment", amount = BigDecimal(475)
        ),
        GroupPaymentRecord(
          date = LocalDate.of(2026, 4, 8), description = "Electronic payment", amount = BigDecimal(50.18)
        )
      )
    )
    Future.successful(record)
  }
}
