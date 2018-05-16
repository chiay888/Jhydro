package com.jzp.jhydro;



import jdk.incubator.http.HttpRequest;
import jdk.incubator.http.HttpResponse;
import org.json.simple.*;



import java.net.URI;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class HydroService {
        //private final String BaseUrl = "https://api.hydrogenplatform.com/hydro/v1";
    private final String BaseUrl = "https://sandbox.hydrogenplatform.com/hydro/v1";


        //private HttpClient httpClient;

        private Map<String, String> BaseParameters = new HashMap<String, String>();


        private String ApiKey;
        private String ApiUsername;


        public HydroService(String apiKey, String apiUsername) throws Exception
        {
            if ((apiKey==null || apiKey.equals("")) || (apiUsername == null || apiUsername.equals("")))
                throw new Exception("Error en parametros apiKey y apiUsername, revise valores");

            ApiKey = apiKey;

            ApiUsername = apiUsername;

            BaseParameters.put("username", ApiUsername);
            BaseParameters.put("key", ApiKey);


        }
        public String whiteList(String hydroAddress) throws Exception{
            final CountDownLatch latch = new CountDownLatch(4);

            String content = "{\"key\":\""+BaseParameters.get("key")+"\",\"username\":\""+BaseParameters.get("username")+"\"}";
            final HttpRequest request = HttpClientHelper.postRequest(HttpClientHelper.CLIENT,
                    new URI(BaseUrl+"/whitelist/"+hydroAddress), HttpClientHelper.HEADERS,
                    () -> HttpRequest.BodyPublisher.fromString(content));



            final CompletableFuture<HttpResponse<String>> futureResponse = HttpClientHelper.CLIENT.sendAsync(request,
                    HttpResponse.BodyHandler.asString());

            handleFutureResponseWhiteList(futureResponse, latch, "'Put'       : 'https://api.hydrogenplatform.com/hydro/v1/whitelist/{0}'");

            latch.await(60l, TimeUnit.SECONDS);
            Object obj=JSONValue.parse(futureResponse.get().body()); JSONArray array=(JSONArray)obj;
            //String hydroAddressId = JSONObject new Gson().fromJson(futureResponse.get().body(),String.class);
            String hydroAddressId= array.get("hydroAdressId");
            return hydroAddressId;

            //return futureResponse.get().toString();
        }

    private static void handleFutureResponseWhiteList(final CompletableFuture<HttpResponse<String>> futureResponse,
                                             final CountDownLatch latch, final String message) {
        assert !Objects.isNull(futureResponse) && !Objects.isNull(latch) && !Objects.isNull(message);


        futureResponse.whenComplete((response, exception) -> {
            try {
                if (Objects.isNull(exception)) {
                    HttpClientHelper.printResponse(response, message);
                    //String hydroAddressId = new Gson().fromJson(response.body(),String.class);
                    //return hydroAddressId;

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

    public void requestAirdrop(String hydroAddressId) throws Exception{
        final CountDownLatch latch = new CountDownLatch(4);

        String content = "{\"key\":\""+BaseParameters.get("key")+"\",\"username\":\""+BaseParameters.get("username")+"\"}";
        final HttpRequest request = HttpClientHelper.postRequest(HttpClientHelper.CLIENT,
                new URI(BaseUrl+"/challenge?hydroAdressId="+hydroAddressId), HttpClientHelper.HEADERS,
                () -> HttpRequest.BodyPublisher.fromString(content));

        final CompletableFuture<HttpResponse<String>> futureResponse = HttpClientHelper.CLIENT.sendAsync(request,
                HttpResponse.BodyHandler.asString());

        handleFutureResponseRequestAirdrop(futureResponse, latch, "'Put'       : 'https://api.hydrogenplatform.com/hydro/v1//challenge?hydroAdressId'");
        latch.await(60l, TimeUnit.SECONDS);
    }

    private static void handleFutureResponseRequestAirdrop(final CompletableFuture<HttpResponse<String>> futureResponse,
                                                      final CountDownLatch latch, final String message) {
        assert !Objects.isNull(futureResponse) && !Objects.isNull(latch) && !Objects.isNull(message);

        futureResponse.whenComplete((response, exception) -> {
            try {
                if (Objects.isNull(exception)) {
                    HttpClientHelper.printResponse(response, message);
                    //String str = new Gson().fromJson(response.body(),String.class);

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
    /*

        public async Task<String> RegisterAddress(String address)
        {
            String path = "/whitelist/{0}" + address;

            StringContent content = new StringContent(BaseParameters.ToString(), Encoding.UTF8);

            HttpResponseMessage response = await httpClient.PostAsync(path, content).ConfigureAwait(false);

            string hydroAddressId = await response.Content.ReadAsStringAsync().ConfigureAwait(false);

            return hydroAddressId;
        }

        public async Task<RaindropDetails> RequestRaindrop(string hydroAddressId)
        {
            string path = "/challenge?hydroAddressId=" + hydroAddressId;

            StringContent content = new StringContent(BaseParameters.ToString(), Encoding.UTF8);

            HttpResponseMessage response = await httpClient.PostAsync(path, content).ConfigureAwait(false);

            string responseString  = await response.Content.ReadAsStringAsync().ConfigureAwait(false);

            RaindropDetails details = JsonConvert.DeserializeObject<RaindropDetails>(responseString);

            return details;
        }

        public async Task<bool> CheckValidRaindrop(string hydroAddressId)
        {
            string path = "/authenticate?hydroAddressId=" + hydroAddressId;

            StringContent content = new StringContent(BaseParameters.ToString(), Encoding.UTF8);

            HttpResponseMessage response = await httpClient.PostAsync(path, content).ConfigureAwait(false);

            string responseString = await response.Content.ReadAsStringAsync().ConfigureAwait(false);

            return responseString == "true" ?  true : false;
        }

    }
}
*/
}
