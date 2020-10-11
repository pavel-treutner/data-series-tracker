package org.factory.dst.persistence.repository;

import org.factory.dst.persistence.entity.Datapoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Datapoint repository.
 */
public interface DatapointRepository extends JpaRepository<Datapoint, Long> {

    /**
     * Find a single item assigned to the device.
     *
     * @param device Device id.
     * @return Optional.
     */
    Optional<Datapoint> findFirstByDevice(String device);

    /**
     * Find items assigned to the device.
     *
     * @param device Device id.
     * @return List of items.
     */
    @Transactional(readOnly = true)
    List<Datapoint> findByDevice(String device);

    /**
     * Delete all items assigned to the device.
     *
     * @param device Device id.
     */
    @Modifying
    @Query("delete from Datapoint d where d.device = :device")
    void deleteByDevice(@Param("device") String device);

    /**
     * Find a single item assigned to the user.
     *
     * @param user User id.
     * @return Optional.
     */
    Optional<Datapoint> findFirstByUser(String user);

    /**
     * Find items assigned to the user.
     *
     * @param user User id.
     * @return List of items.
     */
    @Transactional(readOnly = true)
    List<Datapoint> findByUser(String user);

    /**
     * Delete all items assigned to the user.
     *
     * @param user User id.
     */
    @Modifying
    @Query("delete from Datapoint d where d.user = :user")
    void deleteByUser(@Param("user") String user);
}
