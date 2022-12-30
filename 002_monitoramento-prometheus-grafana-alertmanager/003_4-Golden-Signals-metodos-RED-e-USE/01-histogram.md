

# histogram_quantile()

https://prometheus.io/docs/prometheus/latest/querying/functions/#histogram_quantile
<https://prometheus.io/docs/prometheus/latest/querying/functions/#histogram_quantile>

histogram_quantile(φ scalar, b instant-vector) calculates the φ-quantile (0 ≤ φ ≤ 1) from a conventional histogram or from a native histogram. (See histograms and summaries for a detailed explanation of φ-quantiles and the usage of the (conventional) histogram metric type in general.)

Note that native histograms are an experimental feature. The behavior of this function when dealing with native histograms may change in future versions of Prometheus.

The conventional float samples in b are considered the counts of observations in each bucket of one or more conventional histograms. Each float sample must have a label le where the label value denotes the inclusive upper bound of the bucket. (Float samples without such a label are silently ignored.) The other labels and the metric name are used to identify the buckets belonging to each conventional histogram. The histogram metric type automatically provides time series with the _bucket suffix and the appropriate labels.

The native histogram samples in b are treated each individually as a separate histogram to calculate the quantile from.

As long as no naming collisions arise, b may contain a mix of conventional and native histograms.

Use the rate() function to specify the time window for the quantile calculation.

Example: A histogram metric is called http_request_duration_seconds (and therefore the metric name for the buckets of a conventional histogram is http_request_duration_seconds_bucket). To calculate the 90th percentile of request durations over the last 10m, use the following expression in case http_request_duration_seconds is a conventional histogram:

histogram_quantile(0.9, rate(http_request_duration_seconds_bucket[10m]))

For a native histogram, use the following expression instead:

histogram_quantile(0.9, rate(http_request_duration_seconds[10m]))

The quantile is calculated for each label combination in http_request_duration_seconds. To aggregate, use the sum() aggregator around the rate() function. Since the le label is required by histogram_quantile() to deal with conventional histograms, it has to be included in the by clause. The following expression aggregates the 90th percentile by job for conventional histograms:

histogram_quantile(0.9, sum by (job, le) (rate(http_request_duration_seconds_bucket[10m])))

When aggregating native histograms, the expression simplifies to:

histogram_quantile(0.9, sum by (job) (rate(http_request_duration_seconds[10m])))

To aggregate all conventional histograms, specify only the le label:

histogram_quantile(0.9, sum by (le) (rate(http_request_duration_seconds_bucket[10m])))

With native histograms, aggregating everything works as usual without any by clause:

histogram_quantile(0.9, sum(rate(http_request_duration_seconds[10m])))

The histogram_quantile() function interpolates quantile values by assuming a linear distribution within a bucket.

If b has 0 observations, NaN is returned. For φ < 0, -Inf is returned. For φ > 1, +Inf is returned. For φ = NaN, NaN is returned.

The following is only relevant for conventional histograms: If b contains fewer than two buckets, NaN is returned. The highest bucket must have an upper bound of +Inf. (Otherwise, NaN is returned.) If a quantile is located in the highest bucket, the upper bound of the second highest bucket is returned. A lower limit of the lowest bucket is assumed to be 0 if the upper bound of that bucket is greater than 0. In that case, the usual linear interpolation is applied within that bucket. Otherwise, the upper bound of the lowest bucket is returned for quantiles located in the lowest bucket. 