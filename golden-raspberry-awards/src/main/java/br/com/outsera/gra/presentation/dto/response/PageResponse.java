package br.com.outsera.gra.presentation.dto.response;

import java.util.List;

import org.springframework.data.domain.Page;

import lombok.Data;

@Data
public class PageResponse<T> {

	private List<T> content;
	private int page;
	private int size;
	private long totalElements;
	private int totalPages;
	private boolean first;
	private boolean last;
	private boolean hasNext;
	private boolean hasPrevious;
	private int numberOfElements;

	public PageResponse(Page<T> page) {
		this.content = page.getContent();
		this.page = page.getNumber();
		this.size = page.getSize();
		this.totalElements = page.getTotalElements();
		this.totalPages = page.getTotalPages();
		this.first = page.isFirst();
		this.last = page.isLast();
		this.hasNext = page.hasNext();
		this.hasPrevious = page.hasPrevious();
		this.numberOfElements = page.getNumberOfElements();
	}

	public static <T> PageResponse<T> of(Page<T> page) {
		return new PageResponse<>(page);
	}
}
