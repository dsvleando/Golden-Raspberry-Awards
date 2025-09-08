package br.com.outsera.gra.shared.vo;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProducerAwardIntervals {

	private List<ProducerInterval> min;
	private List<ProducerInterval> max;
}