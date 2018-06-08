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

package mocks.connectors

import mocks.Mock
import org.mockito.stubbing.OngoingStubbing
import org.scalatest.Suite
import play.api.libs.json.JsValue
import router.connectors.SelfAssessmentConnector
import router.httpParsers.SelfAssessmentHttpParser.SelfAssessmentOutcome

import scala.concurrent.Future

trait MockSelfAssessmentConnector extends Mock { _: Suite =>

  val mockSelfAssessmentConnector = mock[SelfAssessmentConnector]

  object MockSelfAssessmentConnector {
    def get(): OngoingStubbing[Future[SelfAssessmentOutcome]] = {
      when(mockSelfAssessmentConnector.get()(any(), any()))
    }

    def post(body: JsValue): OngoingStubbing[Future[SelfAssessmentOutcome]] = {
      when(mockSelfAssessmentConnector.post(eqTo(body))(any(), any()))
    }

    def put(body: JsValue): OngoingStubbing[Future[SelfAssessmentOutcome]] = {
      when(mockSelfAssessmentConnector.put(eqTo(body))(any(), any()))
    }
  }

  override protected def beforeEach(): Unit = {
    super.beforeEach()
    reset(mockSelfAssessmentConnector)
  }
}