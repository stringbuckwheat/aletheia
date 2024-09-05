package com.gold.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AuthApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthApplication.class, args);

		// 서버 실행 테스트
		HelloServiceServer helloServiceServer = new HelloServiceServer(50051);
		helloServiceServer.start();
	}

}
