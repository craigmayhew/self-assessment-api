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

import mocks.config.MockAppConfig
import mocks.connectors.MockPropertyConnector
import play.api.Configuration
import play.api.libs.json.Json
import play.api.test.FakeRequest
import router.errors.{IncorrectAPIVersion, UnsupportedAPIVersion}
import support.UnitSpec
import uk.gov.hmrc.http.{HeaderCarrier, HttpResponse}

import scala.concurrent.Future

class PropertyPeriodServiceSpec extends UnitSpec
  with MockPropertyConnector with MockAppConfig {

  class Setup {

    object service extends PropertyPeriodService(
      mockAppConfig,
      mockPropertyConnector
    )
  }

  implicit val request = FakeRequest()

  "post" should {
    val requestBody = Json.obj("test" -> "body")

    "return a HttpResponse" when {
      "the request contains a version 1.0 header and release-2 config is disabled" in new Setup {
        implicit val hc = HeaderCarrier(extraHeaders = Seq(ACCEPT -> "application/vnd.hmrc.1.0+json"))
        val response = HttpResponse(201)
        val release2EnabledConfig = Configuration("release-2.enabled" -> false)
        MockAppConfig.featureSwitch returns Some(release2EnabledConfig)

        MockPropertyConnector.post(request.uri, requestBody)
          .returns(Future.successful(Right(response)))

        val result = await(service.create(requestBody))
        result shouldBe Right(response)
      }

      "the request contains a version 1.0 header and release-2 config is enabled" in new Setup {
        implicit val hc = HeaderCarrier(extraHeaders = Seq(ACCEPT -> "application/vnd.hmrc.1.0+json"))
        val response = HttpResponse(201)
        val release2EnabledConfig = Configuration("release-2.enabled" -> true)
        MockAppConfig.featureSwitch returns Some(release2EnabledConfig)

        MockPropertyConnector.post(s"/r2${request.uri}", requestBody)
          .returns(Future.successful(Right(response)))

        val result = await(service.create(requestBody))
        result shouldBe Right(response)
      }

      "the request contains a version 2.0 header and release-2 config is disabled" in new Setup {
        implicit val hc = HeaderCarrier(extraHeaders = Seq(ACCEPT -> "application/vnd.hmrc.2.0+json"))
        val response = HttpResponse(201)

        val release2EnabledConfig = Configuration("release-2.enabled" -> false)
        MockAppConfig.featureSwitch returns Some(release2EnabledConfig)

        MockPropertyConnector.post(request.uri, requestBody)
          .returns(Future.successful(Right(response)))

        val result = await(service.create(requestBody))
        result shouldBe Right(response)
      }

      "the request contains a version 2.0 header and release-2 config is enabled" in new Setup {
        implicit val hc = HeaderCarrier(extraHeaders = Seq(ACCEPT -> "application/vnd.hmrc.2.0+json"))
        val response = HttpResponse(201)

        val release2EnabledConfig = Configuration("release-2.enabled" -> true)
        MockAppConfig.featureSwitch returns Some(release2EnabledConfig)

        MockPropertyConnector.post(s"/r2${request.uri}", requestBody)
          .returns(Future.successful(Right(response)))

        val result = await(service.create(requestBody))
        result shouldBe Right(response)
      }
    }

    "return an UnsupportedAPIVersion error" when {

      "the Accept header contains an unsupported API version" in new Setup {
        implicit val hc = HeaderCarrier(extraHeaders = Seq(ACCEPT -> "application/vnd.hmrc.5.0+json"))

        val result = await(service.create(requestBody))
        result shouldBe Left(UnsupportedAPIVersion)
      }
    }

    "return an IncorrectAPIVersion" when {

      "the Accept header contains an incorrect value" in new Setup {
        implicit val hc = HeaderCarrier(extraHeaders = Seq(ACCEPT -> "incorrect value"))

        val result = await(service.create(requestBody))
        result shouldBe Left(IncorrectAPIVersion)
      }
    }
  }
}
