/*
 * Copyright Strimzi authors.
 * License: Apache License 2.0 (see the file LICENSE or http://apache.org/licenses/LICENSE-2.0.html).
 */
package com.alesj.qcl.common.rest;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.codec.BodyCodec;

public class ProducerService extends BaseService {

    private static ProducerService producerService;

    private ProducerService(WebClient webClient) {
        super(webClient);
    }

    public static synchronized ProducerService getInstance(WebClient webClient) {
        if (producerService == null || webClient != producerService.webClient) {
            producerService = new ProducerService(webClient);
        }
        return producerService;
    }

    public HttpRequest<JsonObject> sendRecordsRequest(String topic, JsonObject jsonObject, String bridgeContentType) {
        return postRequest(Urls.producerTopic(topic))
                .putHeader("Content-Length", String.valueOf(jsonObject.toBuffer().length()))
                .putHeader("Content-Type", bridgeContentType)
                .as(BodyCodec.jsonObject());
    }

    public HttpRequest<JsonObject> sendRecordsToPartitionRequest(String topic, Object partition, JsonObject jsonObject, String bridgeContentType) {
        return postRequest(Urls.producerTopicPartition(topic, partition))
                .putHeader("Content-Length", String.valueOf(jsonObject.toBuffer().length()))
                .putHeader("Content-Type", bridgeContentType)
                .as(BodyCodec.jsonObject());
    }
}
