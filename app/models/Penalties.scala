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

import play.api.libs.json.{Json, OFormat, Reads, Writes}

import java.time.LocalDate

case class PenaltiesResponse(penaltyTransactions: Seq[PenaltyTransaction])

object PenaltiesResponse {
  implicit val reads: Reads[PenaltiesResponse] = Json.reads[PenaltiesResponse]
  implicit val writes: Writes[PenaltiesResponse] = Json.writes[PenaltiesResponse]

}

case class PenaltyTransaction(
                               penaltyDate: LocalDate,
                               `type`: String,
                               postingAmount: BigDecimal
                             )

object PenaltyTransaction {
  implicit val format: OFormat[PenaltyTransaction] = Json.format[PenaltyTransaction]
}
