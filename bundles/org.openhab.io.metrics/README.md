# Metrics service

The metrics service provides 

* an additional REST endpoint to retrieve openHAB core metrics from. This can be used as scrape target for pull-based monitoring systems like [Prometheus](https://prometheus.io/).
* optionally configurable services to export openHAB core metrics to push-based monitoring systems like [InfluxDB](https://www.influxdata.com/). 

## Precondition

The openHAB core metrics must be available - a recent enough openHAB version must be used.
https://github.com/openhab/openhab-core/pull/2133

## Provided metrics

Currently the following metrics are provided: 

- openHAB events counts (per topic)
- openHAB bundle states
- openHAB thing states
- openHAB rule runs (per rule)
- openHAB threadpool stats (per scheduler)
- JVM stats including metrics of
    - class loader
    - memory
    - GarbageCollector
    - OS (system load, CPU)
    - thread metrics

## Configuration

The configuration for the metrics service is available in the openHAB UI under Settings | Other Services | Metrics service.
Support for pull-based monitoring systems (e. g. Prometheus) is always enabled, since it doesn't imply any significant overhead when not used.
Support for push-based monitoring systems (e. g. InfluxDB) have to be enabled seperately. 

The following configuration parameters can be set:
<table>
  <thead>
    <tr><th>Config param</th><th>Description</th><th>Default value</th></tr>
  </thead>
  <tbody>
    <tr><td>influxMetricsEnabled</td><td>Enable the Influx (www.influxdata.com) metrics. Further configuration of the InfluxDB instance necessary.</td><td>false</td></tr>
  </tbody>
</table>

Refer to the corresponding monitoring system sections for monitoring system specific configuration parameters.  

### Supported monitoring systems

For a start, the following formats are supported:

### Prometheus

 Once the IO addon is installed and , the Prometheus endpoint will be available under:
_<openhab_host>:8080/metrics/prometheus_ 

Refer to the [Prometheus](https://prometheus.io/) documentation on how to setup a Prometheus instance and add a scrape configuration. A typical scrape config could look like this (excerpt from `/etc/prometheus/prometheus.yml`):

````shell
scrape_configs:
  - job_name: 'openhab'
    scrape_interval: 1m
    scheme: http
    metrics_path: /metrics/prometheus
    static_configs:
    - targets:
      - 'openhab.local:8080'
````

Replace `openhab.local` by the openhab host.   

#### Available configuration parameters

There are no Prometheus specific configuration paramters.

### InfluxDB

The InfluxDB exporter service will start as soon as the _influxMetricsEnabled_ configuration parameter is set to true.

#### Available configuration parameters

<table>
  <thead>
    <tr><th>Config param</th><th>Description</th><th>Default value</th></tr>
  </thead>
  <tbody>
    <tr><td>influxURL</td><td>The URL of the InfluxDB instance. Defaults to http://localhost:8086</td><td>http://localhost:8086</td></tr>
    <tr><td>influxDB</td><td>The name of the database to use. Defaults to "openhab".</td><td>openhab</td></tr>
    <tr><td>influxUsername</td><td>InfluxDB user name</td><td>n/a</td></tr>
    <tr><td>influxPassword</td><td>The InfluxDB password (no default).</td><td>n/a</td></tr>
    <tr><td>influxUpdateIntervalInSeconds</td><td>Controls how often metrics are exported to InfluxDB (in seconds). Defaults to 300</td><td>300</td></tr>
  </tbody>
</table>

## Additional metric formats

The metrics service was implemented using [Micrometer](https://micrometer.io), which supports a number of [monitoring systems](https://micrometer.io/docs) 
It should be possible to add any of these, especially the ones using a pull mechanism ("scraping") like Prometheus does.     

## Grafana

You can now visualize the results in Grafana. Micrometer provides a public [Grafana dashboard here](https://grafana.com/grafana/dashboards/4701). 
I adapted it a little bit to include the openHAB metrics. You can download it here [Dashboard](docs/dashboard.json). 
This has been tested with Prometheus - for other monitoring systems adaptions to the dashboard might be necessary.  

Here are some screenshots: 

![Grafana (1)](docs/grafana-1.png)
![Grafana (2)](docs/grafana-2.png)
![Grafana (3)](docs/grafana-3.png)