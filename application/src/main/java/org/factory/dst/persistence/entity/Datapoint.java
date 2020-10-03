package org.factory.dst.persistence.entity;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

/**
 * Entity representing a single measurement.
 */
@Entity
public class Datapoint {

    @Id
    @GeneratedValue
    private Long id;

    @Column
    private LocalDateTime timestamp;

    @Column
    private double value;

    @Column
    private String device;

    @Column
    private String user;

    public Datapoint(Long id, LocalDateTime timestamp, double value, String device, String user) {
        this.id = id;
        this.timestamp = timestamp;
        this.value = value;
        this.device = device;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
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

        Datapoint datapoint = (Datapoint) o;

        return new EqualsBuilder()
                .append(value, datapoint.value)
                .append(id, datapoint.id)
                .append(timestamp, datapoint.timestamp)
                .append(device, datapoint.device)
                .append(user, datapoint.user)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(timestamp)
                .append(value)
                .append(device)
                .append(user)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("timestamp", timestamp)
                .append("value", value)
                .append("device", device)
                .append("user", user)
                .toString();
    }
}
