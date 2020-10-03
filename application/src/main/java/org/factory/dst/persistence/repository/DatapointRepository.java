package org.factory.dst.persistence.repository;

import org.factory.dst.persistence.entity.Datapoint;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Datapoint repository.
 */
public interface DatapointRepository extends JpaRepository<Datapoint, Long> {
}
