package com.jd.trackingmicroservice.dto;

import java.time.LocalDateTime;
import java.util.UUID;
import javax.validation.constraints.NotNull;
import com.jd.trackingmicroservice.validator.RequestAnnotation;
import lombok.Data;

@Data
@RequestAnnotation(message = "{validation.userOrMachine.isPresent}")
public class SessionDTO {

	private UUID userId;
	private UUID machineId;
	private UUID orgId;
	private UUID sessionId;
	@NotNull(message = "Start time can't be null")
	private LocalDateTime startAt;

}
