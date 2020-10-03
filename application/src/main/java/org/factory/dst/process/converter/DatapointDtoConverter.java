package org.factory.dst.process.converter;

import org.factory.dst.persistence.entity.Datapoint;
import org.factory.dst.process.dto.DatapointDto;

import java.time.ZoneOffset;

/**
 * Converter between DatapointDto and Datapoint classes.
 */
public final class DatapointDtoConverter {

    private DatapointDtoConverter() {
    }

    /**
     * Convert from DTO object.
     *
     * @param source Source.
     * @return Converted instance.
     */
    public static Datapoint fromDto(DatapointDto source) {
        if (source == null) {
            return null;
        }

        return new Datapoint(null, source.getTimestamp().toLocalDateTime(), source.getValue(), source.getDevice(),
                source.getUser());
    }

    /**
     * Convert to DTO object.
     *
     * @param source Source.
     * @return Converted instance.
     */
    public static DatapointDto toDto(Datapoint source) {
        if (source == null) {
            return null;
        }

        return new DatapointDto(source.getTimestamp().atOffset(ZoneOffset.UTC), source.getValue(), source.getDevice(),
                source.getUser());
    }
}
