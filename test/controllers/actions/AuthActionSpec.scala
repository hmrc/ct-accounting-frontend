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

package controllers.actions

import base.SpecBase
import com.google.inject.Inject
import config.FrontendAppConfig
import connectors.agentValidation.ClientListDownloadStatusConnector
import controllers.actions.TestAuthRetrievals.~
import controllers.routes
import models.agentValidation.ClientListDownloadStatusTransformed
import models.agentValidation.ClientListDownloadStatusTransformed.{Failed, InProgress, InitiateDownload, Succeeded}
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.{times, verify, verifyNoInteractions, when}
import org.scalatestplus.mockito.MockitoSugar.mock
import play.api.mvc.{Action, AnyContent, BodyParsers, Results}
import play.api.test.FakeRequest
import play.api.test.Helpers.*
import play.api.{Application, inject}
import services.agentValidation.ClientListDownloadStatusService
import uk.gov.hmrc.auth.core.*
import uk.gov.hmrc.auth.core.AffinityGroup.{Agent, Individual, Organisation}
import uk.gov.hmrc.auth.core.authorise.Predicate
import uk.gov.hmrc.auth.core.retrieve.{Retrieval, ~}
import uk.gov.hmrc.http.HeaderCarrier

import java.util.UUID
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{ExecutionContext, Future}

class AuthActionSpec extends SpecBase {

  private trait Fixture {
    val activatedState: String  = "activated"
    val inActivateState: String = "inactive"
    val id: String              = UUID.randomUUID().toString

    implicit val hc: HeaderCarrier = HeaderCarrier()

    def createEnrolment(
      enrolmentKey: String,
      enrolmentIdentifierKey: String,
      enrolmentIdentifierValue: String,
      inputState: String
    ): Enrolments =
      Enrolments(
        Set(
          Enrolment(
            key = enrolmentKey,
            identifiers = Seq(
              EnrolmentIdentifier(
                key = enrolmentIdentifierKey,
                value = enrolmentIdentifierValue
              )
            ),
            state = inputState
          )
        )
      )

    val mockAuthConnector: AuthConnector = mock[AuthConnector]

    type RetrievalsType = Option[String] ~ Enrolments ~ Option[AffinityGroup] ~ Option[CredentialRole]

    val application: Application = applicationBuilder(userAnswers = None)
      .overrides(inject.bind[AuthConnector].toInstance(mockAuthConnector))
      .build()

    val bodyParsers: BodyParsers.Default = application.injector.instanceOf[BodyParsers.Default]
    val appConfig: FrontendAppConfig     = application.injector.instanceOf[FrontendAppConfig]
  }

  class Harness(authAction: IdentifierAction) {
    def onPageLoad(): Action[AnyContent] = authAction(_ => Results.Ok)
  }

