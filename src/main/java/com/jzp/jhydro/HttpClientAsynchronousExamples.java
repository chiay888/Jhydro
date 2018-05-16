package com.jzp.jhydro;

import jdk.incubator.http.HttpRequest;
import jdk.incubator.http.HttpResponse;



import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

final class HttpClientAsynchronousExamples {

    private HttpClientAsynchronousExamples() {
        throw new IllegalStateException("Instantiation not allowed");
    }

    static void start() throws URISyntaxException, InterruptedException {
        System.out.println("Running asynchronous HTTP Client examples\n");

        final CountDownLatch latch = new CountDownLatch(4);

        get(latch);
        post(latch);
        put(latch);
        delete(latch);

        latch.await(60l, TimeUnit.SECONDS);
    }

    private static void get(final CountDownLatch latch) throws URISyntaxException {
        assert !Objects.isNull(latch);

        final HttpRequest request = HttpClientHelper.getRequest(HttpClientHelper.CLIENT,
                new URI("https://nghttp2.org/httpbin/get"), HttpClientHelper.HEADERS);

        final CompletableFuture<HttpResponse<String>> futureResponse = HttpClientHelper.CLIENT.sendAsync(request,
                HttpResponse.BodyHandler.asString());

        handleFutureResponse(futureResponse, latch, "'Get'       : 'https://nghttp2.org/httpbin/get'");
    }

    private static void post(final CountDownLatch latch) throws URISyntaxException {
        assert !Objects.isNull(latch);

        final HttpRequest request = HttpClientHelper.postRequest(HttpClientHelper.CLIENT,
                new URI("https://nghttp2.org/httpbin/post"), HttpClientHelper.HEADERS,
                () -> HttpRequest.BodyPublisher.fromString("Some data"));

        final CompletableFuture<HttpResponse<String>> futureResponse = HttpClientHelper.CLIENT.sendAsync(request,
                HttpResponse.BodyHandler.asString());

        handleFutureResponse(futureResponse, latch, "'Post'      : 'https://nghttp2.org/httpbin/post'");
    }

    private static void put(final CountDownLatch latch) throws URISyntaxException {
        assert !Objects.isNull(latch);

        final HttpRequest request = HttpClientHelper.putRequest(HttpClientHelper.CLIENT,
                new URI("https://nghttp2.org/httpbin/put"), HttpClientHelper.HEADERS,
                () -> HttpRequest.BodyPublisher.fromString("Some data"));

        final CompletableFuture<HttpResponse<String>> futureResponse = HttpClientHelper.CLIENT.sendAsync(request,
                HttpResponse.BodyHandler.asString());

        handleFutureResponse(futureResponse, latch, "'Put'      : 'https://nghttp2.org/httpbin/put'");
    }

    private static void delete(final CountDownLatch latch) throws URISyntaxException {
        assert !Objects.isNull(latch);

        final HttpRequest request = HttpClientHelper.deleteRequest(HttpClientHelper.CLIENT,
                new URI("https://nghttp2.org/httpbin/delete"), HttpClientHelper.HEADERS,
                () -> HttpRequest.BodyPublisher.fromString("Some data"));

        final CompletableFuture<HttpResponse<String>> futureResponse = HttpClientHelper.CLIENT.sendAsync(request,
                HttpResponse.BodyHandler.asString());

        handleFutureResponse(futureResponse, latch, "'Delete'      : 'https://nghttp2.org/httpbin/delete'");
    }

    private static void handleFutureResponse(final CompletableFuture<HttpResponse<String>> futureResponse,
                                             final CountDownLatch latch, final String message) {
        assert !Objects.isNull(futureResponse) && !Objects.isNull(latch) && !Objects.isNull(message);

        futureResponse.whenComplete((response, exception) -> {
            try {
                if (Objects.isNull(exception)) {
                    HttpClientHelper.printResponse(response, message);
                } else {
                    System.err.println(
                            "An exception occured trying to get the future response of the HTTP client request");
                    exception.printStackTrace();
                }
            } finally {
                latch.countDown();
            }
        });
    }
}
