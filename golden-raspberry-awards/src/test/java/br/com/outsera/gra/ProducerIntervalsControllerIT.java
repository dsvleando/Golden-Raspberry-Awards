package br.com.outsera.gra;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

import br.com.outsera.gra.presentation.dto.response.ProducerAwardIntervalsResponse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProducerIntervalsControllerIT {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	void shouldReturnMinAndMaxIntervalsOverHttp() {
		String url = "http://localhost:" + port + "/api/producers/award-intervals";
		ResponseEntity<ProducerAwardIntervalsResponse> response = restTemplate.getForEntity(url,
				ProducerAwardIntervalsResponse.class);

		assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
		ProducerAwardIntervalsResponse body = response.getBody();
		assertThat(body).isNotNull();
		assertThat(body.getMin()).isNotNull();
		assertThat(body.getMax()).isNotNull();
		assertThat(body.getMin().size()).isEqualTo(1);
		assertThat(body.getMax().size()).isEqualTo(1);
	}
}
