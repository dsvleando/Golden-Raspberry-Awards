package br.com.outsera.gra.presentation.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProducerAwardIntervalsResponse {

	private List<ProducerIntervalResponse> min;
	private List<ProducerIntervalResponse> max;
}
