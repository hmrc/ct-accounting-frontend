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

package models.gpa

import play.api.libs.json.{Json, OFormat}

import java.time.LocalDate

case class GpaPayments(
  displayDate: Option[LocalDate],
  total: BigDecimal,
  tablename: Option[String],
  paymentType: Option[String],
  repaymentType: Option[String],
  targetTaxpayerReference: Option[String],
  targetApNo: Option[Int],
  targetApEndDate: Option[LocalDate],
  contractEndDate: Option[LocalDate],
  participatorPresent: Option[Boolean]
)

object GpaPayments {
  implicit val format: OFormat[GpaPayments] = Json.format[GpaPayments]
}

case class GpaPaymentDetails(
  gpaPayments: List[GpaPayments],
  totalNumOfRecords: Option[Long],
  gppEndDate: Option[LocalDate],
  gppTotalGroupPayment: BigDecimal,
  gppTotalGroupTax: BigDecimal,
  gppStatus: String,
  gppCni: Option[LocalDate],
  gppApportionmentMethod: Option[String]
)

object GpaPaymentsDetailsResponse {
  implicit val format: OFormat[GpaPaymentDetails] = Json.format[GpaPaymentDetails]
}
