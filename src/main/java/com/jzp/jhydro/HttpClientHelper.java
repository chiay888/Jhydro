package com.jzp.jhydro;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

import jdk.incubator.http.HttpClient;
import jdk.incubator.http.HttpRequest;
import jdk.incubator.http.HttpRequest.Builder;
import jdk.incubator.http.HttpResponse;

public class HttpClientHelper {
    static final HttpClient CLIENT = HttpClient.newBuilder().followRedirects(HttpClient.Redirect.ALWAYS).build();
    static final Map<String, String> HEADERS = new HashMap<>();
    static {
        HEADERS.put("Accept", "application/json");
        HEADERS.put("Content-Type", "application/json");
    }

    private HttpClientHelper() {
        throw new IllegalStateException("Instantiation not allowed");
    }

    static HttpRequest getRequest(final HttpClient client, final URI uri, final Map<String, String> headers) {
        assert !Objects.isNull(client) && !Objects.isNull(uri) && !Objects.isNull(headers);

        HttpRequest.Builder builder = HttpRequest.newBuilder().version(client.version()).uri(uri).GET();

        fillHeaders(builder, headers);
        return builder.build();
    }

    static HttpRequest postRequest(final HttpClient client, final URI uri, final Map<String, String> headers,
                                   final Supplier<HttpRequest.BodyPublisher> bodyProcessorProducer) {
        assert !Objects.isNull(client) && !Objects.isNull(uri) && !Objects.isNull(headers);

        HttpRequest.Builder builder = HttpRequest.newBuilder().version(client.version()).uri(uri).POST(bodyProcessorProducer.get());

        fillHeaders(builder, headers);
        return builder.build();
    }

    static HttpRequest putRequest(final HttpClient client, final URI uri, final Map<String, String> headers,
                                  final Supplier<HttpRequest.BodyPublisher> bodyProcessorProducer) {
        assert !Objects.isNull(client) && !Objects.isNull(uri) && !Objects.isNull(headers);

        HttpRequest.Builder builder = HttpRequest.newBuilder().version(client.version()).uri(uri).PUT(bodyProcessorProducer.get());

        fillHeaders(builder, headers);
        return builder.build();
    }

    static HttpRequest deleteRequest(final HttpClient client, final URI uri, final Map<String, String> headers,
                                     final Supplier<HttpRequest.BodyPublisher> bodyProcessorProducer) {
        assert !Objects.isNull(client) && !Objects.isNull(uri) && !Objects.isNull(headers);

        HttpRequest.Builder builder = HttpRequest.newBuilder().version(client.version()).uri(uri)
                .DELETE(bodyProcessorProducer.get());

        fillHeaders(builder, headers);
        return builder.build();
    }

    static void printResponse(final HttpResponse<String> response, final String message) {
        assert !Objects.isNull(response) && !Objects.isNull(message);

        System.out.printf("%s\nStatus code : %d\n %s\n%s", message, response.statusCode(), response.body(),
                "-----\n\n");
    }

    private static void fillHeaders(final HttpRequest.Builder builder, final Map<String, String> headers) {
        assert !Objects.isNull(builder) && !Objects.isNull(headers);

        headers.forEach((k, v) -> builder.header(k, v));
    }
}
