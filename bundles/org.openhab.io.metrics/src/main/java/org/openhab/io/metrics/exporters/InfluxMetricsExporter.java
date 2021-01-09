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
package org.openhab.io.metrics.exporters;

import java.time.Duration;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.io.metrics.MetricsConfiguration;
import org.openhab.io.metrics.MetricsExporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.micrometer.core.instrument.Clock;
import io.micrometer.core.instrument.composite.CompositeMeterRegistry;
import io.micrometer.influx.InfluxConfig;
import io.micrometer.influx.InfluxMeterRegistry;

/**
 * The {@link InfluxMetricsExporter} class implements a MetricsExporter for InfluxDB
 *
 * @author Robert Bach - Initial contribution
 */
@NonNullByDefault
public class InfluxMetricsExporter extends MetricsExporter {

    private final Logger logger = LoggerFactory.getLogger(InfluxMetricsExporter.class);
    private @Nullable InfluxMeterRegistry influxMeterRegistry = null;
    private @Nullable CompositeMeterRegistry meterRegistry = null;

    @Override
    public void start(CompositeMeterRegistry meterRegistry, MetricsConfiguration metricsConfiguration) {
        influxMeterRegistry = new InfluxMeterRegistry(getInfluxConfig(metricsConfiguration), Clock.SYSTEM);
        meterRegistry.add(influxMeterRegistry);
    }

    @Override
    public void shutdown() {
        if (influxMeterRegistry != null) {
            influxMeterRegistry.stop();
        }
        if (meterRegistry != null) {
            meterRegistry.remove(influxMeterRegistry);
            this.meterRegistry = null;
        }
        this.influxMeterRegistry = null;
    }

    private InfluxConfig getInfluxConfig(MetricsConfiguration metricsConfiguration) {
        return new InfluxConfig() {
            @Override
            public Duration step() {
                return Duration.ofSeconds(metricsConfiguration.influxUpdateIntervaleInSeconds);
            }

            @Override
            public String uri() {
                return metricsConfiguration.influxURL;
            }

            @Override
            public String db() {
                return metricsConfiguration.influxDB;
            }

            @Override
            public String userName() {
                return metricsConfiguration.influxUsername;
            }

            @Override
            public String password() {
                return metricsConfiguration.influxPassword;
            }

            @Override
            @io.micrometer.core.lang.Nullable
            @Nullable
            public String get(@Nullable String k) {
                return null; // accept the rest of the defaults
            }
        };
    }

    @Override
    protected boolean isEnabled(MetricsConfiguration config) {
        return config.influxMetricsEnabled;
    }
}
