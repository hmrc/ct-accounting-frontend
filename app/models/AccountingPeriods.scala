package models

import play.api.libs.json.{Json, OFormat}

import java.time.LocalDate

case class AccountingPeriods(accountingPeriods: List[AccountingPeriodsRowResponse])

object AccountingPeriods {
  implicit val format: OFormat[AccountingPeriods] = Json.format[AccountingPeriods]
}

case class AccountingPeriodsRowResponse(
                                         accountingPeriod: BigDecimal,
                                         apStartDate: LocalDate,
                                         apEndDate: LocalDate,
                                         apStatus: String,
                                         taxChargePresent: Boolean,
                                         clericalIntSig: Boolean,
                                         creditDebitInterestInd: Boolean,
                                         taxTotal: BigDecimal,
                                         interestTotal: BigDecimal,
                                         penaltyTotal: BigDecimal,
                                         payslipTotal: BigDecimal,
                                         repayReallocTotal: BigDecimal,
                                         adjustmentTotal: BigDecimal
                                       )

object AccountingPeriodsRowResponse {
  implicit val format: OFormat[AccountingPeriodsRowResponse] = Json.format[AccountingPeriodsRowResponse]
}
