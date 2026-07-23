/*
 * Copyright 2026 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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

  "PageBreadcrumbs.paymentsPage" should {

    "contain exactly three breadcrumb items" in {
      PageBreadcrumbs.paymentsPage.items.size shouldBe 3
    }

    "have the correct items in order" in {
      PageBreadcrumbs.paymentsPage.items shouldBe Seq(
        BreadcrumbsItem(content = Text("Corporation tax home"), href = Some("/")),
        BreadcrumbsItem(content = Text("Balance"), href = Some("/")),
        BreadcrumbsItem(content = Text("Accounting period overview"), href = Some("/"))
      )
    }

  }

  "PageBreadcrumbs.adjustmentsTransactionsAccountingPeriodPage" should {

    "contain exactly three breadcrumb items" in {
      PageBreadcrumbs.adjustmentsTransactionsAccountingPeriodPage.items.size shouldBe 3
    }

    "have the correct items in order" in {
      PageBreadcrumbs.paymentsPage.items shouldBe Seq(
        BreadcrumbsItem(content = Text("Corporation tax home"), href = Some("/")),
        BreadcrumbsItem(content = Text("Balance"), href = Some("/")),
        BreadcrumbsItem(content = Text("Accounting period overview"), href = Some("/"))
      )
    }

  }
}
