package com.gold.auth.grpc;

import com.gold.auth.AletheiaUser;
import com.gold.auth.AuthRequest;
import com.gold.auth.AuthServiceGrpc;
import com.gold.auth.common.token.AccessToken;
import com.gold.auth.user.service.TokenProvider;
import io.grpc.stub.StreamObserver;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

@RequiredArgsConstructor
@GrpcService
public class AuthService extends AuthServiceGrpc.AuthServiceImplBase {
    private final TokenProvider tokenProvider;

    @Override
    public void getAuthentication(AuthRequest request, StreamObserver<AletheiaUser> responseObserver) {
        // JWT -> Access Token 객체
        AccessToken accessToken = tokenProvider.convertAccessToken(request.getToken());
        Claims claims = accessToken.getData();

        Long userId = Long.valueOf(String.valueOf(claims.get("aud")));
        String username = claims.getSubject();

        AletheiaUser response = AletheiaUser.newBuilder()
                .setId(userId)
                .setUsername(username)
                .setName("testName")
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();

    }
}
