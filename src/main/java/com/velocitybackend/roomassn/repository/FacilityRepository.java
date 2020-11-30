package com.velocitybackend.roomassn.repository;

import com.velocitybackend.roomassn.domain.Facility;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the Facility entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FacilityRepository extends JpaRepository<Facility, Long> {

    @Query("select facility from Facility facility where facility.user.login = ?#{principal.username}")
    List<Facility> findByUserIsCurrentUser();
}
