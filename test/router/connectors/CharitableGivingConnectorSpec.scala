/*
 * Copyright 2021 HM Revenue & Customs
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

package router.connectors

import mocks.MockHttp
import mocks.config.MockAppConfig
import mocks.httpParser.MockSelfAssessmentHttpParser
import play.api.http.Status
import play.api.libs.json.{JsValue, Json}
import router.httpParsers.SelfAssessmentHttpParser.SelfAssessmentOutcome
import support.UnitSpec
import uk.gov.hmrc.http.HttpResponse

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class CharitableGivingConnectorSpec extends UnitSpec
  with MockHttp
  with MockAppConfig
  with MockSelfAssessmentHttpParser {

  class Setup {
    object TestConnector extends CharitableGivingConnector(
      mockHttp,
      mockSelfAssessmentHttpParser,
      mockAppConfig
    )
    MockAppConfig.cgApiUrl returns cgApiUrl
  }

  lazy val cgApiUrl = "test-cg-api-url"
  val path = "/2.0/test-path"

  "amend" should {
    "return a HttpResponse" when {
      "a successful HttpResponse is returned" in new Setup {
        val response  = HttpResponse(Status.OK, Json.obj().toString())
        val requestJson = Json.obj("test" -> "request json")

        MockSelfAssessmentHttpParser.read.returns(Right(response))
        MockHttp.PUT[JsValue, SelfAssessmentOutcome](s"$cgApiUrl$path", requestJson).returns(Future.successful(Right(response)))
        await(TestConnector.put(path, requestJson)(hc)) shouldBe Right(response)
      }
    }
  }

  "retrieve" should {
    "return a HttpResponse" when {
      "a successful HttpResponse is returned" in new Setup {
        val response  = HttpResponse(Status.OK, Json.obj().toString())

        MockSelfAssessmentHttpParser.read.returns(Right(response))
        MockHttp.GET[SelfAssessmentOutcome](s"$cgApiUrl$path").returns(Future.successful(Right(response)))
        await(TestConnector.get(path)(hc)) shouldBe Right(response)
      }
    }
  }
}
