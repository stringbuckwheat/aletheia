package com.gold.resource;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TestController {
    private final GrpcClient grpcClient;

    @GetMapping("/api/test")
    public String grpcTest() {
        return grpcClient.sendMessage("test");
    }
}
