package helpers

trait DebitInterestHelper {

  val defaultViewModel: DebitInterestViewModel = DebitInterestViewModel(
    interest = None,
    rows = List(
      DebitInterestRow(
        unpaidAmount = BigDecimal(17.01),
        fromDate = LocalDate.of(2026, 1, 1),
        toDate = LocalDate.of(2026, 1, 1),
        noOfDays = 1,
        rate = BigDecimal(0.75),
        interestAmount = BigDecimal(99.11)
      ),
      DebitInterestRow(
        unpaidAmount = BigDecimal(7.01),
        fromDate = LocalDate.of(2025, 2, 1),
        toDate = LocalDate.of(2025, 2, 1),
        noOfDays = 11,
        rate = BigDecimal(0.15),
        interestAmount = BigDecimal(278.13)
      ),
      DebitInterestRow(
        unpaidAmount = BigDecimal(87.01),
        fromDate = LocalDate.of(2015, 2, 1),
        toDate = LocalDate.of(2015, 2, 1),
        noOfDays = 89,
        rate = BigDecimal(14.5),
        interestAmount = BigDecimal(798.83)
      )
    )
  )

}
