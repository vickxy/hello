package com.grpc.services;

import com.grpc.hello.grpc.HelloGrpc.HelloImplBase;
import com.grpc.hello.grpc.HelloReply;
import com.grpc.hello.grpc.HelloReply.Builder;
import com.grpc.hello.grpc.HelloRequest;

import io.grpc.stub.StreamObserver;

public class HelloService extends HelloImplBase{
  @Override
  public void sayHello(HelloRequest request, StreamObserver<HelloReply> responseObserver) {
    Builder response = HelloReply.newBuilder();

    String caller = request.getName();
    String greet = "Hello " + caller;
    response.setMessage(greet);
    responseObserver.onNext(response.build());
    responseObserver.onCompleted();
  }
}
