package com.alesj.qcl.app;

import io.grpc.Context;
import io.grpc.Contexts;
import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import io.quarkus.grpc.GlobalInterceptor;
import jakarta.enterprise.context.ApplicationScoped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Ales Justin
 */
@ApplicationScoped
@GlobalInterceptor
public class ServerInterceptors2 implements ServerInterceptor {

    private final Logger logger = LoggerFactory.getLogger(ServerInterceptors2.class);

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> call, Metadata headers, ServerCallHandler<ReqT, RespT> next) {
        Context context = Context.current();
        logger.warn("HOHOHOHO2");
        return Contexts.interceptCall(context, call, headers, next);
    }
}