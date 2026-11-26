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

package services.agentValidation

import connectors.agentValidation.ClientListDownloadStatusConnector
import models.agentValidation.ClientListDownloadStatusTransformed
import models.agentValidation.ClientListDownloadStatusTransformed.{Failed, InProgress, InitiateDownload, Succeeded}
import play.api.Logging
import uk.gov.hmrc.http.HeaderCarrier

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class ClientListDownloadStatusService @Inject() (
  connector: ClientListDownloadStatusConnector
) extends Logging {

  def getClientListDownloadStatus(implicit
    hc: HeaderCarrier,
    ec: ExecutionContext
  ): Future[ClientListDownloadStatusTransformed] = {
    logger.info(
      s"Calling ClientListDownloadStatusConnector"
    )
    connector.getDownloadStatus
      .map { value =>
        value.result match {
          case -1    => InitiateDownload
          case 0     => InProgress
          case 1     => Succeeded
          case 2     => Failed
          case other =>
            logger.error(s"Unknown status from '$other' from Backend")
            throw new RuntimeException(s"Unknown status '$other' from rds-datacache-proxy")
        }
      }
  }
}
