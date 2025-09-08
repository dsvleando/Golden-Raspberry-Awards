package br.com.outsera.gra.presentation.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProducerIntervalResponse {

	private String producer;
	private int interval;
	private int previousWin;
	private int followingWin;
}
