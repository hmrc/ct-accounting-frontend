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

package viewmodels.gpa

import play.twirl.api.HtmlFormat
import uk.gov.hmrc.govukfrontend.views.viewmodels.content.HtmlContent

case class LinkViewModel(text: String, href: String, classes: String = "govuk-link")
object LinkViewModel {

  def toHtmlContent(link: LinkViewModel): HtmlContent =
    HtmlContent(
      s"""<a href="${HtmlFormat.escape(link.href)}" class="${HtmlFormat.escape(link.classes)}">
         |${HtmlFormat.escape(link.text)}
         |</a>""".stripMargin
    )
}
