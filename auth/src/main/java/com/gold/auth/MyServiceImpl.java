package com.gold.auth;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
class MyServiceImpl extends MyServiceGrpc.MyServiceImplBase {

    @Override
    public void sayHello(HelloRequest request, StreamObserver<HelloReply> responseObserver) {
        System.out.println("MyServiceImpl.say hello 시작!");
        HelloReply reply = HelloReply.newBuilder()
                .setMessage("Hello ==> " + request.getName() + " success!")
                .build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }

}
