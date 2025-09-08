package br.com.outsera.gra.presentation.mapper;

import java.util.function.Function;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import br.com.outsera.gra.presentation.dto.response.PageResponse;

@Component
public class PageMapper {

	public <T, R> PageResponse<R> toPageResponse(Page<T> page, Function<T, R> mapper) {
		Page<R> mappedPage = page.map(mapper);
		return PageResponse.of(mappedPage);
	}

	public <T, R> PageResponse<R> toPageResponse(Page<T> page, Function<T, R> mapper, Class<R> responseClass) {
		return toPageResponse(page, mapper);
	}
}
