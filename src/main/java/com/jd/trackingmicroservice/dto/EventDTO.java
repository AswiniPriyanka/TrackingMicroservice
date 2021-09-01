package com.jd.trackingmicroservice.dto;

import java.time.LocalDateTime;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EventDTO {
	
	@NotNull(message = "{validation.eventTime.notEmpty}")
	private LocalDateTime eventAt;
	@NotBlank(message = "{validation.eventType.notEmpty}")
	private String eventType;
	private String payload;

}
