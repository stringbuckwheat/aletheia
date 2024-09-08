package com.gold.resource.invoice.dto;

import lombok.*;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
@ToString
public class PagingLink {
    private String self;
    private String first;
    private String last;
    private String prev;
    private String next;
}
