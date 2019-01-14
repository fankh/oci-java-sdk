/**
 * Copyright (c) 2016, 2019, Oracle and/or its affiliates. All rights reserved.
 */
package com.oracle.bmc.database.internal.http;

import com.oracle.bmc.database.model.*;
import com.oracle.bmc.database.requests.*;
import com.oracle.bmc.database.responses.*;
import org.apache.commons.lang3.Validate;

@javax.annotation.Generated(value = "OracleSDKGenerator", comments = "API Version: 20160918")
@lombok.extern.slf4j.Slf4j
public class CreateAutonomousDataWarehouseBackupConverter {
    private static final com.oracle.bmc.http.internal.ResponseConversionFunctionFactory
            RESPONSE_CONVERSION_FACTORY =
                    new com.oracle.bmc.http.internal.ResponseConversionFunctionFactory();

    public static CreateAutonomousDataWarehouseBackupRequest interceptRequest(
            CreateAutonomousDataWarehouseBackupRequest request) {

        return request;
    }

    public static com.oracle.bmc.http.internal.WrappedInvocationBuilder fromRequest(
            com.oracle.bmc.http.internal.RestClient client,
            CreateAutonomousDataWarehouseBackupRequest request) {
        Validate.notNull(request, "request instance is required");
        Validate.notNull(
                request.getCreateAutonomousDataWarehouseBackupDetails(),
                "createAutonomousDataWarehouseBackupDetails is required");

        com.oracle.bmc.http.internal.WrappedWebTarget target =
                client.getBaseTarget().path("/20160918").path("autonomousDataWarehouseBackups");

        com.oracle.bmc.http.internal.WrappedInvocationBuilder ib = target.request();

        ib.accept(javax.ws.rs.core.MediaType.APPLICATION_JSON);

        if (request.getOpcRetryToken() != null) {
            ib.header("opc-retry-token", request.getOpcRetryToken());
        }

        return ib;
    }

    public static com.google.common.base.Function<
                    javax.ws.rs.core.Response, CreateAutonomousDataWarehouseBackupResponse>
            fromResponse() {
        final com.google.common.base.Function<
                        javax.ws.rs.core.Response, CreateAutonomousDataWarehouseBackupResponse>
                transformer =
                        new com.google.common.base.Function<
                                javax.ws.rs.core.Response,
                                CreateAutonomousDataWarehouseBackupResponse>() {
                            @Override
                            public CreateAutonomousDataWarehouseBackupResponse apply(
                                    javax.ws.rs.core.Response rawResponse) {
                                LOG.trace(
                                        "Transform function invoked for CreateAutonomousDataWarehouseBackupResponse");
                                com.google.common.base.Function<
                                                javax.ws.rs.core.Response,
                                                com.oracle.bmc.http.internal.WithHeaders<
                                                        AutonomousDataWarehouseBackup>>
                                        responseFn =
                                                RESPONSE_CONVERSION_FACTORY.create(
                                                        AutonomousDataWarehouseBackup.class);

                                com.oracle.bmc.http.internal.WithHeaders<
                                                AutonomousDataWarehouseBackup>
                                        response = responseFn.apply(rawResponse);
                                javax.ws.rs.core.MultivaluedMap<String, String> headers =
                                        response.getHeaders();

                                CreateAutonomousDataWarehouseBackupResponse.Builder builder =
                                        CreateAutonomousDataWarehouseBackupResponse.builder();

                                builder.autonomousDataWarehouseBackup(response.getItem());

                                com.google.common.base.Optional<java.util.List<String>> etagHeader =
                                        com.oracle.bmc.http.internal.HeaderUtils.get(
                                                headers, "etag");
                                if (etagHeader.isPresent()) {
                                    builder.etag(
                                            com.oracle.bmc.http.internal.HeaderUtils.toValue(
                                                    "etag", etagHeader.get().get(0), String.class));
                                }

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

                                CreateAutonomousDataWarehouseBackupResponse responseWrapper =
                                        builder.build();

                                return responseWrapper;
                            }
                        };
        return transformer;
    }
}
