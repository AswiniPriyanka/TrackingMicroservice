package com.jd.trackingmicroservice.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.jd.trackingmicroservice.dto.EndSessionDTO;
import com.jd.trackingmicroservice.dto.SessionDTO;
import com.jd.trackingmicroservice.entity.SessionDetail;
import com.jd.trackingmicroservice.service.TrackingService;

@RunWith(SpringRunner.class)
@WebMvcTest(TrackingController.class)
class TrackingControllerTest {

	@Autowired
	private MockMvc mvc;
	@MockBean
	private TrackingService trackingService;
	
	private static String sessionId;

	@Test
	void startSessionPositiveTest() throws Exception {
		String exampleSessionDetailJSON = "{\n" + "    \"userId\": \"dd722807-62c6-4e51-966a-a3a92f6a5169\", \n"
				+ "    \"machineId\": \"\",\n"
				+ "    \"startAt\": \"2016-01-25T21:34:55\",\n"
				+ "    \"orgId\": \"725ec8bd-7e7f-40a3-b6c0-ea6e1f43c979\" \n" + "}";
		// trackingService.startSession to respond back with mockSessionDetail
		Mockito.when(trackingService.startSession(Mockito.any(SessionDTO.class))).thenReturn(buildStartSessionDetail());

		// Send session detail as body to /startSession
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/startSession").accept(MediaType.APPLICATION_JSON)
				.content(exampleSessionDetailJSON).contentType(MediaType.APPLICATION_JSON);
		
		MvcResult result = mvc.perform(requestBuilder).andReturn();

		MockHttpServletResponse response = result.getResponse();
		sessionId = response.getCookie("sessionId").getValue();
		
		assertEquals(HttpStatus.CREATED.value(), response.getStatus());
		assertNotNull(response.getCookie("sessionId"));

	}
	
	@Test
	void endSessionPositiveTest() throws Exception {
		String exampleEndSessionDetailJSON = "{\n"
				+ "    \"logOffTime\": \"2016-03-16T13:56:39.492\",\n"
				+ "    \"sessionId\": \""+sessionId+"\"\n"
				+ "}";
		// trackingService.endSession to respond back with mockSessionDetail
		Mockito.when(trackingService.endSession(Mockito.any(EndSessionDTO.class))).thenReturn(buildEndSessionDetail());

		// Send session detail as body to /endSession
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/endSession").accept(MediaType.APPLICATION_JSON)
				.content(exampleEndSessionDetailJSON).contentType(MediaType.APPLICATION_JSON);
		
		MvcResult result = mvc.perform(requestBuilder).andReturn();

		MockHttpServletResponse response = result.getResponse();
		
		assertEquals(HttpStatus.OK.value(), response.getStatus());

	}
	
	
	@Test
	void addEventPositiveTest() throws Exception {
		String exampleEndSessionDetailJSON = "{\n"
				+ "    \"sessionId\": \""+sessionId+"\",\n"
				+ "    \"events\": [\n"
				+ "        {\n"
				+ "            \"eventAt\": \"2016-03-16T13:56:39.492\",\n"
				+ "            \"eventType\": \"{event_type1}\",\n"
				+ "            \"payload\": \"Something 1\"\n"
				+ "        },\n"
				+ "         {\n"
				+ "            \"eventAt\": \"2016-03-16T13:56:39.492\",\n"
				+ "            \"eventType\": \"{event_type2}\",\n"
				+ "            \"payload\": \"Something 2\"\n"
				+ "        },\n"
				+ "         {\n"
				+ "            \"eventAt\": \"2016-03-16T13:56:39.492\",\n"
				+ "            \"eventType\": \"{event_type3}\",\n"
				+ "            \"payload\": \"Something 3\"\n"
				+ "        }\n"
				+ "       \n"
				+ "] \n"
				+ "	\n"
				+ "}";
		

		// Send session detail as body to /startSession
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/addEvent").accept(MediaType.APPLICATION_JSON)
				.content(exampleEndSessionDetailJSON).contentType(MediaType.APPLICATION_JSON);
		
		MvcResult result = mvc.perform(requestBuilder).andReturn();

		MockHttpServletResponse response = result.getResponse();
		
		assertEquals(HttpStatus.CREATED.value(), response.getStatus());

	}

	SessionDetail buildStartSessionDetail() {
		return SessionDetail.builder().startAt(null).id(UUID.fromString("4bd2cfd3-ebd7-4792-be0d-eb6163d2fe3d"))
				.userId(UUID.fromString("dd722807-62c6-4e51-966a-a3a92f6a5169")).machineId(null)
				.orgId(UUID.fromString("725ec8bd-7e7f-40a3-b6c0-ea6e1f43c979")).startAt(LocalDateTime.now())
				.sessionId(UUID.fromString("b8632108-56a8-46ba-8392-0019886b9921")).build();
	}
	
	SessionDetail buildEndSessionDetail() {
		return SessionDetail.builder().startAt(null).id(UUID.fromString("4bd2cfd3-ebd7-4792-be0d-eb6163d2fe3d"))
				.userId(UUID.fromString("dd722807-62c6-4e51-966a-a3a92f6a5169")).machineId(null)
				.orgId(UUID.fromString("725ec8bd-7e7f-40a3-b6c0-ea6e1f43c979")).startAt(LocalDateTime.now())
				.sessionId(UUID.fromString("b8632108-56a8-46ba-8392-0019886b9921"))
				.endAt(LocalDateTime.now()).
				build();
	}


}