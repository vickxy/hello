package com.grpc.hello;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.grpc.hello.grpc.HelloGrpc;
import com.grpc.hello.grpc.HelloGrpc.HelloBlockingStub;
import com.grpc.hello.grpc.HelloGrpc.HelloFutureStub;
import com.grpc.hello.grpc.HelloReply;
import com.grpc.hello.grpc.HelloRequest;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class GrpcClient {
  final static ExecutorService executors = Executors.newCachedThreadPool();
  private static HelloBlockingStub blockingStub;
  private static HelloFutureStub futureStub;

  public static void main(String[] args) {
    try {
      ManagedChannel channel =
          ManagedChannelBuilder.forAddress("127.0.0.1", 8080).usePlaintext().build();

      blockingStub = HelloGrpc.newBlockingStub(channel);
      futureStub = HelloGrpc.newFutureStub(channel);

      blockingApi();
      listenableFutureApi();
      // sleeping to get the Future response for ListenableFuture type stub
      Thread.sleep(3000);
    } catch (Exception e) {
      System.out.println("error while creating channel " + e);
    }
  }

  public static void blockingApi() {
    // Preparing the request
    HelloRequest request = HelloRequest.newBuilder().setName("Vikesh").build();

    try {
      // adding Deadline of 5ms
      HelloReply reply =
          blockingStub.withDeadlineAfter(1000, TimeUnit.MILLISECONDS).sayHello(request);

      // printing response returned after completion of RPC call
      System.out.println("response from blocking stub message: " + reply.getMessage());

    } catch (Exception e) {
      System.out.println("error in blocking stub Exception: " + e.getMessage());
    }

  }

  public static void listenableFutureApi() {
    // Preparing the request
    HelloRequest request = HelloRequest.newBuilder().setName("Vikesh").build();

    // adding Deadline of 5ms
    ListenableFuture<HelloReply> reply =
        futureStub.withDeadlineAfter(5, TimeUnit.MILLISECONDS).sayHello(request);

    Futures.addCallback(reply, new FutureCallback<HelloReply>() {
      @Override
      public void onSuccess(HelloReply result) {
        System.out.println("response from future stub message: " + result.getMessage());
      }

      @Override
      public void onFailure(Throwable t) {
        System.out.println("error in future stub Exception: " + t.getMessage());
      }
    }, executors);
  }

}
