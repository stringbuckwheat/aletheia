package com.gold.auth.grpc;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class AuthServiceServer {
    private Server authServer;

    public AuthServiceServer(int port, AuthService authService) {
        this.authServer = ServerBuilder.forPort(port)
                .addService(authService)
                .build();
    }


    public void start() {
        try {
            authServer.start();
            System.out.println("Server started on : " + authServer.getPort());
            authServer.awaitTermination();
        } catch (IOException IOE) {
            IOE.printStackTrace();
        } catch (InterruptedException IE) {
            IE.printStackTrace();
        }
    }
}
