/**
 * Copyright (c) 2010-2021 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.openhab.io.metrics;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Set;

import javax.ws.rs.core.Application;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.jaxrs.whiteboard.JaxrsWhiteboardConstants;
import org.osgi.service.jaxrs.whiteboard.propertytypes.JaxrsName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link MetricsService} class ...
 *
 * @author Robert Bach - Initial contribution
 */
@NonNullByDefault
@Component(immediate = true, service = MetricsService.class)
public class MetricsService {
    public static final String METRICS_APP_NAME = "Metrics";
    public static final String ROOT = "/metrics";
    private final Logger logger = LoggerFactory.getLogger(MetricsService.class);
    @Nullable
    private ServiceRegistration<Application> restService = null;

    @Reference
    protected @NonNullByDefault({}) MetricsRestController metrics;

    @Activate
    protected void activate() {
        MetricsRestApplication app = new MetricsRestApplication(ROOT);

        BundleContext context = FrameworkUtil.getBundle(getClass()).getBundleContext();
        restService = context.registerService(Application.class, app, getServiceProperties());
        logger.info("Metrics service available under {}.", ROOT);
    }

    @Deactivate
    protected void deactivate() {
        restService.unregister();
    }

    Dictionary<String, String> getServiceProperties() {
        Dictionary<String, String> dict = new Hashtable<>();
        dict.put(JaxrsWhiteboardConstants.JAX_RS_APPLICATION_BASE, ROOT);
        return dict;
    }

    @JaxrsName(METRICS_APP_NAME)
    private class MetricsRestApplication extends Application {

        private String root;

        MetricsRestApplication(String root) {
            this.root = root;
        }

        @NonNullByDefault({})
        @Override
        public Set<Object> getSingletons() {
            return Set.of(metrics);
        }
    }
}
