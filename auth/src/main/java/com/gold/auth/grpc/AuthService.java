package com.gold.auth.grpc;

import com.gold.auth.AletheiaUser;
import com.gold.auth.AuthRequest;
import com.gold.auth.AuthServiceGrpc;
import com.gold.auth.auth.token.AccessToken;
import com.gold.auth.auth.service.TokenProvider;
import com.gold.auth.common.error.ErrorMessage;
import com.gold.auth.common.error.exception.RefreshTokenException;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.NoSuchElementException;

/**
 * gRPC를 통해 사용자 인증/인가 여부와 사용자 정보를 반환하는 서비스
 */
@RequiredArgsConstructor
@GrpcService
@Slf4j
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
            log.info("Access Token expired: {}, Message: {}", request.getToken(), e.getMessage());

            responseObserver.onError(Status.UNAUTHENTICATED
                    .withDescription(ErrorMessage.ACCESS_TOKEN_EXPIRED.getMessage())
                    .asRuntimeException());
        } catch (UnsupportedJwtException | MalformedJwtException | SignatureException e) {
            // 지원되지 않는/유효하지 않은 JWT의 경우
            log.warn("{} by invalid access token: {}, Message: {}",
                    e.getClass().getSimpleName(),
                    request.getToken(), e.getMessage());

            responseObserver.onError(Status.INVALID_ARGUMENT
                    .withDescription(ErrorMessage.INVALID_ACCESS_TOKEN.getMessage())
                    .asRuntimeException());
        } catch (RefreshTokenException | NoSuchElementException e) {
            // RefreshTokenException: Refresh token 만료 등
            // NoSuchElementException: Redis에 리프레쉬 토큰이 존재하지 않는 경우
            log.warn("{} by invalid refresh token: {}, Message: {}",
                    e.getClass().getSimpleName(),
                    request.getToken(), e.getMessage());

            responseObserver.onError(Status.UNAUTHENTICATED
                    .withDescription(e.getMessage())
                    .asRuntimeException());
        } catch (Exception e) {
            // 일반적인 예외 처리
            log.error("Unexpected error occurred, Message: {}", e.getMessage(), e);

            responseObserver.onError(Status.INTERNAL
                    .withDescription(ErrorMessage.UNEXPECTED_ERROR_OCCUR.getMessage())
                    .asRuntimeException());
        }
    }
}
