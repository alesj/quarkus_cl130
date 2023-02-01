package com.alesj.qcl.app;

import com.example.grpc.exc.HelloReply;
import com.example.grpc.exc.HelloRequest;
import com.example.grpc.exc.LegacyHelloGrpcGrpc;
import com.google.rpc.Code;
import com.google.rpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.protobuf.StatusProto;
import io.grpc.stub.StreamObserver;
import io.quarkus.grpc.GrpcService;

@GrpcService
public class LegacyGrpcService extends LegacyHelloGrpcGrpc.LegacyHelloGrpcImplBase {
    @Override
    public void legacySayHello(HelloRequest request, StreamObserver<HelloReply> responseObserver) {
        final StatusRuntimeException t =
            StatusProto.toStatusRuntimeException(
                Status.newBuilder().setCode(Code.INVALID_ARGUMENT_VALUE).build());
        responseObserver.onError(t);
    }
}
