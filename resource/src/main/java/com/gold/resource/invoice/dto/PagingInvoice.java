package com.gold.resource.invoice.dto;

import com.gold.resource.common.dto.InvoiceResponse;
import com.gold.resource.invoice.enums.PagingMessage;
import lombok.Getter;

import java.util.List;

@Getter
public class PagingInvoice {
    private boolean success;
    private String message;
    private List<InvoiceResponse> data;
    private PagingLink link;

    public PagingInvoice(List<InvoiceResponse> data, PagingLink link) {
        PagingMessage message = data.size() == 0 ? PagingMessage.EMPTY : PagingMessage.SUCCESS;

        this.success = true;
        this.message = message.getMessage();
        this.data = data;
        this.link = link;
    }
}