package com.gold.auth.grpc;

import com.gold.auth.AletheiaUser;
import com.gold.auth.AuthRequest;
import com.gold.auth.AuthServiceGrpc;
import com.gold.auth.auth.token.AccessToken;
import com.gold.auth.auth.service.TokenProvider;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

@RequiredArgsConstructor
@GrpcService
public class AuthService extends AuthServiceGrpc.AuthServiceImplBase {
    private final TokenProvider tokenProvider;

    @Override
    public void getAuthentication(AuthRequest request, StreamObserver<AletheiaUser> responseObserver) {
        try {
            // JWT -> Access Token 객체
            AccessToken accessToken = tokenProvider.convertAccessToken(request.getToken());
            Claims claims = accessToken.getData();

            Long userId = Long.valueOf(String.valueOf(claims.get("aud")));
            String username = claims.getSubject();
            String role = String.valueOf(claims.get("role"));

            AletheiaUser response = AletheiaUser.newBuilder()
                    .setId(userId)
                    .setUsername(username)
                    .setRole(role)
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (ExpiredJwtException e) {
            // JWT가 만료된 경우
            responseObserver.onError(Status.UNAUTHENTICATED
                    .withDescription("액세스 토큰이 만료되었습니다. 재발급해주세요")
                    .asRuntimeException());
        } catch (UnsupportedJwtException | MalformedJwtException | SignatureException e) {
            // 지원되지 않는 JWT의 경우
            responseObserver.onError(Status.INVALID_ARGUMENT
                    .withDescription("유효하지 않은 액세스 토큰입니다. 다시 로그인해주세요")
                    .asRuntimeException());
        } catch (Exception e) {
            // 일반적인 예외 처리
            responseObserver.onError(Status.INTERNAL
                    .withDescription("Internal server error: " + e.getMessage())
                    .asRuntimeException());
        }
    }
}
