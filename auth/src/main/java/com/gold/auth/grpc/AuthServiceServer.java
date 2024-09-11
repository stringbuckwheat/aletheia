package com.gold.auth.grpc;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * gRPC 서버 설정
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class AuthServiceServer {
    private final AuthService authService;
    @Value("${grpc.server.port}")
    private int port;
    private Server authServer;

    @PostConstruct
    public void start() {
        try {
            authServer = ServerBuilder.forPort(port)
                    .addService(authService)
                    .build()
                    .start();

            log.info("Server started on : {}", authServer.getPort());
        } catch (IOException e) {
            log.error("Error occurred while starting server: {}", e.getMessage(), e);
        }
    }

    @PreDestroy
    public void stop() {
        if (authServer != null) {
            try {
                authServer.shutdown().awaitTermination(5, TimeUnit.SECONDS);
                log.info("gRPC server stopped successfully");
            } catch (InterruptedException e) {
                log.error("error is occurred while terminate server: {}", e.getMessage(), e);
            }
        }
    }
}


