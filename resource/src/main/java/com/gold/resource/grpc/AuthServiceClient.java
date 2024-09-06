package com.gold.resource.grpc;

import com.gold.resource.AletheiaUser;
import com.gold.resource.AuthRequest;
import com.gold.resource.AuthServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.StatusRuntimeException;
import io.grpc.netty.shaded.io.grpc.netty.NettyChannelBuilder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceClient {

    // gRPC Stub을 자동으로 주입받음
//    @GrpcClient("auth")
//    private AuthServiceGrpc.AuthServiceBlockingStub authServiceStub;
    private AuthServiceGrpc.AuthServiceBlockingStub authServiceStub;

    public AuthServiceClient() {
        ManagedChannel channel = NettyChannelBuilder.forAddress("localhost", 50051)
                .usePlaintext()
                .build();
        authServiceStub = AuthServiceGrpc.newBlockingStub(channel);
    }

    public AletheiaUser getAuthentication(String token) {
        // AuthRequest 메시지 생성
        AuthRequest request = AuthRequest.newBuilder()
                .setToken(token)
                .build();

        // 서버로 요청을 보내고 응답을 받음
        return authServiceStub.getAuthentication(request);
    }
}
