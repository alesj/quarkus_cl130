package com.alesj.qcl.app;

import com.example.grpc.exc.HelloReply;
import com.example.grpc.exc.HelloRequest;
import com.example.grpc.exc.LegacyHelloGrpcGrpc;
import io.grpc.Metadata;
import io.grpc.Status;
import io.grpc.StatusException;
import io.grpc.stub.StreamObserver;
import io.quarkus.grpc.GrpcService;

@GrpcService
public class LegacyGrpcService extends LegacyHelloGrpcGrpc.LegacyHelloGrpcImplBase {
    @Override
    public void legacySayHello(HelloRequest request, StreamObserver<HelloReply> responseObserver) {
        // it NEEDS to be plain StatusException, and NOT StatusRuntimeException ?!
        final StatusException t = new StatusException(Status.INVALID_ARGUMENT, new Metadata());
        responseObserver.onError(t);
    }
}
