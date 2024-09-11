package com.gold.resource.auth.grpc;

import com.gold.resource.AletheiaUser;
import com.gold.resource.AuthRequest;
import com.gold.resource.AuthServiceGrpc;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

/**
 * gRPC Client 서비스
 */
@Service
public class AuthServiceClient {
    @GrpcClient("auth")
    private AuthServiceGrpc.AuthServiceBlockingStub authServiceStub;

    public AletheiaUser getAuthentication(String token) {
        // AuthRequest 메시지 생성
        AuthRequest request = AuthRequest.newBuilder().setToken(token).build();

        // 서버로 요청을 보내고 응답을 받음
        return authServiceStub.getAuthentication(request);
    }
}
