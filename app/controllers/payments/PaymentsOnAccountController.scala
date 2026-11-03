package controllers.payments

import controllers.actions.*
import controllers.Execution.trampoline
import controllers.payments.routes

import javax.inject.Inject
import play.api.i18n.Lang.logger
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import services.payments.PaymentsOnAccountService
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController
import viewmodels.payments.PaymentsOnAccountViewModel
import views.html.PaymentsOnAccountView
import utils.PaginationHelper.startIndexCountHelper

class PaymentsOnAccountController @Inject() (
                                              override val messagesApi: MessagesApi,
                                              identify: IdentifierAction,
                                              val controllerComponents: MessagesControllerComponents,
                                              view: PaymentsOnAccountView,
                                              service: PaymentsOnAccountService
                                            ) extends FrontendBaseController
  with I18nSupport {

  def onPageLoad(page: Option[Int]): Action[AnyContent] = identify.async { implicit request =>

    // TODO update to retrieve from session
    val taxRef = 5L
    val pCount = 10
    val pStartIndex = startIndexCountHelper(page, pCount)

    service
      .getPaymentsOnAccount(taxRef, 0, 10)
      .map { response =>
        val viewModel = PaymentsOnAccountViewModel.toViewModel(
          response,
          page,
          pageUrl = pageNumber => routes.PaymentsOnAccountController.onPageLoad(Some(pageNumber)).url
        )
        Ok(view(viewModel))
      }
      .recover { case ex =>
        logger.error(s"Unexpected failure while retrieving payments on account: ${ex.getMessage}")
        Redirect(routes.JourneyRecoveryController.onPageLoad())
      }
  }
}
