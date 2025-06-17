package com.example.helloboot.dto;
import org.springframework.data.domain.Page;
import java.util.List;
public class PagedResponse<T> {
    private final List<T> content;
    private final long totalElements;
    private final int  totalPages;
    private final int  pageNumber;
    private final int  pageSize;
    private final boolean last;
    public PagedResponse(Page<T> page) {
        this.content = page.getContent();
        this.totalElements = page.getTotalElements();
        this.totalPages = page.getTotalPages();
        this.pageNumber = page.getNumber();
        this.pageSize = page.getSize();
        this.last = page.isLast();
    }
    public List<T> getContent() {
        return content;
    }
    public long getTotalElements() {
        return totalElements;
    }
    public int getTotalPages() {
        return totalPages;
    }
    public int getPageNumber() {
        return pageNumber;
    }
    public int getPageSize() {
        return pageSize;
    }
    public boolean isLast() {
        return last;
    }
}
