package br.com.outsera.gra.shared.mapper;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Component;

@Component
public class EntityMapper {

	private final ModelMapper modelMapper;

	public EntityMapper(ModelMapper modelMapper) {
		this.modelMapper = modelMapper;
	}

	public <T, K> K mapObject(T source, Class<K> targetClass) {
		return modelMapper.map(source, targetClass);
	}

	public <T, K> List<K> mapList(List<T> source, Class<K> targetClass) {
		return modelMapper.map(source, new TypeToken<List<K>>() {
		}.getType());
	}
}
