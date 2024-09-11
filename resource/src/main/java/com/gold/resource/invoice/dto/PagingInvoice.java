package com.gold.resource.invoice.dto;

import com.gold.resource.common.dto.InvoiceResponse;
import com.gold.resource.invoice.enums.PagingMessage;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.List;

@Getter
public class PagingInvoice {
    @Schema(description = "성공 여부", example = "true")
    private boolean success;

    @Schema(description = "메시지", example = "주문 내역 조회에 성공했습니다")
    private String message;

    @Schema(description = "주문 조회 내역")
    private List<InvoiceResponse> data;

    @Schema(description = "페이지네이션 링크")
    private PagingLink link;

    public PagingInvoice(List<InvoiceResponse> data, PagingLink link) {
        PagingMessage message = data.size() == 0 ? PagingMessage.EMPTY : PagingMessage.SUCCESS;

        this.success = true;
        this.message = message.getMessage();
        this.data = data;
        this.link = link;
    }
}