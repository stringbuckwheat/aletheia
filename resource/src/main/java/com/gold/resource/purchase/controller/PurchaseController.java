package com.gold.resource.purchase.controller;

import com.gold.resource.auth.dto.CustomUserDetails;
import com.gold.resource.common.doc.ApiDocs;
import com.gold.resource.common.dto.InvoiceRequest;
import com.gold.resource.common.dto.InvoiceResponse;
import com.gold.resource.common.enums.InvoiceType;
import com.gold.resource.common.error.ErrorResponse;
import com.gold.resource.invoice.dto.InvoiceQueryParam;
import com.gold.resource.invoice.dto.PagingInvoice;
import com.gold.resource.purchase.dto.PurchaseStatusUpdate;
import com.gold.resource.purchase.service.PurchaseService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@Slf4j
public class PurchaseController {
    private final PurchaseService purchaseService;

    @ApiDocs(summary = "금 구매 정보 저장", description = "유저의 금 구매 정보 저장")
    @ApiResponse(
            responseCode = "201",
            description = "CREATED: 금 구매 정보 저장 성공",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = InvoiceResponse.class))
    )
    @ApiResponse(
            responseCode = "400",
            description = "BAD_REQUEST: 요청의 필수 입력값 누락 또는 잘못된 데이터",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(
                            value = "{\"message\": \"품목은 필수입니다.\"}"
                    )
            )
    )
    @PostMapping("/api/resource/purchase")
    public ResponseEntity<InvoiceResponse> save(@RequestBody @Valid InvoiceRequest purchaseRequest,
                                                @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.status(HttpStatus.CREATED).body(purchaseService.save(purchaseRequest, userDetails.getId()));
    }

    @ApiDocs(summary = "금 구매 내역 조회", description = "해당 유저의 모든 금 구매 내역을 조회, 페이징")
    @ApiResponse(
            responseCode = "200",
            description = "OK: 금 구매 내역 조회 성공",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = PagingInvoice.class))
    )
    @GetMapping("/api/resource/purchase")
    public ResponseEntity<PagingInvoice> getAll(@RequestParam(value = "startDate", required = false) LocalDate startDate,
                                                @RequestParam(value = "endDate", required = false) LocalDate endDate,
                                                @RequestParam(value = "offset", defaultValue = "1") int page,
                                                @RequestParam(value = "limit", defaultValue = "10") int limit,
                                                @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        InvoiceQueryParam queryParam = InvoiceQueryParam.builder()
                .userId(userDetails.getId())
                .startDate(startDate)
                .endDate(endDate)
                .invoiceType(InvoiceType.PURCHASE)
                .pageable(PageRequest.of(page - 1, limit))
                .domain(InvoiceType.PURCHASE)
                .build();

        return ResponseEntity.ok().body(purchaseService.getAll(userDetails.getId(), queryParam));
    }

    @ApiDocs(summary = "금 구매 상세 조회", description = "금 구매 ID(PK)를 기반으로 본인(사용자 ID)의 것일때만 조회")
    @ApiResponse(
            responseCode = "200",
            description = "OK: 금 구매 상세 조회 성공",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = InvoiceResponse.class))
    )
    @ApiResponse(
            responseCode = "403",
            description = "FORBIDDEN: 본인의 금 구매 내역이 아닌 경우",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(
                            value = "{\"message\": \"해당 주문에 대한 접근 권한이 없습니다.\"}"
                    )
            )
    )
    @ApiResponse(
            responseCode = "404",
            description = "NOT_FOUND: 금 주문 ID에 해당하는 주문 정보가 없는 경우",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(
                            value = "{\"message\": \"해당 구매 주문을 찾을 수 없습니다.\"}"
                    )
            )
    )
    @GetMapping("/api/resource/purchase/{purchaseId}")
    public ResponseEntity<InvoiceResponse> getDetail(
            @PathVariable(name = "purchaseId") Long purchaseId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok().body(purchaseService.getDetail(purchaseId, userDetails.getId()));
    }

    @ApiDocs(summary = "금 구매 상태 변경", description = "요청된 금 주문 내역이 본인(사용자 ID)의 것일때만 변경 가능")
    @ApiResponse(
            responseCode = "200",
            description = "OK: 금 구매 상태 변경 성공",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = InvoiceResponse.class))
    )
    @ApiResponse(
            responseCode = "403",
            description = "FORBIDDEN: 본인의 금 구매 내역이 아닌 경우",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(
                            value = "{\"message\": \"해당 주문에 대한 접근 권한이 없습니다.\"}"
                    )
            )
    )
    @ApiResponse(
            responseCode = "404",
            description = "NOT_FOUND: 금 주문 ID에 해당하는 주문 정보가 없는 경우",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(
                            value = "{\"message\": \"해당 구매 주문을 찾을 수 없습니다.\"}"
                    )
            )
    )
    @PutMapping("/api/resource/purchase/{purchaseId}/status")
    public ResponseEntity<InvoiceResponse> updateState(
            @PathVariable(name = "purchaseId") Long purchaseId,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody @Valid PurchaseStatusUpdate statusUpdate) {
        return ResponseEntity.ok().body(purchaseService.updateState(purchaseId, userDetails.getId(), statusUpdate));
    }
}
