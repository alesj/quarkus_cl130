package com.alesj.qcl.common.rest;

import io.strimzi.kafka.bridge.BridgeContentType;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.tracing.TracingOptions;
import io.vertx.core.tracing.TracingPolicy;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.tracing.opentelemetry.OpenTelemetryOptions;

import java.util.logging.Logger;

import static io.strimzi.kafka.bridge.tracing.TracingConstants.JAEGER;
import static io.strimzi.kafka.bridge.tracing.TracingConstants.OPENTELEMETRY_SERVICE_NAME_PROPERTY_KEY;
import static io.strimzi.kafka.bridge.tracing.TracingConstants.OPENTELEMETRY_TRACES_EXPORTER_PROPERTY_KEY;

/**
 * @author Ales Justin
 */
public class Input implements AutoCloseable {
    private boolean closed;

    public boolean closed() {
        return closed;
    }

    private static final Logger log = Logger.getLogger(Input.class.getName());

    public static void main(String[] args) {
        log.info("Started input...");

        System.setProperty(OPENTELEMETRY_TRACES_EXPORTER_PROPERTY_KEY, JAEGER);
        System.setProperty(OPENTELEMETRY_SERVICE_NAME_PROPERTY_KEY, "opentelemetry-test");
        TracingOptions to = new OpenTelemetryOptions();

        Vertx vertx = Vertx.vertx(new VertxOptions().setTracingOptions(to));

        WebClient client = WebClient.create(vertx, new WebClientOptions()
            .setDefaultHost(Urls.BRIDGE_HOST)
            .setDefaultPort(Urls.BRIDGE_PORT)
            .setTracingPolicy(TracingPolicy.ALWAYS)
        );

        String value = "message-value";

        JsonArray records = new JsonArray();
        JsonObject json = new JsonObject();
        json.put("value", value);
        records.add(json);

        JsonObject root = new JsonObject();
        root.put("records", records);

        ProducerService.getInstance(client)
            .sendRecordsRequest("mytopic", root, BridgeContentType.KAFKA_JSON_JSON)
            .sendJsonObject(root, verifyOK());
    }

    static Handler<AsyncResult<HttpResponse<JsonObject>>> verifyOK() {
        return ar -> {
            if (ar.failed()) {
                log.warning("Error ..." + ar.cause());
            } else {
                log.info("Sent OK ...");
            }
        };
    }

    @Override
    public void close() {
        closed = true;
    }
}
