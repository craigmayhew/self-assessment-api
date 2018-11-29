/*
 * Copyright 2018 HM Revenue & Customs
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

package router.services

import config.{AppConfig, FeatureSwitch}
import javax.inject.Inject
import play.api.libs.json.JsValue
import play.api.mvc.Request
import router.connectors.SelfAssessmentConnector
import router.constants.Versions.{VERSION_1, VERSION_2}
import router.httpParsers.SelfAssessmentHttpParser.SelfAssessmentOutcome
import uk.gov.hmrc.http.HeaderCarrier

import scala.concurrent.Future

class PropertyPeriodService @Inject()(val appConfig: AppConfig,
                                      val selfAssessmentConnector: SelfAssessmentConnector) extends Service {

  def create(body: JsValue)(implicit hc: HeaderCarrier, req: Request[_]): Future[SelfAssessmentOutcome] = {

    def uri = {
      val featureSwitch = FeatureSwitch(appConfig.featureSwitch)
      if (featureSwitch.isRelease2Enabled) {
        s"/r2${req.uri}"
      }
      else {
        s"${req.uri}"
      }
    }

    withApiVersion {
      case Some(VERSION_1) => selfAssessmentConnector.post(uri, body)
      case Some(VERSION_2) => selfAssessmentConnector.post(uri, body)(convertHeaderToVersion1, req)
    }
  }
}
