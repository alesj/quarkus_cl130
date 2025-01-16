package com.alesj.qcl.test;

import io.quarkus.grpc.GrpcClient;
import io.quarkus.grpc.GrpcClientMarker;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.enterprise.inject.Instance;
import jakarta.enterprise.inject.spi.Bean;
import jakarta.enterprise.inject.spi.CDI;
import org.junit.jupiter.api.Test;

/**
 * @author Ales Justin
 */
@QuarkusTest
public class SimpleTest extends ManagedSimpleTestBase {

    @Override
    int port() {
        return 9001;
    }

    @Test
    public void testClients() {
        Instance<Object> clients = CDI.current().select(Object.class, new GrpcClientMarker.Literal());
        System.out.println("clients = " + clients);
        clients.handles().forEach(h -> {
            System.out.println("h = " + h);
            System.out.println("o = " + h.get());
            Bean<Object> bean = h.getBean();
            bean.getQualifiers().forEach(a -> {
                if (a instanceof GrpcClient) {
                    GrpcClient gc = (GrpcClient) a;
                    System.out.println("gc = " + gc);
                }
            });
        });
    }
}
