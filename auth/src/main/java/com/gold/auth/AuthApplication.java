package com.gold.auth;

import com.gold.auth.grpc.AuthService;
import com.gold.auth.grpc.AuthServiceServer;
import com.gold.auth.user.service.TokenProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AuthApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthApplication.class, args);


		// AuthService 구현체 생성
		AuthService authService = new AuthService(new TokenProvider());

		// 서버 포트 및 서비스 설정
		AuthServiceServer server = new AuthServiceServer(50051, authService);

		// 서버 시작
		server.start();
	}

}
