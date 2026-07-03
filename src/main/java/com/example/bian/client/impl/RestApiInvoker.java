package com.example.bian.client.impl;
import com.example.bian.client.bushu.T5;
import com.example.bian.client.exception.BinanceApiException;
import com.example.bian.client.impl.utils.EnumLookup;
import com.example.bian.client.impl.utils.JsonWrapper;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


abstract class RestApiInvoker {

    private static final Logger log = LoggerFactory.getLogger(RestApiInvoker.class);
//    private static final OkHttpClient.Builder builder = OKHttpClientBuilder.buildOKHttpClient();
//    private static final OkHttpClient client = new OkHttpClient();
//    private static final OkHttpClient client = OKHttpClientBuilder.buildOKHttpClient().build();
//    private static final OkHttpClient client = new HttpsUtils().getTrustAllClient();
//    private static final OkHttpClient client = new AddCerHTTPSUtils().getOkHttpClient();
    private static final OkHttpClient client = new OkHttpClient().newBuilder().build();


    static void checkResponse(JsonWrapper json) {
        try {
            if (json.containKey("success")) {
                boolean success = json.getBoolean("success");
                if (!success) {
                    String err_code = json.getStringOrDefault("code", "");
                    String err_msg = json.getStringOrDefault("msg", "");
                    if ("".equals(err_code)) {
                        throw new BinanceApiException(BinanceApiException.EXEC_ERROR, "[Executing] " + err_msg);
                    } else {
                        throw new BinanceApiException(BinanceApiException.EXEC_ERROR,
                                "[Executing] " + err_code + ": " + err_msg);
                    }
                }
            } else if (json.containKey("code")) {

                int code = json.getInteger("code");
                if (code != 200) {
                    String message = json.getStringOrDefault("msg", "");
                    throw new BinanceApiException(BinanceApiException.EXEC_ERROR,
                            "[Executing] " + code + ": " + message);
                }
            }
        } catch (BinanceApiException e) {
            throw e;
        } catch (Exception e) {
            throw new BinanceApiException(BinanceApiException.RUNTIME_ERROR,
                    "[Invoking] Unexpected error: " + e.getMessage());
        }
    }

    static <T> T callSync(RestApiRequest<T> request) {
        try {
            String str;
            /*log.info("Request URL " + request.request.url());
            log.info("Request URL " + request.request.method());*/
//            HttpUtil1.sendGet(request.request);
            Call call = client.newCall(request.request);

            Response response = call.execute();
            if(response.code() == 403 || response.code() == 429 || response.code() == 418){
                T5.searchAgain(response.code() + "见到这个问题联系胡亚龙");
            }
            // System.out.println(response.body().string());
            if (response != null && response.body() != null) {
                str = response.body().string();
                response.close();
            } else {
                throw new BinanceApiException(BinanceApiException.ENV_ERROR,
                        "[Invoking] Cannot get the response from server");
            }
//            log.info("Response =====> " + str);
            JsonWrapper jsonWrapper = JsonWrapper.parseFromString(str);
            checkResponse(jsonWrapper);
            return request.jsonParser.parseJson(jsonWrapper);
        } catch (BinanceApiException e) {
            throw e;
        } catch (Exception e) {
            throw new BinanceApiException(BinanceApiException.ENV_ERROR,
                    "[Invoking] Unexpected error: " + e.getMessage());
        }
    }

    static <T> T callSync1(RestApiRequest<T> request) {
        try {
            String str;
            log.info("Request URL " + request.request.url());
            log.info("Request URL " + request.request.method());
            Call call = client.newCall(request.request);
            Response response = call.execute();
            // System.out.println(response.body().string());
            if (response != null && response.body() != null) {
                str = response.body().string();
                response.close();
            } else {
                throw new BinanceApiException(BinanceApiException.ENV_ERROR,
                        "[Invoking] Cannot get the response from server");
            }
            log.info("Response =====> " + str);
            JsonWrapper jsonWrapper = JsonWrapper.parseFromString(str);
            checkResponse(jsonWrapper);
            return request.jsonParser.parseJson(jsonWrapper);
        } catch (BinanceApiException e) {
            throw e;
        } catch (Exception e) {
            throw new BinanceApiException(BinanceApiException.ENV_ERROR,
                    "[Invoking] Unexpected error: " + e.getMessage());
        }
    }

    static WebSocket createWebSocket(Request request, WebSocketListener listener) {
        return client.newWebSocket(request, listener);
    }

}
