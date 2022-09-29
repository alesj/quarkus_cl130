package com.alesj.qcl.test;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.PemKeyCertOptions;
import io.vertx.core.net.PemTrustOptions;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.concurrent.TimeUnit;

@QuarkusTest
@TestProfile(NewVertxGrpcTestProfile.class)
class EndpointTest {

    @Inject
    Vertx vertx;

    @Test
    public void testHelloWorldServiceUsingBlockingStub() throws Exception {
        WebClientOptions options = new WebClientOptions();
        options.setSsl(true);
        options.setUseAlpn(true);
        options.setTrustOptions(new PemTrustOptions().addCertValue(buffer("tls/ca.pem")));
        options.setKeyCertOptions(
            new PemKeyCertOptions()
                .setKeyValue(buffer("tls/client.key"))
                .setCertValue(buffer("tls/client.pem"))
        );
        WebClient client = WebClient.create(vertx, options);
        HttpRequest<Buffer> request = client.get(8444, "localhost", "/hello/blocking/neo");
        Future<HttpResponse<Buffer>> fr = request.send();
        String response = fr.toCompletionStage().toCompletableFuture().get(10, TimeUnit.SECONDS).bodyAsString();
        Assertions.assertEquals(response, "Hello neo");
    }

    private Buffer buffer(String resource) {
        try (InputStream stream = getClass().getClassLoader().getResourceAsStream(resource)) {
            return Buffer.buffer(stream.readAllBytes());
        } catch (IOException ioe) {
            throw new UncheckedIOException(ioe);
        }
    }

}