  "Auth Action" - {

    "when the user hasn't logged in" - {

      "must redirect the user to log in " in {

        val application = applicationBuilder(userAnswers = None).build()

        val mockService: FakeUnusedClientStatusService = mock[FakeUnusedClientStatusService]

        running(application) {
          val bodyParsers = application.injector.instanceOf[BodyParsers.Default]
          val appConfig   = application.injector.instanceOf[FrontendAppConfig]

          val authAction = new AuthenticatedIdentifierAction(
            new FakeFailingAuthConnector(new MissingBearerToken),
            mockService,
            appConfig,
            bodyParsers
          )
          val controller = new Harness(authAction)
          val result     = controller.onPageLoad()(FakeRequest())

          status(result) mustBe SEE_OTHER
          redirectLocation(result).value must startWith(appConfig.loginUrl)

          verifyNoInteractions(mockService)
        }
      }
    }

    "when the user tries to log in as an Organisation" - {
      "must allow into the service with correct and active Enrolment, enrolment key = IR-CT and identifier key = UTR, identifier value matching the Regex([0-9]{10}$)" in new Fixture {

        val mockService: FakeUnusedClientStatusService = mock[FakeUnusedClientStatusService]
        val validOrgEnrolment: Enrolments              = createEnrolment("IR-CT", "UTR", "1234567890", activatedState)
        when(mockAuthConnector.authorise[RetrievalsType](any(), any())(any(), any())).thenReturn(
          Future.successful(Some(id) ~ validOrgEnrolment ~ Some(Organisation) ~ Some(User))
        )
        running(application) {

          val authAction = new AuthenticatedIdentifierAction(mockAuthConnector, mockService, appConfig, bodyParsers)
          val controller = new Harness(authAction)
          val result     = controller.onPageLoad()(FakeRequest())

          status(result) mustBe OK

          verifyNoInteractions(mockService)

        }
      }
      "must redirect to UnauthorisedController with inactive Enrolment, enrolment key = IR-CT and identifier key = UTR, identifier value matching the Regex([0-9]{10}$)" in new Fixture {
        val invalidOrgEnrolment: Enrolments = createEnrolment("IR-CT", "UTR", "1234567890", inActivateState)

        val mockService: FakeUnusedClientStatusService = mock[FakeUnusedClientStatusService]
        when(mockAuthConnector.authorise[RetrievalsType](any(), any())(any(), any())).thenReturn(
          Future.successful(Some(id) ~ invalidOrgEnrolment ~ Some(Organisation) ~ Some(User))
        )
        running(application) {

          val authAction = new AuthenticatedIdentifierAction(mockAuthConnector, mockService, appConfig, bodyParsers)
          val controller = new Harness(authAction)
          val result     = controller.onPageLoad()(FakeRequest())

          status(result) mustBe SEE_OTHER

          redirectLocation(result) mustBe Some(routes.UnauthorisedController.onPageLoad().url)

          verifyNoInteractions(mockService)

        }
      }
      "must redirect to UnauthorisedController with active Enrolment, with incorrect enrolment key and identifier key = UTR, identifier value matching the Regex([0-9]{10}$)" in new Fixture {
        val invalidOrgEnrolment: Enrolments            = createEnrolment("IR-CT-AGENT", "UTR", "1234567890", activatedState)
        val mockService: FakeUnusedClientStatusService = mock[FakeUnusedClientStatusService]
        when(mockAuthConnector.authorise[RetrievalsType](any(), any())(any(), any())).thenReturn(
          Future.successful(Some(id) ~ invalidOrgEnrolment ~ Some(Organisation) ~ Some(User))
        )
        running(application) {

          val authAction = new AuthenticatedIdentifierAction(mockAuthConnector, mockService, appConfig, bodyParsers)
          val controller = new Harness(authAction)
          val result     = controller.onPageLoad()(FakeRequest())

          status(result) mustBe SEE_OTHER

          redirectLocation(result) mustBe Some(routes.UnauthorisedController.onPageLoad().url)

          verifyNoInteractions(mockService)
        }
      }
      "must redirect to UnauthorisedController with active Enrolment, enrolment key = IR-CT, incorrect identifier key and identifier value matching the Regex([0-9]{10}$)" in new Fixture {
        val invalidOrgEnrolment: Enrolments            = createEnrolment("IR-CT", "UTRNO", "1234567890", activatedState)
        val mockService: FakeUnusedClientStatusService = mock[FakeUnusedClientStatusService]
        when(mockAuthConnector.authorise[RetrievalsType](any(), any())(any(), any())).thenReturn(
          Future.successful(Some(id) ~ invalidOrgEnrolment ~ Some(Organisation) ~ Some(User))
        )
        running(application) {

          val authAction = new AuthenticatedIdentifierAction(mockAuthConnector, mockService, appConfig, bodyParsers)
          val controller = new Harness(authAction)
          val result     = controller.onPageLoad()(FakeRequest())

          status(result) mustBe SEE_OTHER

          redirectLocation(result) mustBe Some(routes.UnauthorisedController.onPageLoad().url)

          verifyNoInteractions(mockService)
        }
      }
      "must redirect to UnauthorisedController with active Enrolment, correct identifier key and identifier value not-matching the Regex([0-9]{10}$)" in new Fixture {
        val invalidOrgEnrolment: Enrolments            = createEnrolment("IR-CT", "UTR", "1234567", activatedState)
        val mockService: FakeUnusedClientStatusService = mock[FakeUnusedClientStatusService]
        when(mockAuthConnector.authorise[RetrievalsType](any(), any())(any(), any())).thenReturn(
          Future.successful(Some(id) ~ invalidOrgEnrolment ~ Some(Organisation) ~ Some(User))
        )
        running(application) {

          val authAction = new AuthenticatedIdentifierAction(mockAuthConnector, mockService, appConfig, bodyParsers)
          val controller = new Harness(authAction)
          val result     = controller.onPageLoad()(FakeRequest())

          status(result) mustBe SEE_OTHER

          redirectLocation(result) mustBe Some(routes.UnauthorisedController.onPageLoad().url)

          verifyNoInteractions(mockService)
        }
      }
      "must redirect to UnauthorisedController with incorrect and inactive Enrolments, incorrect enrolment key, incorrect identifier key and identifier value not-matching the Regex([0-9]{10}$)" in new Fixture {
        val invalidOrgEnrolment: Enrolments            = createEnrolment("IR-CT-AGENT", "UTRN", "1234567890", activatedState)
        val mockService: FakeUnusedClientStatusService = mock[FakeUnusedClientStatusService]
        when(mockAuthConnector.authorise[RetrievalsType](any(), any())(any(), any())).thenReturn(
          Future.successful(Some(id) ~ invalidOrgEnrolment ~ Some(Organisation) ~ Some(User))
        )
        running(application) {

          val authAction = new AuthenticatedIdentifierAction(mockAuthConnector, mockService, appConfig, bodyParsers)
          val controller = new Harness(authAction)
          val result     = controller.onPageLoad()(FakeRequest())

          status(result) mustBe SEE_OTHER

          redirectLocation(result) mustBe Some(routes.UnauthorisedController.onPageLoad().url)

          verifyNoInteractions(mockService)
        }
      }

    }
    "when the user tries to log in as an Agent" - {
      "must allow into the service with correct and active Enrolment, enrolment key = IR-CT-AGENT and identifier key = IRAgentReference, identifier value matching the Regex(^[A-Za-z0-9]{6}$)" in new Fixture {
        val validAgentEnrolment: Enrolments =
          createEnrolment("IR-CT-AGENT", "IRAgentReference", "N12A36", activatedState)

        val mockService: ClientListDownloadStatusService = mock[ClientListDownloadStatusService]

        when(mockService.getClientListDownloadStatus(any[HeaderCarrier], any[ExecutionContext]))
          .thenReturn(Future.successful(Succeeded))

        when(mockAuthConnector.authorise[RetrievalsType](any(), any())(any(), any())).thenReturn(
          Future.successful(Some(id) ~ validAgentEnrolment ~ Some(Agent) ~ Some(User))
        )
        running(application) {

          val authAction = new AuthenticatedIdentifierAction(mockAuthConnector, mockService, appConfig, bodyParsers)
          val controller = new Harness(authAction)
          val result     = controller.onPageLoad()(FakeRequest())

          status(result) mustBe OK

        }
      }

      "must redirect to UnauthorisedController with inactive Enrolment, enrolment key = IR-CT-AGENT and identifier key = IRAgentReference, identifier value matching the Regex(^[A-Za-z0-9]{6}$)" in new Fixture {
        val invalidAgentEnrolment: Enrolments =
          createEnrolment("IR-CT-AGENT", "IRAgentReference", "N12A36", inActivateState)

        val mockService: ClientListDownloadStatusService = mock[ClientListDownloadStatusService]

        when(mockService.getClientListDownloadStatus).thenReturn(Future.successful(Succeeded))

        when(mockAuthConnector.authorise[RetrievalsType](any(), any())(any(), any())).thenReturn(
          Future.successful(Some(id) ~ invalidAgentEnrolment ~ Some(Agent) ~ Some(User))
        )
        running(application) {

          val authAction = new AuthenticatedIdentifierAction(mockAuthConnector, mockService, appConfig, bodyParsers)
          val controller = new Harness(authAction)
          val result     = controller.onPageLoad()(FakeRequest())

          status(result) mustBe SEE_OTHER

          redirectLocation(result) mustBe Some(routes.UnauthorisedController.onPageLoad().url)

          verifyNoInteractions(mockService)
        }
      }

      "must redirect to UnauthorisedController with active Enrolment, with incorrect enrolment key and identifier key = IRAgentReference, identifier value matching the Regex(^[A-Za-z0-9]{6}$)" in new Fixture {
        val invalidAgentEnrolment: Enrolments = createEnrolment("IR", "IRAgentReference", "N12A36", activatedState)

        val mockService: ClientListDownloadStatusService = mock[ClientListDownloadStatusService]

        when(mockService.getClientListDownloadStatus).thenReturn(Future.successful(InitiateDownload))

        when(mockAuthConnector.authorise[RetrievalsType](any(), any())(any(), any())).thenReturn(
          Future.successful(Some(id) ~ invalidAgentEnrolment ~ Some(Agent) ~ Some(User))
        )
        running(application) {

          val authAction = new AuthenticatedIdentifierAction(mockAuthConnector, mockService, appConfig, bodyParsers)
          val controller = new Harness(authAction)
          val result     = controller.onPageLoad()(FakeRequest())

          status(result) mustBe SEE_OTHER

          redirectLocation(result) mustBe Some(routes.UnauthorisedController.onPageLoad().url)
          verifyNoInteractions(mockService)
        }
      }

      "must redirect to UnauthorisedController with active Enrolment, enrolment key = IR-CT-AGENT, incorrect identifier key and identifier value matching the Regex(^[A-Za-z0-9]{6}$)" in new Fixture {
        val invalidAgentEnrolment: Enrolments = createEnrolment("IR-CT-AGENT", "Agent", "N12A36", activatedState)

        val mockService: ClientListDownloadStatusService = mock[ClientListDownloadStatusService]

        when(mockService.getClientListDownloadStatus).thenReturn(Future.successful(InitiateDownload))

        when(mockAuthConnector.authorise[RetrievalsType](any(), any())(any(), any())).thenReturn(
          Future.successful(Some(id) ~ invalidAgentEnrolment ~ Some(Agent) ~ Some(User))
        )
        running(application) {

          val authAction = new AuthenticatedIdentifierAction(mockAuthConnector, mockService, appConfig, bodyParsers)
          val controller = new Harness(authAction)
          val result     = controller.onPageLoad()(FakeRequest())

          status(result) mustBe SEE_OTHER

          redirectLocation(result) mustBe Some(routes.UnauthorisedController.onPageLoad().url)
          verifyNoInteractions(mockService)
        }
      }

      "must redirect to UnauthorisedController with active Enrolment, correct identifier key and identifier value not-matching the Regex([0-9]{10}$)" in new Fixture {
        val invalidAgentEnrolment: Enrolments =
          createEnrolment("IR-CT-AGENT", "IRAgentReference", "11241", activatedState)

        val mockService: ClientListDownloadStatusService = mock[ClientListDownloadStatusService]

        when(mockService.getClientListDownloadStatus).thenReturn(Future.successful(InitiateDownload))

        when(mockAuthConnector.authorise[RetrievalsType](any(), any())(any(), any())).thenReturn(
          Future.successful(Some(id) ~ invalidAgentEnrolment ~ Some(Agent) ~ Some(User))
        )
        running(application) {

          val authAction = new AuthenticatedIdentifierAction(mockAuthConnector, mockService, appConfig, bodyParsers)
          val controller = new Harness(authAction)
          val result     = controller.onPageLoad()(FakeRequest())

          status(result) mustBe SEE_OTHER

          redirectLocation(result) mustBe Some(routes.UnauthorisedController.onPageLoad().url)
          verifyNoInteractions(mockService)
        }
      }

      "must redirect to UnauthorisedController with incorrect and inactive Enrolments, incorrect enrolment key, incorrect identifier key and identifier value not-matching the Regex(^[A-Za-z0-9]{6}$)" in new Fixture {
        val invalidAgentEnrolment: Enrolments = createEnrolment("IR-CT", "NINO", "11241", activatedState)

        val mockService: ClientListDownloadStatusService = mock[ClientListDownloadStatusService]

        when(mockService.getClientListDownloadStatus).thenReturn(Future.successful(InitiateDownload))

        when(mockAuthConnector.authorise[RetrievalsType](any(), any())(any(), any())).thenReturn(
          Future.successful(Some(id) ~ invalidAgentEnrolment ~ Some(Agent) ~ Some(User))
        )
        running(application) {

          val authAction = new AuthenticatedIdentifierAction(mockAuthConnector, mockService, appConfig, bodyParsers)
          val controller = new Harness(authAction)
          val result     = controller.onPageLoad()(FakeRequest())

          status(result) mustBe SEE_OTHER

          redirectLocation(result) mustBe Some(routes.UnauthorisedController.onPageLoad().url)

          verifyNoInteractions(mockService)
        }
      }

      "must redirect to InProgress Page when ClientListDownloadStatus returns InProgress after enrolments has been verified for the user" in new Fixture {
        val validAgentEnrolment: Enrolments =
          createEnrolment("IR-CT-AGENT", "IRAgentReference", "N12A36", activatedState)

        val mockService: ClientListDownloadStatusService = mock[ClientListDownloadStatusService]

        when(mockService.getClientListDownloadStatus(any[HeaderCarrier], any[ExecutionContext]))
          .thenReturn(Future.successful(InProgress))

        when(mockAuthConnector.authorise[RetrievalsType](any(), any())(any(), any())).thenReturn(
          Future.successful(Some(id) ~ validAgentEnrolment ~ Some(Agent) ~ Some(User))
        )
        running(application) {

          val authAction = new AuthenticatedIdentifierAction(mockAuthConnector, mockService, appConfig, bodyParsers)
          val controller = new Harness(authAction)
          val result     = controller.onPageLoad()(FakeRequest())

          status(result) mustBe SEE_OTHER

          redirectLocation(result) mustBe Some(controllers.routes.UnauthorisedController.onPageLoad().url)

          verify(mockService, times(1)).getClientListDownloadStatus(any(), any())
        }
      }

      "must block the request and redirect to UnauthorisedController when ClientListDownloadStatus returns Failed after enrolments has been verified for the user" in new Fixture {
        val validAgentEnrolment: Enrolments =
          createEnrolment("IR-CT-AGENT", "IRAgentReference", "N12A36", activatedState)

        val mockService: ClientListDownloadStatusService = mock[ClientListDownloadStatusService]

        when(mockService.getClientListDownloadStatus(any[HeaderCarrier], any[ExecutionContext]))
          .thenReturn(Future.successful(Failed))

        when(mockAuthConnector.authorise[RetrievalsType](any(), any())(any(), any())).thenReturn(
          Future.successful(Some(id) ~ validAgentEnrolment ~ Some(Agent) ~ Some(User))
        )
        running(application) {

          val authAction = new AuthenticatedIdentifierAction(mockAuthConnector, mockService, appConfig, bodyParsers)
          val controller = new Harness(authAction)
          val result     = controller.onPageLoad()(FakeRequest())

          status(result) mustBe SEE_OTHER

          redirectLocation(result) mustBe Some(controllers.routes.UnauthorisedController.onPageLoad().url)

          verify(mockService, times(1)).getClientListDownloadStatus(any(), any())
        }
      }

      "must Authorised when ClientListDownloadStatus returns Succeeded after enrolments has been verified for the user" in new Fixture {
        val validAgentEnrolment: Enrolments =
          createEnrolment("IR-CT-AGENT", "IRAgentReference", "N12A36", activatedState)

        val mockService: ClientListDownloadStatusService = mock[ClientListDownloadStatusService]

        when(mockService.getClientListDownloadStatus(any[HeaderCarrier], any[ExecutionContext]))
          .thenReturn(Future.successful(Succeeded))

        when(mockAuthConnector.authorise[RetrievalsType](any(), any())(any(), any())).thenReturn(
          Future.successful(Some(id) ~ validAgentEnrolment ~ Some(Agent) ~ Some(User))
        )
        running(application) {

          val authAction = new AuthenticatedIdentifierAction(mockAuthConnector, mockService, appConfig, bodyParsers)
          val controller = new Harness(authAction)
          val result     = controller.onPageLoad()(FakeRequest())

          status(result) mustBe OK

          verify(mockService, times(1)).getClientListDownloadStatus(any(), any())
        }
      }

      "must Authorise and redirect to InProgress Page when ClientListDownloadStatus returns InitiateDownload after enrolments has been verified for the user" in new Fixture {
        val validAgentEnrolment: Enrolments =
          createEnrolment("IR-CT-AGENT", "IRAgentReference", "N12A36", activatedState)

        val mockService: ClientListDownloadStatusService = mock[ClientListDownloadStatusService]

        when(mockService.getClientListDownloadStatus(any[HeaderCarrier], any[ExecutionContext]))
          .thenReturn(Future.successful(InProgress))

        when(mockAuthConnector.authorise[RetrievalsType](any(), any())(any(), any())).thenReturn(
          Future.successful(Some(id) ~ validAgentEnrolment ~ Some(Agent) ~ Some(User))
        )
        running(application) {

          val authAction = new AuthenticatedIdentifierAction(mockAuthConnector, mockService, appConfig, bodyParsers)
          val controller = new Harness(authAction)
          val result     = controller.onPageLoad()(FakeRequest())

          status(result) mustBe SEE_OTHER

          redirectLocation(result) mustBe Some(controllers.routes.UnauthorisedController.onPageLoad().url)

          verify(mockService, times(1)).getClientListDownloadStatus(any(), any())
        }
      }

      "must redirect to JourneyRecoveryController Page when ClientListDownloadStatus returns Exception" in new Fixture {
        val validAgentEnrolment: Enrolments =
          createEnrolment("IR-CT-AGENT", "IRAgentReference", "N12A36", activatedState)

        val mockService: ClientListDownloadStatusService = mock[ClientListDownloadStatusService]

        when(mockService.getClientListDownloadStatus(any[HeaderCarrier], any[ExecutionContext]))
          .thenReturn(Future.failed(new RuntimeException("Exception from Downstream services")))

        when(mockAuthConnector.authorise[RetrievalsType](any(), any())(any(), any())).thenReturn(
          Future.successful(Some(id) ~ validAgentEnrolment ~ Some(Agent) ~ Some(User))
        )
        running(application) {

          val authAction = new AuthenticatedIdentifierAction(mockAuthConnector, mockService, appConfig, bodyParsers)
          val controller = new Harness(authAction)
          val result     = controller.onPageLoad()(FakeRequest())

          status(result) mustBe SEE_OTHER

          redirectLocation(result) mustBe Some(controllers.routes.JourneyRecoveryController.onPageLoad().url)

          verify(mockService, times(1)).getClientListDownloadStatus(any(), any())
        }
      }

    }
    "when the user tries to login in as an Individual" - {
      "must redirect to UnauthorisedController" in new Fixture {
        val validEnrolment: Enrolments                 = createEnrolment("IR-CT-AGENT", "IRAgentReference", "N12A36", activatedState)
        val mockService: FakeUnusedClientStatusService = mock[FakeUnusedClientStatusService]
        when(mockAuthConnector.authorise[RetrievalsType](any(), any())(any(), any())).thenReturn(
          Future.successful(Some(id) ~ validEnrolment ~ Some(Individual) ~ Some(User))
        )
        running(application) {

          val authAction = new AuthenticatedIdentifierAction(mockAuthConnector, mockService, appConfig, bodyParsers)
          val controller = new Harness(authAction)
          val result     = controller.onPageLoad()(FakeRequest())

          status(result) mustBe SEE_OTHER

          redirectLocation(result) mustBe Some(routes.UnauthorisedController.onPageLoad().url)

          verifyNoInteractions(mockService)
        }

      }

    }
    "when the user tries to login in as an Assistant for Organisation or Individual" - {
      "must redirect to UnauthorisedController" in new Fixture {
        val validEnrolment: Enrolments = createEnrolment("IR-CT", "UTR", "1234567890", activatedState)

        val mockService: FakeUnusedClientStatusService = mock[FakeUnusedClientStatusService]

        when(mockAuthConnector.authorise[RetrievalsType](any(), any())(any(), any())).thenReturn(
          Future.successful(Some(id) ~ validEnrolment ~ Some(Organisation) ~ Some(Assistant))
        )
        running(application) {

          val authAction = new AuthenticatedIdentifierAction(mockAuthConnector, mockService, appConfig, bodyParsers)
          val controller = new Harness(authAction)
          val result     = controller.onPageLoad()(FakeRequest())

          status(result) mustBe SEE_OTHER

          redirectLocation(result) mustBe Some(routes.UnauthorisedController.onPageLoad().url)

          verifyNoInteractions(mockService)
        }
      }
    }

    "when not any Enrolment, identifier key and values are not provided" - {
      "must redirect to UnauthorisedController" in new Fixture {
        val emptyEnrolment: Enrolments = Enrolments(Set.empty)

        val mockService: FakeUnusedClientStatusService = mock[FakeUnusedClientStatusService]

        when(mockAuthConnector.authorise[RetrievalsType](any(), any())(any(), any())).thenReturn(
          Future.successful(Some(id) ~ emptyEnrolment ~ Some(Organisation) ~ Some(Assistant))
        )
        running(application) {

          val authAction = new AuthenticatedIdentifierAction(mockAuthConnector, mockService, appConfig, bodyParsers)
          val controller = new Harness(authAction)
          val result     = controller.onPageLoad()(FakeRequest())

          status(result) mustBe SEE_OTHER

          redirectLocation(result) mustBe Some(routes.UnauthorisedController.onPageLoad().url)

          verifyNoInteractions(mockService)
        }
      }
    }

    "the user's session has expired" - {

      "must redirect the user to log in " in {

        val application = applicationBuilder(userAnswers = None).build()

        val mockService: FakeUnusedClientStatusService = mock[FakeUnusedClientStatusService]

        running(application) {
          val bodyParsers = application.injector.instanceOf[BodyParsers.Default]
          val appConfig   = application.injector.instanceOf[FrontendAppConfig]

          val authAction = new AuthenticatedIdentifierAction(
            new FakeFailingAuthConnector(new BearerTokenExpired),
            mockService,
            appConfig,
            bodyParsers
          )
          val controller = new Harness(authAction)
          val result     = controller.onPageLoad()(FakeRequest())

          status(result) mustBe SEE_OTHER
          redirectLocation(result).value must startWith(appConfig.loginUrl)

          verifyNoInteractions(mockService)
        }
      }
    }

    "the user doesn't have sufficient enrolments" - {

      "must redirect the user to the unauthorised page" in {

        val application = applicationBuilder(userAnswers = None).build()

        val mockService: FakeUnusedClientStatusService = mock[FakeUnusedClientStatusService]

        running(application) {
          val bodyParsers = application.injector.instanceOf[BodyParsers.Default]
          val appConfig   = application.injector.instanceOf[FrontendAppConfig]

          val authAction = new AuthenticatedIdentifierAction(
            new FakeFailingAuthConnector(new InsufficientEnrolments),
            mockService,
            appConfig,
            bodyParsers
          )
          val controller = new Harness(authAction)
          val result     = controller.onPageLoad()(FakeRequest())

          status(result) mustBe SEE_OTHER
          redirectLocation(result).value mustBe routes.UnauthorisedController.onPageLoad().url

          verifyNoInteractions(mockService)
        }
      }
    }

    "the user doesn't have sufficient confidence level" - {

      "must redirect the user to the unauthorised page" in {

        val application                                = applicationBuilder(userAnswers = None).build()
        val mockService: FakeUnusedClientStatusService = mock[FakeUnusedClientStatusService]

        running(application) {
          val bodyParsers = application.injector.instanceOf[BodyParsers.Default]
          val appConfig   = application.injector.instanceOf[FrontendAppConfig]

          val authAction = new AuthenticatedIdentifierAction(
            new FakeFailingAuthConnector(new InsufficientConfidenceLevel),
            mockService,
            appConfig,
            bodyParsers
          )
          val controller = new Harness(authAction)
          val result     = controller.onPageLoad()(FakeRequest())

          status(result) mustBe SEE_OTHER
          redirectLocation(result).value mustBe routes.UnauthorisedController.onPageLoad().url

          verifyNoInteractions(mockService)
        }
      }
    }

    "the user used an unaccepted auth provider" - {

      "must redirect the user to the unauthorised page" in {

        val application = applicationBuilder(userAnswers = None).build()

        val mockService: FakeUnusedClientStatusService = mock[FakeUnusedClientStatusService]

        running(application) {
          val bodyParsers = application.injector.instanceOf[BodyParsers.Default]
          val appConfig   = application.injector.instanceOf[FrontendAppConfig]

          val authAction = new AuthenticatedIdentifierAction(
            new FakeFailingAuthConnector(new UnsupportedAuthProvider),
            mockService,
            appConfig,
            bodyParsers
          )
          val controller = new Harness(authAction)
          val result     = controller.onPageLoad()(FakeRequest())

          status(result) mustBe SEE_OTHER
          redirectLocation(result).value mustBe routes.UnauthorisedController.onPageLoad().url

          verifyNoInteractions(mockService)
        }
      }
    }

    "the user has an unsupported affinity group" - {

      "must redirect the user to the unauthorised page" in {

        val application                                = applicationBuilder(userAnswers = None).build()
        val mockService: FakeUnusedClientStatusService = mock[FakeUnusedClientStatusService]

        running(application) {
          val bodyParsers = application.injector.instanceOf[BodyParsers.Default]
          val appConfig   = application.injector.instanceOf[FrontendAppConfig]

          val authAction = new AuthenticatedIdentifierAction(
            new FakeFailingAuthConnector(new UnsupportedAffinityGroup),
            mockService,
            appConfig,
            bodyParsers
          )
          val controller = new Harness(authAction)
          val result     = controller.onPageLoad()(FakeRequest())

          status(result) mustBe SEE_OTHER
          redirectLocation(result) mustBe Some(routes.UnauthorisedController.onPageLoad().url)

          verifyNoInteractions(mockService)
        }
      }
    }

    "the user has an unsupported credential role" - {

      "must redirect the user to the unauthorised page" in {

        val application                                = applicationBuilder(userAnswers = None).build()
        val mockService: FakeUnusedClientStatusService = mock[FakeUnusedClientStatusService]

        running(application) {
          val bodyParsers = application.injector.instanceOf[BodyParsers.Default]
          val appConfig   = application.injector.instanceOf[FrontendAppConfig]

          val authAction = new AuthenticatedIdentifierAction(
            new FakeFailingAuthConnector(new UnsupportedCredentialRole),
            mockService,
            appConfig,
            bodyParsers
          )
          val controller = new Harness(authAction)
          val result     = controller.onPageLoad()(FakeRequest())

          status(result) mustBe SEE_OTHER
          redirectLocation(result) mustBe Some(routes.UnauthorisedController.onPageLoad().url)

          verifyNoInteractions(mockService)
        }
      }
    }
  }
}

class FakeFailingAuthConnector @Inject() (exceptionToReturn: Throwable) extends AuthConnector {
  val serviceUrl: String = ""

  override def authorise[A](predicate: Predicate, retrieval: Retrieval[A])(implicit
    hc: HeaderCarrier,
    ec: ExecutionContext
  ): Future[A] =
    Future.failed(exceptionToReturn)
}

class FakeUnusedClientStatusService @Inject (connector: ClientListDownloadStatusConnector)
    extends ClientListDownloadStatusService(connector) {
  override def getClientListDownloadStatus(implicit
    hc: HeaderCarrier,
    ec: ExecutionContext
  ): Future[ClientListDownloadStatusTransformed] =
    Future.successful(InitiateDownload)
}
