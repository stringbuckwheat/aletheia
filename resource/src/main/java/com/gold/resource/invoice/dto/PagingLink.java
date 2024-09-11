package com.gold.resource.invoice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
@ToString
public class PagingLink {
    @Schema(description = "현재 페이지", example = "http://localhost:9999/api/resource/sales?invoiceType=sales&page=5&size=10")
    private String self;

    @Schema(description = "첫 페이지", example = "http://localhost:9999/api/resource/sales?invoiceType=sales&page=1&size=10")
    private String first;

    @Schema(description = "마지막 페이지", example = "http://localhost:9999/api/resource/sales?invoiceType=sales&page=55&size=10")
    private String last;

    @Schema(description = "이전 페이지", example = "http://localhost:9999/api/resource/sales?invoiceType=sales&page=4&size=10")
    private String prev;

    @Schema(description = "다음 페이지", example = "http://localhost:9999/api/resource/sales?invoiceType=sales&page=6&size=10")
    private String next;
}
