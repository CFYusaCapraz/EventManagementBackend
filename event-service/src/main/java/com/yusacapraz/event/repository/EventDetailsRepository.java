package com.yusacapraz.event.repository;

import com.yusacapraz.event.model.EventDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EventDetailsRepository extends JpaRepository<EventDetails, UUID> {
}
