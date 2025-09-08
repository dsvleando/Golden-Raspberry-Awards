package br.com.outsera.gra;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import br.com.outsera.gra.application.port.inbound.ProducerUseCase;
import br.com.outsera.gra.shared.vo.ProducerAwardIntervals;

@SpringBootTest
class ProducerIntervalsIT {

	@Autowired
	private ProducerUseCase producerUseCase;

	@Test
	void shouldReturnMinAndMaxIntervals() {
		ProducerAwardIntervals intervals = producerUseCase.getProducerAwardIntervals();
		assertThat(intervals.getMin()).isNotNull();
		assertThat(intervals.getMax()).isNotNull();
		assertThat(intervals.getMin().size()).isEqualTo(1);
		assertThat(intervals.getMax().size()).isEqualTo(1);
	}
}
