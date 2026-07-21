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

package models

import play.api.libs.json.*

case class AdjustmentTransactionsList(adjustmentTransactionsList: List[AdjustmentTransactions])

object AdjustmentTransactionsList {
  implicit val format: OFormat[AdjustmentTransactionsList] = Json.format[AdjustmentTransactionsList]
}

case class AdjustmentTransactions(
  amount: BigDecimal,
  `type`: AdjustmentTransactionType
)

object AdjustmentTransactions {
  implicit val format: OFormat[AdjustmentTransactions] = Json.format[AdjustmentTransactions]
}

enum AdjustmentTransactionType {
  case N, O, P
}

object AdjustmentTransactionType {
  def asString(att: AdjustmentTransactionType): String =
    att match {
      case N => "Not currently being pursued"
      case O => "Permanent overpayment"
      case P => "Postponement"
    }
}

implicit val adjustmentTransactionType: Format[AdjustmentTransactionType] = new Format[AdjustmentTransactionType] {
  def reads(json: JsValue): JsResult[AdjustmentTransactionType] = json match {
    case JsString(s) =>
      s.toUpperCase() match {
        case "N" => JsSuccess(AdjustmentTransactionType.N)
        case "O" => JsSuccess(AdjustmentTransactionType.O)
        case "P" => JsSuccess(AdjustmentTransactionType.P)
        case _   => JsError("Invalid AdjustmentTransactionType")
      }
    case _           => JsError("AdjustmentTransactionType: Unexpected JsString")
  }

  def writes(adjustmentType: AdjustmentTransactionType): JsValue = JsString(adjustmentType.toString.toUpperCase())
}
