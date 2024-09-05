package com.gold.resource;

import io.grpc.ManagedChannel;
import io.grpc.StatusRuntimeException;
import io.grpc.netty.shaded.io.grpc.netty.NettyChannelBuilder;
import org.springframework.stereotype.Component;

@Component
public class GrpcClient {
    private final MyServiceGrpc.MyServiceBlockingStub blockingStub;

    // gRPC 서버에 연결 (생성자)
    public GrpcClient() {
        ManagedChannel channel = NettyChannelBuilder.forAddress("localhost", 50051)
                .usePlaintext()
                .build();
        blockingStub = MyServiceGrpc.newBlockingStub(channel);
    }


    public String sendMessage(final String name) {
        try{
            HelloReply response = blockingStub.sayHello(HelloRequest.newBuilder().setName(name).build());
            return response.getMessage();
        } catch (StatusRuntimeException e) {
            return "FAILED with " + e.getStatus().getCode().name();
        }
    }
}
