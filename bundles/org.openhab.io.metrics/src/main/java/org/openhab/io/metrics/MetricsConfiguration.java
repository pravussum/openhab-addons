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

/**
 * The {@link MetricsConfiguration} class holds the configuration for the metrics service
 *
 * @author Robert Bach - Initial contribution
 */
public class MetricsConfiguration {
    public boolean influxMetricsEnabled = false;
    public String influxURL = "http://localhost:8086";
    public String influxDB = "openhab";
    public String influxPassword = null;
    public String influxUsername = null;
    public Integer influxUpdateIntervalInSeconds = 300;

    @Override
    public String toString() {
        return "MetricsConfiguration{" + "influxMetricsEnabled=" + influxMetricsEnabled + ", influxURL='" + influxURL
                + '\'' + ", influxDB='" + influxDB + '\'' + ", influxPassword='" + influxPassword + '\''
                + ", influxUsername='" + influxUsername + '\'' + ", influxUpdateIntervalInSeconds="
                + influxUpdateIntervalInSeconds + '}';
    }
}
