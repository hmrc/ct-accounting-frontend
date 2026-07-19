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

import uk.gov.hmrc.govukfrontend.views.viewmodels.breadcrumbs.{Breadcrumbs, BreadcrumbsItem}
import uk.gov.hmrc.govukfrontend.views.viewmodels.content.Text

object PageBreadcrumbs {

  def taxTransactionsPage: Breadcrumbs = Breadcrumbs(
    // TODO: Change these to be from message file (are these page titles?)
    // TODO: Add hrefs
    items = Seq(
      BreadcrumbsItem(content = Text("Corporation tax home"), href = Some("/")),
      BreadcrumbsItem(content = Text("Balance"), href = Some("/")),
      BreadcrumbsItem(content = Text("Accounting period overview"), href = Some("/"))
    )
  )

  def penaltiesAccountingPeriodPage: Breadcrumbs = Breadcrumbs(
    // TODO: Change these to be from message file (are these page titles?)
    // TODO: Add hrefs
    items = Seq(
      BreadcrumbsItem(content = Text("Corporation tax home"), href = Some("/")),
      BreadcrumbsItem(content = Text("Balance"), href = Some("/")),
      BreadcrumbsItem(content = Text("Accounting period overview"), href = Some("/"))
    )
  )

  def adjustmentsTransactionsAccountingPeriodPage: Breadcrumbs = Breadcrumbs(
    // TODO: Change these to be from message file (are these page titles?)
    // TODO: Add hrefs
    items = Seq(
      BreadcrumbsItem(content = Text("Corporation tax home"), href = Some("/")),
      BreadcrumbsItem(content = Text("Balance"), href = Some("/")),
      BreadcrumbsItem(content = Text("Accounting period overview"), href = Some("/"))
    )
  )

  def paymentsPage: Breadcrumbs = Breadcrumbs(
    // TODO: Change these to be from message file (are these page titles?)
    // TODO: Add hrefs
    items = Seq(
      BreadcrumbsItem(content = Text("Corporation tax home"), href = Some("/")),
      BreadcrumbsItem(content = Text("Balance"), href = Some("/")),
      BreadcrumbsItem(content = Text("Accounting period overview"), href = Some("/"))
    )
  )

}
