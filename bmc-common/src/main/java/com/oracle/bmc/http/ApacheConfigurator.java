/**
 * Copyright (c) 2016, 2021, Oracle and/or its affiliates.  All rights reserved.
 * This software is dual-licensed to you under the Universal Permissive License (UPL) 1.0 as shown at https://oss.oracle.com/licenses/upl or Apache License 2.0 as shown at http://www.apache.org/licenses/LICENSE-2.0. You may choose either license.
 */
package com.oracle.bmc.http;

import com.oracle.bmc.http.internal.ContentLengthFilter;
import com.oracle.bmc.util.JavaRuntimeUtils;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.glassfish.jersey.SslConfigurator;
import org.glassfish.jersey.apache.connector.ApacheConnectorProvider;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.client.RequestEntityProcessing;

import javax.net.ssl.SSLContext;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import java.util.LinkedList;
import java.util.List;

/**
 * A {@code ClientConfigurator} implementation that uses the Apache HTTP Connector Provider for the Jersey Client
 * configuration.  This enables support for connection pooling in addition to per-client HTTP(S) proxy support.
 *
 * Note: Use of the ApacheConnectorProvider will buffer requests into memory and can impact memory utilization within
 * your application. To avoid buffering of requests in memory, please use ApacheConfigurator.NonBuffering
 * Apache Configurator by default configures a connection pool with default values for defaultMaxConnectionsPerRoute
 * and maximum open connections. Please make sure to close all InputStreams obtained from the response object by
 * calling close on the stream object. For example - GetObjectReponse.getInputStream().close or
 * try-with-resources. Otherwise, the connection is not released from the pool and results in hanging for
 * an indefinite time.
 */
@Slf4j
public class ApacheConfigurator
        implements ClientConfigurator, SetsClientBuilderProperties, HasApacheConnectorProperties {
    /** The list of {@code ClientConfigDecorator}s to support the ability to decorate {@code ClientConfig} */
    protected final List<ClientConfigDecorator> clientConfigDecorators = new LinkedList<>();

    /** Getter for ApacheConnectorProperties for the Apache Connection **/
    @Getter protected ApacheConnectorProperties apacheConnectorProperties;

    /** Creates a new {@code ApacheConfigurator} object. */
    public ApacheConfigurator() {
        this.apacheConnectorProperties = ApacheConnectorProperties.builder().build();
    }

    /**
     * Creates a new {@code ApacheConfigurator} and registers the list of provided {@code ClientConfigDecorator}s.
     *
     * @param clientConfigDecorators the list of client configuration decorators
     */
    public ApacheConfigurator(final List<ClientConfigDecorator> clientConfigDecorators) {
        this.apacheConnectorProperties = ApacheConnectorProperties.builder().build();
        this.clientConfigDecorators.addAll(clientConfigDecorators);
    }

    /** Creates a new {@code ApacheConfigurator} object and sets the {@code ApacheConnectorProperties} */
    public ApacheConfigurator(final @NonNull ApacheConnectorProperties apacheConnectorProperties) {
        this.apacheConnectorProperties = apacheConnectorProperties;
    }

    /** Creates a new {@code ApacheConfigurator} object and sets the {@code ApacheConnectorProperties}  and registers the list of provided {@code ClientConfigDecorator}s*/
    public ApacheConfigurator(
            final @NonNull ApacheConnectorProperties apacheConnectorProperties,
            final List<ClientConfigDecorator> clientConfigDecorators) {
        this.apacheConnectorProperties = apacheConnectorProperties;
        this.clientConfigDecorators.addAll(clientConfigDecorators);
    }

    @Override
    public void customizeBuilder(ClientBuilder builder) {
        setConnectorProvider(builder);
        setSslContext(builder);
    }

    @Override
    public void customizeClient(Client client) {
        // Use buffered processing to get better error messages on POST and PUT
        // but the downside is that this will buffer large uploads in memory.
        client.property(
                ClientProperties.REQUEST_ENTITY_PROCESSING, RequestEntityProcessing.BUFFERED);

        // Required for calls to only allow Apache to set the content-length header.
        // Otherwise, apache will throw an exception if it already exists. For example, such a case includes
        // ObjectStorage where the content-length is explicitly set in the header based on the value in the request.
        client.register(new ContentLengthFilter());
    }

    public void setConnectorProvider(ClientBuilder builder) {
        LOG.info("Setting connector provider to ApacheConnectorProvider");

        final ClientConfig clientConfig = new ClientConfig();

        clientConfig.connectorProvider(new ApacheConnectorProvider());

        // Apache Configurator by default configures a connection pool with defaultMaxConnectionsPerRoute and
        // maximum open connections. Please make sure to close all InputStreams obtained from the response object by
        // calling close on the stream object. For example - GetObjectReponse.getInputStream().close or
        // try-with-resources. Otherwise, the connection is not released from the pool and results in hanging for
        // an indefinite time

        if (this.apacheConnectorProperties != null) {
            ApacheConnectorPropertiesClientConfigDecorator defaultClientConfig =
                    new ApacheConnectorPropertiesClientConfigDecorator(
                            this.apacheConnectorProperties);
            defaultClientConfig.customizeClientConfig(clientConfig);
        }
        // Decorate config with any configured client config decorators
        for (ClientConfigDecorator clientConfigDecorator : clientConfigDecorators) {
            clientConfigDecorator.customizeClientConfig(clientConfig);
        }

        builder.withConfig(clientConfig);
    }

    /**
     * Sets the SSL context on the builder.
     * <p>
     * Separate so subclasses can call if desired.
     *
     * @param builder
     *            The client builder to use.
     */
    public void setSslContext(ClientBuilder builder) {
        JavaRuntimeUtils.JreVersion version = JavaRuntimeUtils.getRuntimeVersion();
        if (version == JavaRuntimeUtils.JreVersion.Java_7) {
            LOG.info("Running on 1.7 VM, manually setting security protocol to TLSv1.2");
            SSLContext sslContext =
                    SslConfigurator.newInstance(true)
                            .securityProtocol("TLSv1.2")
                            .createSSLContext();
            builder.sslContext(sslContext);
        } else if (version == JavaRuntimeUtils.JreVersion.Unsupported) {
            LOG.error("Using an unsupported runtime only 1.7+ is supported");
        } else if (version == JavaRuntimeUtils.JreVersion.Unknown) {
            LOG.warn("Using an unknown runtime, calls may not work");
        }
    }

    public static class NonBuffering extends ApacheConfigurator
            implements HasApacheConnectorProperties {

        public NonBuffering() {
            super();
        }

        public NonBuffering(final List<ClientConfigDecorator> clientConfigDecorators) {
            this.apacheConnectorProperties = ApacheConnectorProperties.builder().build();
            this.clientConfigDecorators.addAll(clientConfigDecorators);
        }

        public NonBuffering(final @NonNull ApacheConnectorProperties apacheConnectorProperties) {
            this.apacheConnectorProperties = apacheConnectorProperties;
        }

        public NonBuffering(
                final @NonNull ApacheConnectorProperties apacheConnectorProperties,
                final List<ClientConfigDecorator> clientConfigDecorators) {
            this.apacheConnectorProperties = apacheConnectorProperties;
            this.clientConfigDecorators.addAll(clientConfigDecorators);
        }

        @Override
        public void customizeClient(Client client) {
            super.customizeClient(client);
        }

        @Override
        public void setConnectorProvider(ClientBuilder builder) {
            super.setConnectorProvider(builder);
        }
    }
}
