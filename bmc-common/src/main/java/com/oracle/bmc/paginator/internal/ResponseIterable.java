/**
 * Copyright (c) 2016, 2019, Oracle and/or its affiliates. All rights reserved.
 */
package com.oracle.bmc.paginator.internal;

import com.google.common.base.Function;
import com.google.common.base.Supplier;

import java.util.Iterator;

/**
 * An iterable which can be used to iterate over responses returned from calling a list operation. Since
 * this deals in responses, the returned response objects will contain a collection of results. This iterable
 * will handle calling the service to retrieve more results when required.
 *
 * @param <REQUESTBUILDER> the type of a builder which can produce requests for a list operation
 * @param <REQUEST> the type of a request to a list operation. This type must match the type produced
 * by REQUESTBUILDER
 * @param <RESPONSE> the type of the response from a list operation
 */
public class ResponseIterable<REQUESTBUILDER, REQUEST, RESPONSE> implements Iterable<RESPONSE> {
    private final Supplier<REQUESTBUILDER> requestBuilderSupplier;
    private final Function<RESPONSE, String> nextPageTokenRetrievalFunction;
    private final Function<REQUEST, RESPONSE> pageRetrievalFunction;
    private final Function<RequestBuilderAndToken<REQUESTBUILDER>, REQUEST> requestBuilderFunction;

    /**
     * Creates a new iterable.
     *
     * @param requestBuilderSupplier a supplier which can called to produce a builder object for requests
     * @param nextPageTokenRetrievalFunction a function which can extract the next page token from a
     * response produced by a list operation
     * @param requestBuilderFunction a function which can build a request for a list operation based on
     * a builder object and a pagination token to use
     * @param pageRetrievalFunction a function which will call a list operation with a request and return
     * the response of the call
     */
    public ResponseIterable(
            final Supplier<REQUESTBUILDER> requestBuilderSupplier,
            final Function<RESPONSE, String> nextPageTokenRetrievalFunction,
            final Function<RequestBuilderAndToken<REQUESTBUILDER>, REQUEST> requestBuilderFunction,
            final Function<REQUEST, RESPONSE> pageRetrievalFunction) {

        this.requestBuilderSupplier = requestBuilderSupplier;
        this.nextPageTokenRetrievalFunction = nextPageTokenRetrievalFunction;
        this.requestBuilderFunction = requestBuilderFunction;
        this.pageRetrievalFunction = pageRetrievalFunction;
    }

    @Override
    public Iterator<RESPONSE> iterator() {
        return new ResponseIterator<REQUESTBUILDER, REQUEST, RESPONSE>(
                requestBuilderSupplier.get(),
                nextPageTokenRetrievalFunction,
                requestBuilderFunction,
                pageRetrievalFunction);
    }
}
