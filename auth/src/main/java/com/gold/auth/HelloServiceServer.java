package com.gold.auth;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class HelloServiceServer {

    private Server helloServer;

    public HelloServiceServer(int port) {
        helloServer = ServerBuilder.forPort(port).addService(new MyServiceImpl()).build();
    }

    public void start() {
        try {
            helloServer.start();
            System.out.println("Server started on : " + helloServer.getPort());
            helloServer.awaitTermination();
        } catch (IOException IOE) {
            IOE.printStackTrace();
        } catch (InterruptedException IE) {
            IE.printStackTrace();
        }
    }
}
