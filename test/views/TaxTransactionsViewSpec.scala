package views

import base.SpecBase
import models.TaxTransactionsItem
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import play.api.test.FakeRequest
import views.html.TaxTransactionsView

import java.time.LocalDate

class TaxTransactionsViewSpec extends SpecBase {
  val application = applicationBuilder().build()

  val view: TaxTransactionsView = application.injector.instanceOf[TaxTransactionsView]

  implicit val request: FakeRequest[_] = FakeRequest()

  val accountPeriod: LocalDate = LocalDate.of(2026, 1, 1)

  val taxTransactions: List[TaxTransactionsItem] = List(
    TaxTransactionsItem(
      currentAmount = 1234.56,
      assessmentType = "A",
      taxDate = LocalDate.of(2026, 1, 15),
      correctionClaimSignal = None
    )
  )

  def render(items: List[TaxTransactionsItem] = taxTransactions): Document =
    Jsoup.parse(view(items, accountPeriod)(request, messages(application)).toString)

  "TaxTransactionsView" - {

    "render the correct page title" in {
      val doc = render()
      doc.title() mustBe ("Taxes - Accounting period overview - GOV.UK")
    }

    "render the correct heading" in {
      val doc = render()
      doc.select("h1.govuk-heading-xl").text() mustBe "Taxes"
    }

    "render the table caption with the formatted account period" in {
      val doc = render()
      doc.select(".govuk-table__caption").text() must include("Accounting Period ending")
    }

    "render the correct table headers" in {
      val doc = render()
      val headers = doc.select("th.govuk-table__header").eachText()
      headers must contain allOf ("Date", "Description", "Amount")
    }

//    "render a table row for each transaction with formatted date and amount" in {
//      val doc = render()
//      val firstRow = doc.select("tbody.govuk-table__body tr.govuk-table__row").first()
//      val cells = firstRow.select("td, th").eachText()
//
//      cells must contain(ViewUtils.formatDate(taxTransactions.head.taxDate, messages(app).lang))
//      cells must contain(ViewUtils.formatCurrency(taxTransactions.head.currentAmount))
//    }

    "render one row per transaction when there are multiple" in {
      val twoTransactions = taxTransactions :+ TaxTransactionsItem(
        currentAmount = 99.99,
        assessmentType = "A",
        taxDate = LocalDate.of(2026, 2, 1),
        correctionClaimSignal = Some("2")
      )
      val doc = render(items = twoTransactions)
      doc.select("tbody.govuk-table__body tr.govuk-table__row").size() mustBe 2
    }

    "render no data rows when there are no transactions" in {
      val doc = render(items = List.empty)
      doc.select("tbody.govuk-table__body tr.govuk-table__row").size() mustBe 0
    }

    "render the breadcrumbs" in {
      val doc = render()
      doc.select(".govuk-breadcrumbs__list-item").size() must be > 0
    }
  }
}