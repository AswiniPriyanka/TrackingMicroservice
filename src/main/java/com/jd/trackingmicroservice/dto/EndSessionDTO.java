package com.jd.trackingmicroservice.dto;

import java.time.LocalDateTime;
import java.util.UUID;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EndSessionDTO {

	@NotNull(message = "{validation.message.session.id}")
	private UUID sessionId;
	@NotNull(message = "{validation.message.endsession.endtime}")
	private LocalDateTime logOffTime;

}
