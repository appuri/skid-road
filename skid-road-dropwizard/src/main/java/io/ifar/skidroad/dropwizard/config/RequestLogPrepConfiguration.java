package io.ifar.skidroad.dropwizard.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.DecimalMin;

/**
 *
 */
public class RequestLogPrepConfiguration {

    @JsonProperty("master_key")
    //optional; only needed if using encryption
    private String masterKey;

    /**
     * Fixed master IV no longer used during encryption. May optionally be supplied
     * for decrypting legacy data.
     */
    @JsonProperty("master_iv")
    private String masterIV;

    @JsonProperty("report_unhealthy_at_queue_depth")
    @DecimalMin(value="1")
    private int reportUnhealthyAtQueueDepth = 10;

    @Range(min = 1)
    @JsonProperty("retry_interval_seconds")
    private int retryIntervalSeconds= 300;

    @Range(min = 1)
    @JsonProperty("max_concurrency")
    private Integer maxConcurrency = 5; //depends on network bandwidth, not CPUs

    public String getMasterIV() {
        return masterIV;
    }

    public String getMasterKey() {
        return masterKey;
    }

    public int getReportUnhealthyAtQueueDepth() {
        return reportUnhealthyAtQueueDepth;
    }

    public int getRetryIntervalSeconds() {
        return retryIntervalSeconds;
    }

    public Integer getMaxConcurrency() {
        return maxConcurrency;
    }
}