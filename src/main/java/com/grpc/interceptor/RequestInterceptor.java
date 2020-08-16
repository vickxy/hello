package com.grpc.interceptor;

import com.codahale.metrics.JmxReporter;
import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;

import io.grpc.ForwardingServerCallListener.SimpleForwardingServerCallListener;
import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCall.Listener;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;

public class RequestInterceptor implements ServerInterceptor {
  MetricRegistry metricRegistry = new MetricRegistry();
  Meter onMessage = metricRegistry.meter("grpc.on.message");
  Meter onCancel = metricRegistry.meter("grpc.on.cancel");

  public RequestInterceptor() {
    JmxReporter jmxReporter = JmxReporter.forRegistry(metricRegistry).build();
    jmxReporter.start();
  }

  @Override
  public <ReqT, RespT> Listener<ReqT> interceptCall(final ServerCall<ReqT, RespT> serverCall,
      final Metadata headers, final ServerCallHandler<ReqT, RespT> serverCallHandler) {

    return new SimpleForwardingServerCallListener<ReqT>(
        serverCallHandler.startCall(serverCall, headers)) {

      @Override
      public void onMessage(ReqT message) {
        onMessage.mark();
        super.onMessage(message);
      }

      @Override
      public void onCancel() {
        onCancel.mark();
      }
    };
  }
}
