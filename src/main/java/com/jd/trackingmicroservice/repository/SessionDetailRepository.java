package com.jd.trackingmicroservice.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jd.trackingmicroservice.entity.SessionDetail;

public interface SessionDetailRepository extends JpaRepository<SessionDetail, UUID> {

	SessionDetail findBySessionId(UUID sessiondId);
	
}
