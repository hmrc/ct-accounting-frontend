package helpers

import models.{RepayReallocationSummary, RepayReallocationSummaryDetails}

import java.time.LocalDate

trait RepaymentsReallocationsHelper {
  val realloctionsFromSummary: RepayReallocationSummary = RepayReallocationSummary(
    transactions = List(
      RepayReallocationSummaryDetails(
        transactionDate = Some(LocalDate.of(2008, 10, 2)),
        `type` = Some("RFR"),
        amount = Some(BigDecimal("-56280")),
        accountingPeriodEndDate = Some(LocalDate.of(2003, 12, 20)),
        taxpayerReference = Some("8754000057")
      )
    )
  )

  val realloctionsToSummary: RepayReallocationSummary = RepayReallocationSummary(
    transactions = List(
      RepayReallocationSummaryDetails(
        transactionDate = Some(LocalDate.of(2007, 1, 5)),
        `type` = Some("RTO"),
        amount = Some(BigDecimal("56280")),
        accountingPeriodEndDate = Some(LocalDate.of(2003, 12, 31)),
        taxpayerReference = Some("8754000057")
      )
    )
  )

  val multipleSummaries: RepayReallocationSummary = RepayReallocationSummary(
    transactions = List(
      RepayReallocationSummaryDetails(
        transactionDate = Some(LocalDate.of(2008, 10, 2)),
        `type` = Some("RFR"),
        amount = Some(BigDecimal("-56280")),
        accountingPeriodEndDate = Some(LocalDate.of(2003, 12, 20)),
        taxpayerReference = Some("8754000057")
      ),
      RepayReallocationSummaryDetails(
        transactionDate = Some(LocalDate.of(2007, 1, 5)),
        `type` = Some("RTO"),
        amount = Some(BigDecimal("56280")),
        accountingPeriodEndDate = Some(LocalDate.of(2003, 12, 31)),
        taxpayerReference = Some("8754000057")
      )
    )
  )

}
