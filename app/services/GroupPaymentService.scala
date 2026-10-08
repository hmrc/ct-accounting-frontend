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

import connectors.GroupPaymentsConnector
import models.GpaPaymentsDetailsResponse
import play.api.Logging
import uk.gov.hmrc.http.HeaderCarrier
import viewmodels.{GroupPaymentArrangementViewModel, GroupPaymentRecord}
import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class GroupPaymentService @Inject() (
                                      connector: GroupPaymentsConnector
) (implicit ec: ExecutionContext) extends Logging  {

  private def getStatus(s: String): String = s match {
    case "O" | "S" => "Open"
    case "C" => "Earlier Cleared"
    case "L" => "Cleared"
    case "E" => "Earlier Exempt"
    case _ => ""
  }

  // TODO: review next mappings:
  // arrangementReference - ??
  private def transform(record : GpaPaymentsDetailsResponse, taxRef: Long) : GroupPaymentArrangementViewModel = {
    val records = record
      .gpaPayments.map( r =>
        GroupPaymentRecord(
          date = r.displayDate,
          description = r.paymentType.getOrElse(""),
          amount = r.total
        )
    )
    GroupPaymentArrangementViewModel(
      arrangementReference = taxRef.toString,
      accountEnding = record.gppEndDate,
      accountStatus = getStatus(record.gppStatus),
      paymentTotal = record.gppTotalGroupPayment,
      taxTotal = record.gppTotalGroupTax,
      records = records
    )
  }

  def getViewModel(taxRef: Long,
                   accPeriod: Long,
                   startIndex: Int,
                   count:Int)(implicit
    hc: HeaderCarrier
  ): Future[GroupPaymentArrangementViewModel] = {
    logger.info(
      s"Get Group Payment Arrangement ViewModel for taxRef: $taxRef"
    )
    connector
      .getPaymentDetails(taxRef, accPeriod, startIndex, count)
      .map(response => transform(response, taxRef))
  }
}
