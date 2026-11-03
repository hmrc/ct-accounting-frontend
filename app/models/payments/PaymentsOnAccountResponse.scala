package models.payments

import play.api.libs.json.{Json, OFormat}

import java.time.LocalDate

case class PaymentsOnAccountResponse(taxRef: String, totalNumberRows: Option[Int], paymentsOnAccountRows: Seq[PaymentsOnAccountRow])

object PaymentsOnAccountResponse {
  implicit val format: OFormat[PaymentsOnAccountResponse] = Json.format[PaymentsOnAccountResponse]
}

case class PaymentsOnAccountRow(
  paymentDate: LocalDate,
  paymentAllocationDate: LocalDate,
  description: String,
  amount: BigDecimal
)
