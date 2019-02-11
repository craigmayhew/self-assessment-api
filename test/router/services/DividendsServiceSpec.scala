/*
 * Copyright 2019 HM Revenue & Customs
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

import mocks.config.MockAppConfig
import mocks.connectors.{MockDividendsConnector, MockSelfAssessmentConnector}
import play.api.Configuration
import play.api.libs.json.Json
import play.api.test.FakeRequest
import router.constants.Versions.VERSION_2
import router.errors.{IncorrectAPIVersion, UnsupportedAPIVersion}
import support.UnitSpec
import uk.gov.hmrc.http.{HeaderCarrier, HttpResponse}

import scala.concurrent.Future

class DividendsServiceSpec extends UnitSpec
  with MockDividendsConnector with MockAppConfig with MockSelfAssessmentConnector {

  class Setup {

    object service extends DividendsService(
      mockAppConfig,
      mockDividendsConnector,
      mockSelfAssessmentConnector
    )
    
  }

  implicit val request = FakeRequest()

  "amend" should {
    val requestBody = Json.obj("test" -> "body")

    "return a HttpResponse" when {
      "the request contains a version 1.0 header and dividends version 2 config is disabled" in new Setup {
        implicit val hc = HeaderCarrier(extraHeaders = Seq(ACCEPT -> "application/vnd.hmrc.1.0+json"))
        val response = HttpResponse(204)
        val dividendsVersionTwoConfig = Configuration("dividends-income-version-2.enabled" -> false)

        MockAppConfig.featureSwitch returns Some(dividendsVersionTwoConfig)
        MockSelfAssessmentConnector.put(request.uri, requestBody)
          .returns(Future.successful(Right(response)))

        val result = await(service.put(requestBody))
        result shouldBe Right(response)
      }

      "the request contains a version 1.0 header and dividends version 2 config is enabled" in new Setup {
        implicit val hc = HeaderCarrier(extraHeaders = Seq(ACCEPT -> "application/vnd.hmrc.1.0+json"))
        val response = HttpResponse(204)
        val dividendsVersionTwoConfig = Configuration("dividends-income-version-2.enabled" -> true)

        MockAppConfig.featureSwitch returns Some(dividendsVersionTwoConfig)
        MockSelfAssessmentConnector.put(request.uri, requestBody)
          .returns(Future.successful(Right(response)))

        val result = await(service.put(requestBody))
        result shouldBe Right(response)
      }

      "the request contains a version 2.0 header and dividends version 2 config is disabled" in new Setup {
        implicit val hc = HeaderCarrier(extraHeaders = Seq(ACCEPT -> "application/vnd.hmrc.2.0+json"))
        val response = HttpResponse(204)
        val dividendsVersionTwoConfig = Configuration("dividends-income-version-2.enabled" -> false)
        MockAppConfig.featureSwitch returns Some(dividendsVersionTwoConfig)

        MockSelfAssessmentConnector.put(request.uri, requestBody)
          .returns(Future.successful(Right(response)))

        val result = await(service.put(requestBody))
        result shouldBe Right(response)
      }

      "the request contains a version 2.0 header and dividends version 2 config is enabled" in new Setup {
        implicit val hc = HeaderCarrier(extraHeaders = Seq(ACCEPT -> "application/vnd.hmrc.2.0+json"))
        val response = HttpResponse(204)
        val dividendsVersionTwoConfig = Configuration("dividends-income-version-2.enabled" -> true)
        MockAppConfig.featureSwitch returns Some(dividendsVersionTwoConfig)

        MockDividendsConnector.put(s"/$VERSION_2${request.uri}", requestBody)
          .returns(Future.successful(Right(response)))

        val result = await(service.put(requestBody))
        result shouldBe Right(response)
      }
    }

    "return an UnsupportedAPIVersion error" when {
      "the Accept header contains an unsupported API version" in new Setup {
        implicit val hc = HeaderCarrier(extraHeaders = Seq(ACCEPT -> "application/vnd.hmrc.5.0+json"))

        val result = await(service.put(requestBody))
        result shouldBe Left(UnsupportedAPIVersion)
      }
    }

    "return an IncorrectAPIVersion" when {
      "the Accept header contains an incorrect value" in new Setup {
        implicit val hc = HeaderCarrier(extraHeaders = Seq(ACCEPT -> "incorrect value"))

        val result = await(service.put(requestBody))
        result shouldBe Left(IncorrectAPIVersion)
      }
    }
  }

  "retrieve" should {
    val body = Json.parse(
      s"""{
         |  "ukDividends": 1000.00,
         |  "otherUkDividends": 2000.00
         |}""".stripMargin
    )
    "return a HttpResponse" when {
      "the request contains a version 2.0 header and dividends-income version 2 config is enabled" in new Setup {
        implicit val hc = HeaderCarrier(extraHeaders = Seq(ACCEPT -> "application/vnd.hmrc.2.0+json"))


        val correlationId = "X-123"
        val httpResponse = HttpResponse(OK, Some(body), Map("CorrelationId" -> Seq(correlationId)))
        val dividendsVersionTwoConfig = Configuration("dividends-income-version-2.enabled" -> true)

        MockAppConfig.featureSwitch returns Some(dividendsVersionTwoConfig)
        MockDividendsConnector.get(s"/$VERSION_2${request.uri}")
          .returns(Future.successful(Right(httpResponse)))

        val result = await(service.get())
        result shouldBe Right(httpResponse)
      }

      "the request contains a version 1.0 header and dividends version 2 config is disabled" in new Setup {
        implicit val hc = HeaderCarrier(extraHeaders = Seq(ACCEPT -> "application/vnd.hmrc.1.0+json"))
        val response = HttpResponse(200)
        val dividendsVersionTwoConfig = Configuration("dividends-income-version-2.enabled" -> false)

        MockAppConfig.featureSwitch returns Some(dividendsVersionTwoConfig)
        MockSelfAssessmentConnector.get(request.uri)
          .returns(Future.successful(Right(response)))

        val result = await(service.get())
        result shouldBe Right(response)
      }

      "the request contains a version 1.0 header and dividends version 2 config is enabled" in new Setup {
        implicit val hc = HeaderCarrier(extraHeaders = Seq(ACCEPT -> "application/vnd.hmrc.1.0+json"))
        val response = HttpResponse(200)
        val dividendsVersionTwoConfig = Configuration("dividends-income-version-2.enabled" -> true)

        MockAppConfig.featureSwitch returns Some(dividendsVersionTwoConfig)
        MockSelfAssessmentConnector.get(request.uri)
          .returns(Future.successful(Right(response)))

        val result = await(service.get())
        result shouldBe Right(response)
      }

      "the request contains a version 2.0 header and dividends version 2 config is disabled" in new Setup {
        implicit val hc = HeaderCarrier(extraHeaders = Seq(ACCEPT -> "application/vnd.hmrc.2.0+json"))
        val response = HttpResponse(200)
        val dividendsVersionTwoConfig = Configuration("dividends-income-version-2.enabled" -> false)
        MockAppConfig.featureSwitch returns Some(dividendsVersionTwoConfig)

        MockSelfAssessmentConnector.get(request.uri)
          .returns(Future.successful(Right(response)))

        val result = await(service.get())
        result shouldBe Right(response)
      }

      "return an UnsupportedAPIVersion error" when {
        "the Accept header contains an unsupported API version" in new Setup {
          implicit val hc = HeaderCarrier(extraHeaders = Seq(ACCEPT -> "application/vnd.hmrc.5.0+json"))

          val result = await(service.get())
          result shouldBe Left(UnsupportedAPIVersion)
        }
      }

      "return an IncorrectAPIVersion" when {
        "the Accept header contains an incorrect value" in new Setup {
          implicit val hc = HeaderCarrier(extraHeaders = Seq(ACCEPT -> "incorrect value"))

          val result = await(service.get())
          result shouldBe Left(IncorrectAPIVersion)
        }
      }
    }
  }
}
