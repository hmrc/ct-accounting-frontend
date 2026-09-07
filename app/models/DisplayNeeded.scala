package models

import play.api.libs.json.{Json, OFormat}

case class DisplayNeeded(
  taxIsDisplayNeededFlag: Boolean,
  interestIsDisplayNeededFlag: Boolean,
  paymentIsDisplayNeededFlag: Boolean,
  repayReallocIsDisplayNeededFlag: Boolean
)

object DisplayNeeded {
  implicit val format: OFormat[DisplayNeeded] = Json.format[DisplayNeeded]
}
