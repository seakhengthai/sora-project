package com.demo.user.profile.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PageResponse {
    @JsonProperty("page_number")
    Integer pageNumber;
    @JsonProperty("page_size")
    Integer pageSize;
    @JsonProperty("total_elements")
    Long totalElements;
    @JsonProperty("total_pages")
    Integer totalPages;

    public static PageResponse fromPage(org.springframework.data.domain.Page<?> page) {
        return PageResponse.builder()
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalPages(page.getTotalPages())
                .totalElements(page.getTotalElements())
                .build();
    }
}
