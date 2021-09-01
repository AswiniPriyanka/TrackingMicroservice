package com.jd.trackingmicroservice.dto;

import java.util.List;
import java.util.UUID;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class EventListDTO {

	@NotNull(message = "{validation.message.sessionId.notEmpty}")
	private UUID sessionId;
	@NotEmpty(message = "{validation.eventList.notempty}")
	private List<@Valid EventDTO> events;

}
