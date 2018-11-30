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

package router.resources

import play.api.libs.json.{JsObject, Json}
import support.IntegrationSpec

class PropertyPeriodResourceISpec extends IntegrationSpec {

  val jsonRequest: JsObject = Json.obj("test" -> "json request")
  val jsonResponse: JsObject = Json.obj("test" -> "json response")

  "Create Non-FHL UK Property period with release-2 enabled" should {
    "return a 200 with no json response body" when {
      "the downstream response from the self assessment api version 1.0 returns a 200 with a json response body" in {
        val incomingUrl = "/ni/AA111111A/uk-properties/other/periods"
        val outgoingUrl = "/ni/AA111111A/uk-properties/other/periods"

        Given()
          .theClientIsAuthorised
          .And()
          .post(outgoingUrl)
          .returns(aResponse
            .withStatus(OK))
          .When()
          .post(incomingUrl)
            .withBody(jsonRequest)
          .withHeaders(
            ACCEPT -> "application/vnd.hmrc.1.0+json",
            CONTENT_TYPE -> JSON)
          .Then()
          .statusIs(OK)
          .verify(mockFor(outgoingUrl)
            .receivedHeaders(ACCEPT -> "application/vnd.hmrc.1.0+json"))
      }

      "the downstream response from the self assessment api version 2.0 returns a 200 with a json response body" in {
        val incomingUrl = "/ni/AA111111A/uk-properties/other/periods"
        val outgoingUrl = "/ni/AA111111A/uk-properties/other/periods"

        Given()
          .theClientIsAuthorised
          .And()
          .post(outgoingUrl)
          .returns(aResponse
            .withStatus(OK))
          .When()
          .post(incomingUrl)
          .withBody(jsonRequest)
          .withHeaders(
            ACCEPT -> "application/vnd.hmrc.2.0+json",
            CONTENT_TYPE -> JSON)
          .Then()
          .statusIs(OK)
          .verify(mockFor(outgoingUrl)
            .receivedHeaders(ACCEPT -> "application/vnd.hmrc.1.0+json"))
      }
    }
  }

  "GET All Non-FHL UK Property period with release-2 enabled" should {
    "return a 200 with a json response body" when {
      "the downstream response from the self assessment api returns returns a 200 with a json response body" in {
        val incomingUrl = "/ni/AA111111A/uk-properties/other/periods"
        val outgoingUrl = "/ni/AA111111A/uk-properties/other/periods"
        Given()
          .theClientIsAuthorised
          .And()
          .get(outgoingUrl)
          .returns(aResponse
            .withStatus(OK)
            .withBody(jsonResponse))
          .When()
          .get(incomingUrl)
          .withHeaders(ACCEPT -> "application/vnd.hmrc.1.0+json")
          .Then()
          .statusIs(OK)
          .bodyIs(jsonResponse)
          .verify(mockFor(outgoingUrl)
            .receivedHeaders(ACCEPT -> "application/vnd.hmrc.1.0+json"))
      }

      "a version 2.0 header is provided and the downstream response from the self assessment api returns a 200 with a json response body" in {
        val incomingUrl = "/ni/AA111111A/uk-properties/other/periods"
        val outgoingUrl = "/ni/AA111111A/uk-properties/other/periods"

        Given()
          .theClientIsAuthorised
          .And()
          .get(outgoingUrl)
          .returns(aResponse
            .withStatus(OK)
            .withBody(jsonResponse))
          .When()
          .get(incomingUrl)
          .withHeaders(ACCEPT -> "application/vnd.hmrc.2.0+json")
          .Then()
          .statusIs(OK)
          .bodyIs(jsonResponse)
          .verify(mockFor(outgoingUrl)
            .receivedHeaders(ACCEPT -> "application/vnd.hmrc.1.0+json"))
      }
    }
  }

  "GET Non-FHL UK Property period with release-2 enabled" should {
    "return a 200 with a json response body" when {
      "the downstream response from the self assessment api returns returns a 200 with a json response body" in {
        val incomingUrl = "/ni/AA111111A/uk-properties/other/periods/periodId"
        val outgoingUrl = "/ni/AA111111A/uk-properties/other/periods/periodId"
        Given()
          .theClientIsAuthorised
          .And()
          .get(outgoingUrl)
          .returns(aResponse
            .withStatus(OK)
            .withBody(jsonResponse))
          .When()
          .get(incomingUrl)
          .withHeaders(ACCEPT -> "application/vnd.hmrc.1.0+json")
          .Then()
          .statusIs(OK)
          .bodyIs(jsonResponse)
          .verify(mockFor(outgoingUrl)
            .receivedHeaders(ACCEPT -> "application/vnd.hmrc.1.0+json"))
      }

      "a version 2.0 header is provided and the downstream response from the self assessment api returns a 200 with a json response body" in {
        val incomingUrl = "/ni/AA111111A/uk-properties/other/periods/periodId"
        val outgoingUrl = "/ni/AA111111A/uk-properties/other/periods/periodId"

        Given()
          .theClientIsAuthorised
          .And()
          .get(outgoingUrl)
          .returns(aResponse
            .withStatus(OK)
            .withBody(jsonResponse))
          .When()
          .get(incomingUrl)
          .withHeaders(ACCEPT -> "application/vnd.hmrc.2.0+json")
          .Then()
          .statusIs(OK)
          .bodyIs(jsonResponse)
          .verify(mockFor(outgoingUrl)
            .receivedHeaders(ACCEPT -> "application/vnd.hmrc.1.0+json"))
      }
    }
  }


  "PUT Non-FHL UK Property period with release-2 enabled" should {
    "return a 200 with no json response body" when {
      "the downstream response from the self assessment api returns a 200 with a json response body" in {
        val incomingUrl = "/ni/AA111111A/uk-properties/other/periods/periodId"
        val outgoingUrl = "/ni/AA111111A/uk-properties/other/periods/periodId"

        Given()
          .theClientIsAuthorised
          .And()
          .put(outgoingUrl)
          .returns(aResponse
            .withStatus(OK)
            .withBody(jsonResponse))
          .When()
          .put(incomingUrl)
          .withBody(jsonRequest)
          .withHeaders(
            ACCEPT -> "application/vnd.hmrc.1.0+json",
            CONTENT_TYPE -> JSON
          )
          .Then()
          .statusIs(OK)
          .bodyIs(jsonResponse)
          .verify(mockFor(outgoingUrl)
            .receivedHeaders(ACCEPT -> "application/vnd.hmrc.1.0+json"))
      }
    }
  }
}
