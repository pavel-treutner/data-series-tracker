package org.factory.dst.process.dto;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.OffsetDateTime;

/**
 * Data transfer object representing a single measurement.
 */
public class DatapointDto {

    /**
     * Device regex pattern.
     */
    public static final String DEVICE_PATTERN = "^[a-zA-Z0-9]{1,50}$";

    /**
     * User regex pattern.
     */
    public static final String USER_PATTERN = "^[a-zA-Z0-9]{1,50}$";

    @NotNull
    private OffsetDateTime timestamp;

    private double value;

    @NotNull
    @Pattern(regexp = DEVICE_PATTERN)
    private String device;

    @NotNull
    @Pattern(regexp = USER_PATTERN)
    private String user;

    public DatapointDto() {
    }

    public DatapointDto(OffsetDateTime timestamp, double value, String device, String user) {
        this.timestamp = timestamp;
        this.value = value;
        this.device = device;
        this.user = user;
    }

    public OffsetDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(OffsetDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        DatapointDto datapointDto = (DatapointDto) o;

        return new EqualsBuilder()
                .append(value, datapointDto.value)
                .append(timestamp, datapointDto.timestamp)
                .append(device, datapointDto.device)
                .append(user, datapointDto.user)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(timestamp)
                .append(value)
                .append(device)
                .append(user)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("timestamp", timestamp)
                .append("value", value)
                .append("device", device)
                .append("user", user)
                .toString();
    }
}
