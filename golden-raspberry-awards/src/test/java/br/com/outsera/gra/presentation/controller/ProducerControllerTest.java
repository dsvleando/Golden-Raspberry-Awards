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

import br.com.outsera.gra.application.port.inbound.ProducerUseCase;
import br.com.outsera.gra.domain.model.Producer;
import br.com.outsera.gra.presentation.dto.request.ProducerRequest;
import br.com.outsera.gra.presentation.dto.response.PageResponse;
import br.com.outsera.gra.presentation.dto.response.ProducerResponse;
import br.com.outsera.gra.presentation.mapper.PageMapper;
import br.com.outsera.gra.presentation.mapper.ProducerMapper;

@WebMvcTest(controllers = ProducerController.class)
class ProducerControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private ProducerUseCase producerUseCase;

	@MockBean
	private ProducerMapper producerMapper;

	@MockBean
	private PageMapper pageMapper;

	@Test
	void createProducer_shouldReturn201() throws Exception {
		ProducerRequest req = new ProducerRequest();
		req.setName("Harvey Weinstein");
		Producer domain = new Producer("Harvey Weinstein");
		ProducerResponse resp = new ProducerResponse();

		given(producerMapper.toDomain(any(ProducerRequest.class))).willReturn(domain);
		given(producerUseCase.createProducer(any(Producer.class))).willReturn(domain);
		given(producerMapper.toResponse(any(Producer.class))).willReturn(resp);

		mockMvc.perform(post("/api/producers").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(req))).andExpect(status().isCreated());
	}

	@Test
	void getProducerById_shouldReturn200_whenFound() throws Exception {
		Producer domain = new Producer("Harvey Weinstein");
		ProducerResponse resp = new ProducerResponse();
		given(producerUseCase.getProducerById(1L)).willReturn(Optional.of(domain));
		given(producerMapper.toResponse(domain)).willReturn(resp);

		mockMvc.perform(get("/api/producers/{id}", 1L)).andExpect(status().isOk());
	}

	@Test
	void updateProducer_shouldReturn200() throws Exception {
		ProducerRequest req = new ProducerRequest();
		req.setName("Joel Silver");
		Producer domain = new Producer("Joel Silver");
		ProducerResponse resp = new ProducerResponse();

		given(producerMapper.toDomain(any(ProducerRequest.class))).willReturn(domain);
		given(producerUseCase.updateProducer(eq(1L), any(Producer.class))).willReturn(domain);
		given(producerMapper.toResponse(any(Producer.class))).willReturn(resp);

		mockMvc.perform(put("/api/producers/{id}", 1L).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(req))).andExpect(status().isOk());
	}

	@Test
	void deleteProducer_shouldReturn204() throws Exception {
		mockMvc.perform(delete("/api/producers/{id}", 1L)).andExpect(status().isNoContent());
		verify(producerUseCase).deleteProducer(1L);
	}

	@Test
	void getAllProducers_shouldReturn200() throws Exception {
		Page<Producer> page = new PageImpl<>(java.util.List.of(), PageRequest.of(0, 10), 0);
		given(producerUseCase.getAllProducers(any())).willReturn(page);
		PageResponse<ProducerResponse> pr = PageResponse.of(new PageImpl<ProducerResponse>(java.util.List.of()));
		given(pageMapper.toPageResponse(Mockito.<Page<Producer>>any(),
				Mockito.<Function<Producer, ProducerResponse>>any())).willReturn(pr);

		mockMvc.perform(get("/api/producers")).andExpect(status().isOk());
	}
}
