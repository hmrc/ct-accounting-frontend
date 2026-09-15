package helpers

import models.{InterestAccrualListWithInterestAccruedDays, InterestAccrualWithInterestAccruedDays}

import java.time.LocalDate

trait InterestAccrualListHelper {

  val emptyInterestAccrualList: InterestAccrualListWithInterestAccruedDays       =
    InterestAccrualListWithInterestAccruedDays(interestAccruals = List.empty)
  
  val interestAccrualMultipleObjects: InterestAccrualListWithInterestAccruedDays =
    InterestAccrualListWithInterestAccruedDays(
      interestAccruals = List(
        InterestAccrualWithInterestAccruedDays(
          computationAmount = BigDecimal("10000.00"),
          interestAccrualFromDate = LocalDate.of(2024, 4, 1),
          interestAccrualToDate = LocalDate.of(2024, 6, 30),
          interestRate = BigDecimal(7.75),
          interestAmount = BigDecimal("193.22"),
          apEndDate = LocalDate.of(2024, 3, 31),
          noOfDays = 91
        ),
        InterestAccrualWithInterestAccruedDays(
          computationAmount = BigDecimal("10000.00"),
          interestAccrualFromDate = LocalDate.of(2024, 7, 1),
          interestAccrualToDate = LocalDate.of(2024, 9, 30),
          interestRate = BigDecimal(8.25),
          interestAmount = BigDecimal("208.02"),
          apEndDate = LocalDate.of(2024, 3, 31),
          noOfDays = 92
        )
      )
    )
  
  val interestAccrualSingleObject: InterestAccrualListWithInterestAccruedDays    =
    InterestAccrualListWithInterestAccruedDays(
      interestAccruals = List(
        InterestAccrualWithInterestAccruedDays(
          computationAmount = BigDecimal("10000.00"),
          interestAccrualFromDate = LocalDate.of(2024, 4, 1),
          interestAccrualToDate = LocalDate.of(2024, 6, 30),
          interestRate = BigDecimal("7.75"),
          interestAmount = BigDecimal("193.22"),
          apEndDate = LocalDate.of(2024, 3, 31),
          noOfDays = 91
        )
      )
    )

}
