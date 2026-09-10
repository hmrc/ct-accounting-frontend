package models


import play.api.libs.json.{Json, OFormat}
import java.time.LocalDate

case class Repayments(repayments: List[RepaymentsDetails])

object Repayments {
  implicit val format: OFormat[Repayments] = Json.format[Repayments]
}

case class RepaymentsDetails(
                              amount: Option[BigDecimal],
                              repaymentType: String,
                              repaymentDate: LocalDate
                            )

object RepaymentsDetails {
  implicit val format: OFormat[RepaymentsDetails] = Json.format[RepaymentsDetails]
}