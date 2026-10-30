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

import com.google.inject.Inject
import config.FrontendAppConfig
import controllers.actions.AuthenticatedIdentifierAction.{
  activatedState, agentEnrolment, agentKey, agentKeyValueRegex, orgEnrolment, orgKey, orgKeyValueRegex
}
import controllers.routes
import models.requests.IdentifierRequest
import play.api.Logging
import play.api.mvc.*
import play.api.mvc.Results.*
import uk.gov.hmrc.auth.core.*
import uk.gov.hmrc.auth.core.AffinityGroup.{Agent, Individual, Organisation}
import uk.gov.hmrc.auth.core.AuthProvider.GovernmentGateway
import uk.gov.hmrc.auth.core.authorise.Predicate
import uk.gov.hmrc.auth.core.retrieve.v2.Retrievals
import uk.gov.hmrc.auth.core.retrieve.~
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.play.http.HeaderCarrierConverter

import scala.concurrent.{ExecutionContext, Future}

trait IdentifierAction
    extends ActionBuilder[IdentifierRequest, AnyContent]
    with ActionFunction[Request, IdentifierRequest]

class AuthenticatedIdentifierAction @Inject() (
  override val authConnector: AuthConnector,
  config: FrontendAppConfig,
  val parser: BodyParsers.Default
)(implicit val executionContext: ExecutionContext)
    extends IdentifierAction
    with AuthorisedFunctions
    with Logging {

  override def invokeBlock[A](request: Request[A], block: IdentifierRequest[A] => Future[Result]): Future[Result] = {

    implicit val hc: HeaderCarrier = HeaderCarrierConverter.fromRequestAndSession(request, request.session)

    val defaultPredicate: Predicate = AuthProviders(GovernmentGateway)

    authorised(defaultPredicate)
      .retrieve(
        Retrievals.internalId and
          Retrievals.allEnrolments and
          Retrievals.affinityGroup and
          Retrievals.credentialRole
      ) {
        case Some(internalId) ~ Enrolments(enrolments) ~ Some(Organisation) ~ Some(User) if enrolments.exists(_.key == orgEnrolment) =>
          logger.info(s"Authorisation requested as Organisation")
          validateOrganisation(enrolments)(request, internalId, block)

        case Some(internalId) ~ Enrolments(enrolments) ~ Some(Agent) ~ Some(User) if enrolments.exists(_.key == agentEnrolment) =>
          logger.info(s"Authorisation requested as Agent")
          validateAgent(enrolments)(request, internalId, block)

        case Some(_) ~ _ ~ Some(Organisation | Agent) ~ Some(Assistant) =>
          logger.error(s"Unauthorised: Assistant login attempt")
          Future.successful(Redirect(routes.UnauthorisedController.onPageLoad()))

        case Some(_) ~ _ ~ Some(Organisation) ~ _ =>
          logger.error(s"Unauthorised: Organisation login attempt")
          Future.successful(Redirect(routes.UnauthorisedController.onPageLoad()))

        case Some(_) ~ _ ~ Some(Individual) ~ _ =>
          logger.error(s"Unauthorised: Individual login attempt")
          Future.successful(Redirect(routes.UnauthorisedController.onPageLoad()))

        case _ =>
          logger.error("Unauthorised: Authentication failure")
          Future.successful(Redirect(routes.UnauthorisedController.onPageLoad()))
      } recover {
      case _: NoActiveSession =>
        Redirect(config.loginUrl, Map("continue" -> Seq(config.loginContinueUrl)))

      case _: AuthorisationException =>
        Redirect(routes.UnauthorisedController.onPageLoad())
    }
  }

  private def validateOrganisation[A](
    enrolment: Set[Enrolment]
  )(request: Request[A], internalId: String, block: IdentifierRequest[A] => Future[Result]) = {
    val validUTR: Option[String] = for {
      enrolmentValue <- enrolment.find(e => e.key.equals(orgEnrolment) && e.state.equalsIgnoreCase(activatedState))
      identifier     <- enrolmentValue.identifiers.find(_.key.equals(orgKey))
      if identifier.value.matches(orgKeyValueRegex)
    } yield identifier.value
    validUTR match {
      case Some(utr) =>
        logger.info("Authenticated successful: Logged in as organisation")
        block(IdentifierRequest(request, internalId, validUTR, None))
      case None      =>
        logger.info("Authentication failed: Attempted login as organisation")
        Future.successful(Redirect(routes.UnauthorisedController.onPageLoad()))
    }
  }
  
  //TODO Add Business function F2 - Agent Service Guard
  private def validateAgent[A](
    enrolment: Set[Enrolment]
  )(request: Request[A], internalId: String, block: IdentifierRequest[A] => Future[Result]) = {
    val validIRAgentReference: Option[String] = for {
      enrolmentValue <- enrolment.find(e => e.key.equals(agentEnrolment) && e.state.equalsIgnoreCase(activatedState))
      identifier     <- enrolmentValue.identifiers.find(_.key.equals(agentKey))
      if identifier.value.matches(agentKeyValueRegex)
    } yield identifier.value

    validIRAgentReference match {
      case Some(utr) =>
        logger.info("Authentication successful: Authorised as an agent")
        block(IdentifierRequest(request, internalId, None, validIRAgentReference))
      case None      =>
        logger.info("Authentication failed: Attempted login as an agent")
        Future.successful(Redirect(routes.UnauthorisedController.onPageLoad()))
    }
  }

}

object AuthenticatedIdentifierAction {
  private val orgEnrolment: String   = "IR-CT"
  private val activatedState: String = "activated"
  private val agentEnrolment: String = "IR-CT-AGENT"
  private val agentKey: String       = "IRAgentReference"
  private val orgKey: String         = "UTR"
  private val agentKeyValueRegex     = "^[A-Za-z0-9]{6}$"
  private val orgKeyValueRegex       = "^[0-9]{10}$"

}

class SessionIdentifierAction @Inject() (
  val parser: BodyParsers.Default
)(implicit val executionContext: ExecutionContext)
    extends IdentifierAction {

  override def invokeBlock[A](request: Request[A], block: IdentifierRequest[A] => Future[Result]): Future[Result] = {

    implicit val hc: HeaderCarrier = HeaderCarrierConverter.fromRequestAndSession(request, request.session)

    hc.sessionId match {
      case Some(session) =>
        block(IdentifierRequest(request, session.value))
      case None          =>
        Future.successful(Redirect(routes.JourneyRecoveryController.onPageLoad()))
    }
  }
}
