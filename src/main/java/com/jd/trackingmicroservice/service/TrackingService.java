package com.jd.trackingmicroservice.service;

import com.jd.trackingmicroservice.dto.EndSessionDTO;
import com.jd.trackingmicroservice.dto.EventListDTO;
import com.jd.trackingmicroservice.dto.SessionDTO;
import com.jd.trackingmicroservice.entity.SessionDetail;

public interface TrackingService {

	public SessionDetail startSession(SessionDTO sessiondto);

	public SessionDetail endSession(EndSessionDTO endSession) throws NullPointerException;

	public void addEvent(EventListDTO eventListDTO);

}
