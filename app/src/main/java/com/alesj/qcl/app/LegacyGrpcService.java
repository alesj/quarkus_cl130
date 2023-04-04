package com.alesj.qcl.app;

import com.example.grpc.exc.HelloReply;
import com.example.grpc.exc.HelloRequest;
import com.example.grpc.exc.LegacyHelloGrpcGrpc;
import io.grpc.stub.StreamObserver;
import io.quarkus.grpc.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@GrpcService
public class LegacyGrpcService extends LegacyHelloGrpcGrpc.LegacyHelloGrpcImplBase {

    Logger log = LoggerFactory.getLogger(LegacyGrpcService.class);

    @Override
    public void legacySayHello(HelloRequest request, StreamObserver<HelloReply> responseObserver) {
        String thread = " - thread: " + Thread.currentThread().getName();
        log.info(request.getName() + thread);
        responseObserver.onNext(HelloReply.newBuilder().setMessage(thread).build());
        responseObserver.onCompleted();
    }
}
