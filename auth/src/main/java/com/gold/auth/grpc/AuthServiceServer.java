package com.gold.auth.grpc;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Component
@Slf4j
public class AuthServiceServer {

    private final AuthService authService;
    @Value("${grpc.server.port}")
    private int port;
    private Server authServer;

    public AuthServiceServer(AuthService authService) {
        this.authService = authService;
    }

    @PostConstruct
    public void start() {
        try {
            authServer = ServerBuilder.forPort(port)
                    .addService(authService)
                    .build()
                    .start();

            log.info("Server started on : {}", authServer.getPort());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @PreDestroy
    public void stop() {
        if (authServer != null) {
            authServer.shutdown();
        }
    }
}


