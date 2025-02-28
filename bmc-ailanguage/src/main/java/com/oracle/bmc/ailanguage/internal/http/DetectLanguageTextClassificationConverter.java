/**
 * Copyright (c) 2016, 2021, Oracle and/or its affiliates.  All rights reserved.
 * This software is dual-licensed to you under the Universal Permissive License (UPL) 1.0 as shown at https://oss.oracle.com/licenses/upl or Apache License 2.0 as shown at http://www.apache.org/licenses/LICENSE-2.0. You may choose either license.
 */
package com.oracle.bmc.ailanguage.internal.http;

import com.oracle.bmc.http.internal.ResponseHelper;
import com.oracle.bmc.ailanguage.model.*;
import com.oracle.bmc.ailanguage.requests.*;
import com.oracle.bmc.ailanguage.responses.*;
import org.apache.commons.lang3.Validate;

@javax.annotation.Generated(value = "OracleSDKGenerator", comments = "API Version: 20210101")
@lombok.extern.slf4j.Slf4j
public class DetectLanguageTextClassificationConverter {
    private static final com.oracle.bmc.http.internal.ResponseConversionFunctionFactory
            RESPONSE_CONVERSION_FACTORY =
                    new com.oracle.bmc.http.internal.ResponseConversionFunctionFactory();

    public static com.oracle.bmc.ailanguage.requests.DetectLanguageTextClassificationRequest
            interceptRequest(
                    com.oracle.bmc.ailanguage.requests.DetectLanguageTextClassificationRequest
                            request) {

        return request;
    }

    public static com.oracle.bmc.http.internal.WrappedInvocationBuilder fromRequest(
            com.oracle.bmc.http.internal.RestClient client,
            com.oracle.bmc.ailanguage.requests.DetectLanguageTextClassificationRequest request) {
        Validate.notNull(request, "request instance is required");
        Validate.notNull(
                request.getDetectLanguageTextClassificationDetails(),
                "detectLanguageTextClassificationDetails is required");

        com.oracle.bmc.http.internal.WrappedWebTarget target =
                client.getBaseTarget()
                        .path("/20210101")
                        .path("actions")
                        .path("detectLanguageTextClassification");

        com.oracle.bmc.http.internal.WrappedInvocationBuilder ib = target.request();

        ib.accept(javax.ws.rs.core.MediaType.APPLICATION_JSON);

        if (request.getOpcRequestId() != null) {
            ib.header("opc-request-id", request.getOpcRequestId());
        }

        return ib;
    }

    public static com.google.common.base.Function<
                    javax.ws.rs.core.Response,
                    com.oracle.bmc.ailanguage.responses.DetectLanguageTextClassificationResponse>
            fromResponse() {
        final com.google.common.base.Function<
                        javax.ws.rs.core.Response,
                        com.oracle.bmc.ailanguage.responses
                                .DetectLanguageTextClassificationResponse>
                transformer =
                        new com.google.common.base.Function<
                                javax.ws.rs.core.Response,
                                com.oracle.bmc.ailanguage.responses
                                        .DetectLanguageTextClassificationResponse>() {
                            @Override
                            public com.oracle.bmc.ailanguage.responses
                                            .DetectLanguageTextClassificationResponse
                                    apply(javax.ws.rs.core.Response rawResponse) {
                                LOG.trace(
                                        "Transform function invoked for com.oracle.bmc.ailanguage.responses.DetectLanguageTextClassificationResponse");
                                com.google.common.base.Function<
                                                javax.ws.rs.core.Response,
                                                com.oracle.bmc.http.internal.WithHeaders<
                                                        DetectLanguageTextClassificationResult>>
                                        responseFn =
                                                RESPONSE_CONVERSION_FACTORY.create(
                                                        DetectLanguageTextClassificationResult
                                                                .class);

                                com.oracle.bmc.http.internal.WithHeaders<
                                                DetectLanguageTextClassificationResult>
                                        response = responseFn.apply(rawResponse);
                                javax.ws.rs.core.MultivaluedMap<String, String> headers =
                                        response.getHeaders();

                                com.oracle.bmc.ailanguage.responses
                                                .DetectLanguageTextClassificationResponse.Builder
                                        builder =
                                                com.oracle.bmc.ailanguage.responses
                                                        .DetectLanguageTextClassificationResponse
                                                        .builder()
                                                        .__httpStatusCode__(
                                                                rawResponse.getStatus());

                                builder.detectLanguageTextClassificationResult(response.getItem());

                                com.google.common.base.Optional<java.util.List<String>>
                                        opcRequestIdHeader =
                                                com.oracle.bmc.http.internal.HeaderUtils.get(
                                                        headers, "opc-request-id");
                                if (opcRequestIdHeader.isPresent()) {
                                    builder.opcRequestId(
                                            com.oracle.bmc.http.internal.HeaderUtils.toValue(
                                                    "opc-request-id",
                                                    opcRequestIdHeader.get().get(0),
                                                    String.class));
                                }

                                com.oracle.bmc.ailanguage.responses
                                                .DetectLanguageTextClassificationResponse
                                        responseWrapper = builder.build();

                                ResponseHelper.closeResponseSilentlyIfNotBuffered(rawResponse);
                                return responseWrapper;
                            }
                        };
        return transformer;
    }
}
