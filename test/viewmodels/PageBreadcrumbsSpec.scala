package viewmodels

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import uk.gov.hmrc.govukfrontend.views.viewmodels.breadcrumbs.BreadcrumbsItem
import uk.gov.hmrc.govukfrontend.views.viewmodels.content.Text

class PageBreadcrumbsSpec extends AnyWordSpec with Matchers {

  "PageBreadcrumbs.taxTransactionsPage" should {

    "contain exactly three breadcrumb items" in {
      PageBreadcrumbs.taxTransactionsPage.items.size shouldBe 3
    }

    "have the correct items in order" in {
      PageBreadcrumbs.taxTransactionsPage.items shouldBe Seq(
        BreadcrumbsItem(content = Text("Corporation tax home"), href = Some("/")),
        BreadcrumbsItem(content = Text("Balance"), href = Some("/")),
        BreadcrumbsItem(content = Text("Accounting period overview"), href = Some("/"))
      )
    }
    
  }
}