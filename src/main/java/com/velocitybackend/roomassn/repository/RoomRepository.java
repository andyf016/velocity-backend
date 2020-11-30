package com.velocitybackend.roomassn.repository;

import com.velocitybackend.roomassn.domain.Room;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Room entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
}
