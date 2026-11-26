package controllers.actions

import base.SpecBase
import models.UserAnswers
import models.agentValidation.HasClient
import models.requests.DataRequest
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.{reset, times, verify, verifyNoInteractions, when}
import org.scalatestplus.mockito.MockitoSugar.mock
import pages.manageClient.UtrQuery
import play.api.http.Status.SEE_OTHER
import play.api.{Application, inject}
import play.api.inject.bind
import play.api.mvc.AnyContentAsEmpty
import play.api.test.FakeRequest
import play.api.test.Helpers.{defaultAwaitTimeout, redirectLocation, running, status}
import services.agentValidation.HasClientService
import uk.gov.hmrc.http.HeaderCarrier

import scala.concurrent.{ExecutionContext, Future}
import scala.concurrent.ExecutionContext.Implicits.global

class AgentClientAuthorisationGuardSpec extends SpecBase {

  private trait Fixture {
    val userAnswersWithUTR: UserAnswers = emptyUserAnswers.set(UtrQuery, "123456").success.value

    val mockHasClientService: HasClientService = mock[HasClientService]

    reset(mockHasClientService)

    val fakeDataRequestWithUTRInUserAnswers: DataRequest[AnyContentAsEmpty.type] =
      DataRequest(FakeRequest("GET", "/"), "id", userAnswersWithUTR)

    val fakeDataRequestWithEmptyUserAnswers: DataRequest[AnyContentAsEmpty.type] =
      DataRequest(FakeRequest("GET", "/"), "id", emptyUserAnswers)

  }

  "AgentClientAuthorisationGuard" - {
    "when utr has been supplied" - {
      "return None(Authorise the user) has client Service returns true" in new Fixture {
        when(mockHasClientService.getHasClient(any())(any[HeaderCarrier]))
          .thenReturn(Future.successful(HasClient(true)))
        val application: Application = applicationBuilder(Some(userAnswersWithUTR))
          .overrides(
            bind[HasClientService].toInstance(mockHasClientService)
          )
          .build()

        running(application) {
          val agentClientAuthorisationGuard = application.injector.instanceOf[AgentClientAuthorisationGuard]

          val result = agentClientAuthorisationGuard.filter(fakeDataRequestWithUTRInUserAnswers).futureValue

          result mustBe None
          verify(mockHasClientService, times(1)).getHasClient(any())(any[HeaderCarrier])
        }
      }

      "redirect to UnauthorisedController when hasClient Service returns false" in new Fixture {
        when(mockHasClientService.getHasClient(any())(any[HeaderCarrier]))
          .thenReturn(Future.successful(HasClient(false)))
        val application: Application = applicationBuilder(Some(userAnswersWithUTR))
          .overrides(
            bind[HasClientService].toInstance(mockHasClientService)
          )
          .build()

        running(application) {
          val agentClientAuthorisationGuard = application.injector.instanceOf[AgentClientAuthorisationGuard]

          val result = agentClientAuthorisationGuard.filter(fakeDataRequestWithUTRInUserAnswers).map(_.value)

          status(result) mustBe SEE_OTHER

          redirectLocation(result) mustBe Some(controllers.routes.UnauthorisedController.onPageLoad().url)

          verify(mockHasClientService, times(1)).getHasClient(any())(any[HeaderCarrier])
        }
      }

      "redirect to JourneyRecovery controller when there is exception from BE" in new Fixture {
        when(mockHasClientService.getHasClient(any())(any[HeaderCarrier]))
          .thenReturn(Future.failed(new RuntimeException("Failure from BE")))
        val application: Application = applicationBuilder(Some(userAnswersWithUTR))
          .overrides(
            bind[HasClientService].toInstance(mockHasClientService)
          )
          .build()

        running(application) {
          val agentClientAuthorisationGuard = application.injector.instanceOf[AgentClientAuthorisationGuard]

          val result = agentClientAuthorisationGuard.filter(fakeDataRequestWithUTRInUserAnswers).map(_.value)

          status(result) mustBe SEE_OTHER

          redirectLocation(result) mustBe Some(controllers.routes.JourneyRecoveryController.onPageLoad().url)

          verify(mockHasClientService, times(1)).getHasClient(any())(any[HeaderCarrier])
        }

      }

      "when utr has not been provided in UserAnswers" - {
        "should redirect to Unauthorised Controller" in new Fixture {
          when(mockHasClientService.getHasClient(any())(any[HeaderCarrier]))
            .thenReturn(Future.successful(HasClient(false)))
          val application: Application = applicationBuilder(Some(emptyUserAnswers))
            .overrides(
              bind[HasClientService].toInstance(mockHasClientService)
            )
            .build()

          running(application) {
            val agentClientAuthorisationGuard = application.injector.instanceOf[AgentClientAuthorisationGuard]

            val result = agentClientAuthorisationGuard.filter(fakeDataRequestWithEmptyUserAnswers).map(_.value)

            status(result) mustBe SEE_OTHER

            redirectLocation(result) mustBe Some(controllers.routes.UnauthorisedController.onPageLoad().url)

            verifyNoInteractions(mockHasClientService)
          }

        }
      }

    }

  }
}
