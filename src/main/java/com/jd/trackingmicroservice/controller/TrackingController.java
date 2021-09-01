package com.jd.trackingmicroservice.controller;

import java.util.UUID;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.jd.trackingmicroservice.dto.EndSessionDTO;
import com.jd.trackingmicroservice.dto.EventListDTO;
import com.jd.trackingmicroservice.dto.SessionDTO;
import com.jd.trackingmicroservice.entity.SessionDetail;
import com.jd.trackingmicroservice.service.TrackingService;

/**
 * This controller's purpose is to expose API's to Save User Or Machine Level
 * Activity for tracing Purpose
 * 
 * @author Aswini Priyanka M
 *
 */
@RestController
public class TrackingController {

	@Autowired
	private TrackingService trackingService;

	/**
	 * This Api endpoint is used to save login details of application and to return
	 * a sessionId in the cookie for further tracking
	 * 
	 * @param sessiondto
	 * @param response
	 * @return User Or Machine Session details
	 */
	@PostMapping("/startSession")
	public ResponseEntity<SessionDetail> startSession(@Valid @RequestBody SessionDTO sessiondto, HttpServletResponse response) {

		String sessionId = UUID.randomUUID().toString();
		sessiondto.setSessionId(UUID.fromString(sessionId));
		// Session Id is created and sent in response as a Cookie for further tracking.
		HttpHeaders headers = new HttpHeaders();
		headers.add("Set-Cookie", "sessionId=" + sessionId);

		SessionDetail sessionDetail = trackingService.startSession(sessiondto);
		return new ResponseEntity<SessionDetail>(sessionDetail, headers, HttpStatus.CREATED);

	}

	/**
	 * This API end point is exposed to save User Or Machine's LogOff details
	 * 
	 * @param endSession
	 * @return Http OK Status on Save of Detail
	 * @throws NullPointerException 
	 */
	@PostMapping("/endSession")
	public ResponseEntity<SessionDetail> endSession(@Valid @RequestBody EndSessionDTO endSession) throws NullPointerException {
		SessionDetail sessionDetail =trackingService.endSession(endSession);
		return new ResponseEntity<SessionDetail>(sessionDetail, HttpStatus.OK);
	}

	/**
	 * This API is exposed to save list of events
	 * 
	 * @param eventListDTO
	 */
	@PostMapping("/addEvent")
	public ResponseEntity<String> addEvent(@Valid @RequestBody EventListDTO eventListDTO) {
		trackingService.addEvent(eventListDTO);
		return new ResponseEntity<String>(HttpStatus.CREATED);
	}

}
