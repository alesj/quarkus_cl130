package com.alesj.qcl.app;

import io.grpc.ServerInterceptor;
import jakarta.enterprise.inject.spi.Prioritized;

/**
 * @author Ales Justin
 */
public interface GrpcInterceptor extends ServerInterceptor, Prioritized {
    Integer TRACE_INTERCEPTOR_ORDER = 10000;
    Integer ACTIVITY_INTERCEPTOR_ORDER = 9999;
}
