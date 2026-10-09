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

case class GroupReferenceNumberLstItem(
  taxpayerReference: Long
)

object GroupReferenceNumberLstItem {
  implicit val format: OFormat[GroupReferenceNumberLstItem] = Json.format[GroupReferenceNumberLstItem]
}

// BE
case class GroupSummaryDetailsRecord(
  contractEndDate: LocalDate,
  groupTaxCharge: BigDecimal,
  groupPayment: BigDecimal,
  groupPaymentRecordCount: Int,
  contractStatus: String,
  contractVersion: Int
)

object GroupSummaryDetailsRecord {
  implicit val format: OFormat[GroupSummaryDetailsRecord] = Json.format[GroupSummaryDetailsRecord]
}

case class GroupSummaryDetailsResponse(
  gpaGrpSummaryDetails: List[GroupSummaryDetailsRecord],
  gpaReferenceNumberLst: List[GroupReferenceNumberLstItem],
  nominatedCompanyName: String
)

object GroupSummaryDetailsResponse {
  implicit val format: OFormat[GroupSummaryDetailsResponse] = Json.format[GroupSummaryDetailsResponse]
}
