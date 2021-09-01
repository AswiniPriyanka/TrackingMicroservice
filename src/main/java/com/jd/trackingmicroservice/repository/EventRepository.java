package com.jd.trackingmicroservice.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jd.trackingmicroservice.entity.Event;

public interface EventRepository extends JpaRepository<Event, UUID>  {

}
