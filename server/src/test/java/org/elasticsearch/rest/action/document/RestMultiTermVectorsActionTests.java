/*
 * Licensed to Elasticsearch under one or more contributor
 * license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright
 * ownership. Elasticsearch licenses this file to you under
 * the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.elasticsearch.rest.action.document;

import org.elasticsearch.common.bytes.BytesReference;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.rest.RestRequest;
import org.elasticsearch.rest.RestRequest.Method;
import org.elasticsearch.rest.action.RestActionTestCase;
import org.elasticsearch.test.rest.FakeRestRequest;
import org.junit.Before;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RestMultiTermVectorsActionTests extends RestActionTestCase {

    @Before
    public void setUpAction() {
        new RestMultiTermVectorsAction(Settings.EMPTY, controller());
    }

    public void testTypeInPath() {
        RestRequest request = new FakeRestRequest.Builder(xContentRegistry())
            .withMethod(Method.POST)
            .withPath("/some_index/some_type/_mtermvectors")
            .build();

        dispatchRequest(request);
        assertWarnings(RestMultiTermVectorsAction.TYPES_DEPRECATION_MESSAGE);
    }

    public void testTypeParameter() {
        Map<String, String> params = new HashMap<>();
        params.put("type", "some_type");

        RestRequest request = new FakeRestRequest.Builder(xContentRegistry())
            .withMethod(Method.GET)
            .withPath("/some_index/_mtermvectors")
            .withParams(params)
            .build();

        dispatchRequest(request);
        assertWarnings(RestMultiTermVectorsAction.TYPES_DEPRECATION_MESSAGE);
    }

    public void testTypeInBody() throws IOException {
        XContentBuilder content = XContentFactory.jsonBuilder().startObject()
            .startArray("docs")
                .startObject()
                    .field("_type", "some_type")
                    .field("_id", 1)
                .endObject()
            .endArray()
        .endObject();

        RestRequest request = new FakeRestRequest.Builder(xContentRegistry())
            .withMethod(Method.GET)
            .withPath("/some_index/_mtermvectors")
            .withContent(BytesReference.bytes(content), XContentType.JSON)
            .build();

        dispatchRequest(request);
        assertWarnings(RestTermVectorsAction.TYPES_DEPRECATION_MESSAGE);
    }
}
