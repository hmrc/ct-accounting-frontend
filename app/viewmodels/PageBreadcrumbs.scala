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
import play.api.i18n.Messages

object PageBreadcrumbs {

  def taxTransactionsPage(implicit messages: Messages): Breadcrumbs = Breadcrumbs(
    // TODO: Add hrefs
    items = Seq(
      BreadcrumbsItem(content = Text(messages("breadcrumbs.home")), href = Some("/")),
      BreadcrumbsItem(content = Text(messages("breadcrumbs.accountingPeriods")), href = Some("/")),
      BreadcrumbsItem(content = Text(messages("breadcrumbs.accountingPeriodEnding")), href = Some("/"))
    )
  )

  def penaltiesAccountingPeriodPage(implicit messages: Messages): Breadcrumbs = Breadcrumbs(
    // TODO: Add hrefs
    items = Seq(
      BreadcrumbsItem(content = Text(messages("breadcrumbs.home")), href = Some("/")),
      BreadcrumbsItem(content = Text(messages("breadcrumbs.accountingPeriods")), href = Some("/")),
      BreadcrumbsItem(content = Text(messages("breadcrumbs.accountingPeriodEnding")), href = Some("/"))
    )
  )

  def adjustmentsTransactionsAccountingPeriodPage(implicit messages: Messages): Breadcrumbs = Breadcrumbs(
    // TODO: Add hrefs
    items = Seq(
      BreadcrumbsItem(content = Text(messages("breadcrumbs.home")), href = Some("/")),
      BreadcrumbsItem(content = Text(messages("breadcrumbs.accountingPeriods")), href = Some("/")),
      BreadcrumbsItem(content = Text(messages("breadcrumbs.accountingPeriodEnding")), href = Some("/"))
    )
  )
  def interestPage(implicit messages: Messages): Breadcrumbs                                = Breadcrumbs(
    // TODO: Add hrefs
    items = Seq(
      BreadcrumbsItem(content = Text(messages("breadcrumbs.home")), href = Some("/")),
      BreadcrumbsItem(content = Text(messages("breadcrumbs.accountingPeriods")), href = Some("/")),
      BreadcrumbsItem(content = Text(messages("breadcrumbs.accountingPeriodEnding")), href = Some("/"))
    )
  )

  def paymentsPage(implicit messages: Messages): Breadcrumbs = Breadcrumbs(
    // TODO: Add hrefs
    items = Seq(
      BreadcrumbsItem(content = Text(messages("breadcrumbs.home")), href = Some("/")),
      BreadcrumbsItem(content = Text(messages("breadcrumbs.accountingPeriods")), href = Some("/")),
      BreadcrumbsItem(content = Text(messages("breadcrumbs.accountingPeriodEnding")), href = Some("/"))
    )
  )
}
