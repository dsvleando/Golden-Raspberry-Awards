package br.com.outsera.gra.presentation.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;
import java.util.function.Function;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.outsera.gra.application.port.inbound.StudioUseCase;
import br.com.outsera.gra.domain.model.Studio;
import br.com.outsera.gra.presentation.dto.request.StudioRequest;
import br.com.outsera.gra.presentation.dto.response.PageResponse;
import br.com.outsera.gra.presentation.dto.response.StudioResponse;
import br.com.outsera.gra.presentation.mapper.PageMapper;
import br.com.outsera.gra.presentation.mapper.StudioMapper;

@WebMvcTest(controllers = StudioController.class)
class StudioControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private StudioUseCase studioUseCase;

	@MockBean
	private StudioMapper studioMapper;

	@MockBean
	private PageMapper pageMapper;

	@Test
	void createStudio_shouldReturn201() throws Exception {
		StudioRequest req = new StudioRequest();
		req.setName("Warner Bros.");
		Studio domain = new Studio("Warner Bros.");
		StudioResponse resp = new StudioResponse();

		given(studioMapper.toDomain(any(StudioRequest.class))).willReturn(domain);
		given(studioUseCase.createStudio(any(Studio.class))).willReturn(domain);
		given(studioMapper.toResponse(any(Studio.class))).willReturn(resp);

		mockMvc.perform(post("/api/studios").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(req))).andExpect(status().isCreated());
	}

	@Test
	void getStudioById_shouldReturn200_whenFound() throws Exception {
		Studio domain = new Studio("Warner Bros.");
		StudioResponse resp = new StudioResponse();
		given(studioUseCase.getStudioById(1L)).willReturn(Optional.of(domain));
		given(studioMapper.toResponse(domain)).willReturn(resp);

		mockMvc.perform(get("/api/studios/{id}", 1L)).andExpect(status().isOk());
	}

	@Test
	void updateStudio_shouldReturn200() throws Exception {
		StudioRequest req = new StudioRequest();
		req.setName("Paramount Pictures");
		Studio domain = new Studio("Paramount Pictures");
		StudioResponse resp = new StudioResponse();

		given(studioMapper.toDomain(any(StudioRequest.class))).willReturn(domain);
		given(studioUseCase.updateStudio(eq(1L), any(Studio.class))).willReturn(domain);
		given(studioMapper.toResponse(any(Studio.class))).willReturn(resp);

		mockMvc.perform(put("/api/studios/{id}", 1L).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(req))).andExpect(status().isOk());
	}

	@Test
	void deleteStudio_shouldReturn204() throws Exception {
		mockMvc.perform(delete("/api/studios/{id}", 1L)).andExpect(status().isNoContent());
		verify(studioUseCase).deleteStudio(1L);
	}

	@Test
	void getAllStudios_shouldReturn200() throws Exception {
		Page<Studio> page = new PageImpl<>(java.util.List.of(), PageRequest.of(0, 10), 0);
		given(studioUseCase.getAllStudios(any())).willReturn(page);
		PageResponse<StudioResponse> pr = PageResponse.of(new PageImpl<StudioResponse>(java.util.List.of()));
		given(pageMapper.toPageResponse(Mockito.<Page<Studio>>any(), Mockito.<Function<Studio, StudioResponse>>any()))
				.willReturn(pr);

		mockMvc.perform(get("/api/studios")).andExpect(status().isOk());
	}
}
