package views.gpa


import base.SpecBase
import helpers.gpa.GroupPaymentArrangementHelper
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import play.api.i18n.{Lang, Messages, MessagesApi, MessagesImpl}
import play.api.test.FakeRequest
import viewmodels.gpa.GroupPaymentArrangementViewModel
import views.html.gpa.GroupPaymentArrangementView

class GroupPaymentArrangementViewSpec extends SpecBase with GroupPaymentArrangementHelper {
  val application = applicationBuilder().build()

  val view: GroupPaymentArrangementView = application.injector.instanceOf[GroupPaymentArrangementView]

  implicit val messagesApi: MessagesApi = application.injector.instanceOf[MessagesApi]
  implicit val messages: Messages       = MessagesImpl(Lang.defaultLang, messagesApi)

  implicit val request: FakeRequest[_] = FakeRequest()

  def render(viewModel: GroupPaymentArrangementViewModel): Document =
    Jsoup.parse(view(viewModel)(request, messages(application)).toString)

  "GroupPaymentArrangementView" - {

    "render the correct page title" in {
      val doc = render(groupPaymentArrangementViewModel)
      doc.title() mustBe "Group Payment Arrangement - GOV.UK"
    }

    "render the correct heading" in {
      val doc = render(groupPaymentArrangementViewModel)
      doc
        .select("h1.govuk-heading-l")
        .text() mustBe s"Group Payment Arrangement"
    }

    "render the first paragraph" in {
      val doc = render(groupPaymentArrangementViewModel)
      doc
        .getElementsByClass("govuk-body")
        .get(0)
        .text() mustBe s"It is an optional agreement with HMRC. Your company can participate only if it is a member of a group that meets HMRC’s conditions."
    }

    "render the second paragraph" in {
      val doc = render(groupPaymentArrangementViewModel)
      doc
        .getElementsByClass("govuk-body")
        .get(1)
        .text() mustBe s"Under a Group Payment Arrangement, your company can:"
    }

    "render the first bullet point" in {
      val doc = render(groupPaymentArrangementViewModel)
      doc
        .getElementsByClass("govuk-list govuk-list--bullet")
        .get(0)
        .text() contains  s"nominate one member of the group to make the payments on behalf of all the companies"
    }

    "render the second bullet point" in {
      val doc = render(groupPaymentArrangementViewModel)
      doc
        .getElementsByClass("govuk-list govuk-list--bullet")
        .get(0)
        .text() contains  s"reduce the administration when making many individual payments and might also reduce the group’s overall interest charges"
    }

    "render the correct table headers" in {
      val doc     = render(groupPaymentArrangementViewModel)
      val headers = doc.select("th.govuk-table__header").eachText()
      headers must contain allOf (
        messages("Period of account ending"),
        messages("Group payments"),
        messages("Group taxes"),
        messages("Status")
      )
      headers.size() mustBe 4
    }

    "render the correct breadcrumbs" in {
      val doc         = render(groupPaymentArrangementViewModel)
      val breadcrumbs = doc.select("li.govuk-breadcrumbs__list-item").eachText()

      breadcrumbs.get(0) mustBe messages("Home")

      doc.select(".govuk-breadcrumbs__list-item").size() mustBe 1
    }

    "render correct table with all content" in {
      val doc      = render(groupPaymentArrangementViewModel)
      val firstRow =
        doc.select("tbody.govuk-table__body tr.govuk-table__row").get(0).getElementsByClass("govuk-table__cell")
      firstRow.get(0).text() mustBe "01 Jan 2026"
      firstRow.get(1).text() mustBe "-£1,536,642.00"
      firstRow.get(2).text() mustBe "£1,536,642.00"
      firstRow.get(3).text() mustBe "Open"

      doc.select("tbody.govuk-table__body tr.govuk-table__row").size() mustBe 1

    }

    "render insetText" in {
      val doc = render(groupPaymentArrangementViewModel)
      doc
        .getElementsByClass("govuk-inset-text")
        .get(0)
        .text() mustBe s"Any changes to the Group Payment Arrangement that have not yet been approved and processed by HMRC will not be shown."
    }

  }
}
