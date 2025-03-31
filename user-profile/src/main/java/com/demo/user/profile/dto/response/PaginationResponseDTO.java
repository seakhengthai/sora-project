package com.demo.user.profile.dto.response;

import lombok.*;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PaginationResponseDTO<T> {
    private PageResponse pageResponse;
    private List<T> content;
}