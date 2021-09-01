package com.jd.trackingmicroservice.service.impl;

import java.util.ArrayList;
import java.util.List;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.jd.trackingmicroservice.dto.EndSessionDTO;
import com.jd.trackingmicroservice.dto.EventDTO;
import com.jd.trackingmicroservice.dto.EventListDTO;
import com.jd.trackingmicroservice.dto.SessionDTO;
import com.jd.trackingmicroservice.entity.Event;
import com.jd.trackingmicroservice.entity.SessionDetail;
import com.jd.trackingmicroservice.repository.EventRepository;
import com.jd.trackingmicroservice.repository.SessionDetailRepository;
import com.jd.trackingmicroservice.service.TrackingService;

/**
 * 
 * @author Aswini Priyanka
 *
 */
@Service
public class TrackingServiceImpl implements TrackingService {

	private static final Logger LOG = LoggerFactory.getLogger(TrackingServiceImpl.class);

	@Value("${spring.jpa.properties.hibernate.jdbc.batch_size}")
	private int batchSize;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private SessionDetailRepository sessionDetailRepository;

	@Autowired
	private EventRepository eventRepository;

	/**
	 * This method is used to Update the AppUser Table with the log Off time of a
	 * particular session.
	 * 
	 * @param endSession
	 * @throws NullPointerException 
	 */
	@Override
	public SessionDetail endSession(EndSessionDTO endSession) throws NullPointerException {
		// AppUser Details are fetched by passing the session Id for updating purpose.
		try {
			SessionDetail sessionDetail = sessionDetailRepository.findBySessionId(endSession.getSessionId());
			sessionDetail.setEndAt(endSession.getLogOffTime());
			return sessionDetailRepository.save(sessionDetail);
		} 
		catch (NullPointerException ex) {
			throw new NullPointerException("Session Id is not a valid entry");
		}
		catch (Exception ex) {
			LOG.info("Update of log off time failed with exception for object " + endSession.toString());
			throw ex;
		}
	}

	/**
	 * This method is used to add List Events per Session.
	 * 
	 * @param eventListDTO
	 */
	@Override
	public void addEvent(EventListDTO eventListDTO) {
		List<Event> events = new ArrayList<Event>();
		int counter = 0;
		int eventsSize = eventListDTO.getEvents().size();
		try {
			// List of events are sent for save in batches based on the batch size set in
			// application.properties
			for (EventDTO eventDetail : eventListDTO.getEvents()) {
				Event event = modelMapper.map(eventDetail, Event.class);
				event.setSessionId(eventListDTO.getSessionId());
				events.add(event);
				counter++;
				if (counter % batchSize == 0|| eventsSize == counter) {
					eventRepository.saveAll(events);
					events.clear();
				}
			}
		} catch (Exception ex) {
			LOG.info("List of Events failed with exception " + events.toString());
			throw ex;
		}

	}

	/**
	 * This method is used to save the User or Machine details for a particular
	 * session.
	 * 
	 * @param sessiondto
	 */
	@Override
	public SessionDetail startSession(SessionDTO sessiondto) {
		try {
			SessionDetail sessionDetail = modelMapper.map(sessiondto, SessionDetail.class);
			return sessionDetailRepository.save(sessionDetail);
		} catch (Exception ex) {
			LOG.info("Creation of AppUser failed with Exception for object " + sessiondto.toString());
			throw ex;
		}

	}

}
