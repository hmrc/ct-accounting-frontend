package models

import play.api.libs.json.{Json, OFormat}

import java.time.LocalDate

case class Reallocations(reallocation: List[ReallocationRow])
case class ReallocationRow(
                            amount: BigDecimal,
                            reallocationDate: LocalDate,
                            sourceApEndDate: Option[LocalDate],
                            sourceTaxpayerReference: String
                          )

object Reallocations {
  implicit val format: OFormat[Reallocations] = Json.format[Reallocations]
}

object ReallocationRow {
  implicit val format: OFormat[ReallocationRow] = Json.format[ReallocationRow]
}

case class ReallocationToAccPeriod(reallocation: List[ReallocationToAccPeriodRow])

object ReallocationToAccPeriod {
  implicit val format: OFormat[ReallocationToAccPeriod] = Json.format[ReallocationToAccPeriod]
}

case class ReallocationToAccPeriodRow(
                                       amount: BigDecimal,
                                       reallocationDate: LocalDate,
                                       sourceApEndDate: Option[LocalDate],
                                       sourceTaxpayerReference: String,
                                       transactionType: String
                                     )

object ReallocationToAccPeriodRow {
  implicit val format: OFormat[ReallocationToAccPeriodRow] = Json.format[ReallocationToAccPeriodRow]
}
