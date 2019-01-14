/**
 * Copyright (c) 2016, 2019, Oracle and/or its affiliates. All rights reserved.
 */
package com.oracle.bmc.core.internal.http;

import com.oracle.bmc.core.model.*;
import com.oracle.bmc.core.requests.*;
import com.oracle.bmc.core.responses.*;
import org.apache.commons.lang3.Validate;

@javax.annotation.Generated(value = "OracleSDKGenerator", comments = "API Version: 20160918")
@lombok.extern.slf4j.Slf4j
public class GetWindowsInstanceInitialCredentialsConverter {
    private static final com.oracle.bmc.http.internal.ResponseConversionFunctionFactory
            RESPONSE_CONVERSION_FACTORY =
                    new com.oracle.bmc.http.internal.ResponseConversionFunctionFactory();

    public static GetWindowsInstanceInitialCredentialsRequest interceptRequest(
            GetWindowsInstanceInitialCredentialsRequest request) {

        return request;
    }

    public static com.oracle.bmc.http.internal.WrappedInvocationBuilder fromRequest(
            com.oracle.bmc.http.internal.RestClient client,
            GetWindowsInstanceInitialCredentialsRequest request) {
        Validate.notNull(request, "request instance is required");
        Validate.notBlank(request.getInstanceId(), "instanceId must not be blank");

        com.oracle.bmc.http.internal.WrappedWebTarget target =
                client.getBaseTarget()
                        .path("/20160918")
                        .path("instances")
                        .path(
                                com.oracle.bmc.util.internal.HttpUtils.encodePathSegment(
                                        request.getInstanceId()))
                        .path("initialCredentials");

        com.oracle.bmc.http.internal.WrappedInvocationBuilder ib = target.request();

        ib.accept(javax.ws.rs.core.MediaType.APPLICATION_JSON);

        return ib;
    }

    public static com.google.common.base.Function<
                    javax.ws.rs.core.Response, GetWindowsInstanceInitialCredentialsResponse>
            fromResponse() {
        final com.google.common.base.Function<
                        javax.ws.rs.core.Response, GetWindowsInstanceInitialCredentialsResponse>
                transformer =
                        new com.google.common.base.Function<
                                javax.ws.rs.core.Response,
                                GetWindowsInstanceInitialCredentialsResponse>() {
                            @Override
                            public GetWindowsInstanceInitialCredentialsResponse apply(
                                    javax.ws.rs.core.Response rawResponse) {
                                LOG.trace(
                                        "Transform function invoked for GetWindowsInstanceInitialCredentialsResponse");
                                com.google.common.base.Function<
                                                javax.ws.rs.core.Response,
                                                com.oracle.bmc.http.internal.WithHeaders<
                                                        InstanceCredentials>>
                                        responseFn =
                                                RESPONSE_CONVERSION_FACTORY.create(
                                                        InstanceCredentials.class);

                                com.oracle.bmc.http.internal.WithHeaders<InstanceCredentials>
                                        response = responseFn.apply(rawResponse);
                                javax.ws.rs.core.MultivaluedMap<String, String> headers =
                                        response.getHeaders();

                                GetWindowsInstanceInitialCredentialsResponse.Builder builder =
                                        GetWindowsInstanceInitialCredentialsResponse.builder();

                                builder.instanceCredentials(response.getItem());

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

                                GetWindowsInstanceInitialCredentialsResponse responseWrapper =
                                        builder.build();

                                return responseWrapper;
                            }
                        };
        return transformer;
    }
}
