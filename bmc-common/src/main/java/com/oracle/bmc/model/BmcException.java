/**
 * Copyright (c) 2016, 2019, Oracle and/or its affiliates. All rights reserved.
 */
package com.oracle.bmc.model;

import lombok.Getter;

public class BmcException extends RuntimeException {
    /**
     * Name of the header that contains the request id.
     */
    public static final String OPC_REQUEST_ID_HEADER = "opc-request-id";

    /**
     * The HTTP status code.
     */
    @Getter private final int statusCode;

    /**
     * Service specific code returned.  Null if the client timed out or failed to get a response from the service.
     */
    @Getter private final String serviceCode;

    /**
     * Flag to indicate that the request timed out.  Status code and service code should not be used if this is true.
     */
    @Getter private final boolean timeout;

    /**
     * The opc-request-id header returned by the service that should be provided for support requests.
     *
     * If the client timed out or failed to get a response from the service, then this is the outbound request id,
     * i.e. either the value for the {@link BmcException#OPC_REQUEST_ID_HEADER} header set by the caller in an
     * invocation callback, or otherwise a value automatically generated by the SDK.
     */
    @Getter private final String opcRequestId;

    @Getter private final boolean isClientSide;

    public BmcException(
            int statusCode,
            String serviceCode,
            String message,
            String opcRequestId,
            Throwable cause) {
        super(message, cause);
        this.statusCode = statusCode;
        this.serviceCode = serviceCode;
        this.opcRequestId = opcRequestId;
        this.timeout = false;
        this.isClientSide = false;
    }

    public BmcException(int statusCode, String serviceCode, String message, String opcRequestId) {
        super(message);
        this.statusCode = statusCode;
        this.serviceCode = serviceCode;
        this.opcRequestId = opcRequestId;
        this.timeout = false;
        this.isClientSide = false;
    }

    public BmcException(boolean timeout, String message, Throwable cause, String opcRequestId) {
        super(message, cause);
        this.statusCode = -1;
        this.serviceCode = null;
        this.opcRequestId = opcRequestId;
        this.timeout = timeout;
        this.isClientSide = true;
    }

    @Override
    public String getMessage() {
        String requestId =
                this.opcRequestId != null
                        ? " ("
                                + (isClientSide ? "outbound " : "")
                                + "opc-request-id: "
                                + this.opcRequestId
                                + ")"
                        : "";
        return String.format(
                "(%s, %s, %s) %s%s",
                statusCode,
                serviceCode,
                timeout,
                super.getMessage(),
                requestId);
    }
}
