package com.grpc.hello;

import java.io.IOException;

import com.grpc.interceptor.RequestInterceptor;
import com.grpc.services.HelloService;

import io.grpc.Server;
import io.grpc.ServerBuilder;

/**
 * Hello!
 *
 */
public class App {
  public static void main(String[] args) {
    int port = 8080;
    System.out.println("starting java gRPC Server on port " + port);
    Server server = ServerBuilder.forPort(port)
        .addService(new HelloService()).intercept(new RequestInterceptor()).build();

    try {
      server.start();
      System.out.println("java server started on port " + port);
      server.awaitTermination();

    } catch (IOException | InterruptedException e) {
      System.out.println("java server failed to start on port " + port);
    }
  }
}
