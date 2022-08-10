package io.uhha.common.utils.http;

import io.uhha.common.utils.StringUtils;
import okhttp3.*;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class OKHttp3ClientUtils {

    private static final int READ_TIMEOUT = 100;
    private static final int CONNECT_TIMEOUT = 60;
    private static final int WRITE_TIMEOUT = 60;
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final byte[] LOCKER = new byte[0];
    private static OKHttp3ClientUtils mInstance;
    private OkHttpClient okHttpClient;

    private OKHttp3ClientUtils() {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        // 读取超时
        clientBuilder.readTimeout(READ_TIMEOUT, TimeUnit.SECONDS);
        // 连接超时
        clientBuilder.connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS);
        //写入超时
        clientBuilder.writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS);
        okHttpClient = clientBuilder.build();
    }

    /**
     * 单例模式获取 NetUtils
     *
     * @return {@link OKHttp3ClientUtils}
     */
    public static OKHttp3ClientUtils getInstance() {
        if (mInstance == null) {
            synchronized (LOCKER) {
                if (mInstance == null) {
                    mInstance = new OKHttp3ClientUtils();
                }
            }
        }
        return mInstance;
    }

    /**
     * GET，同步方式，获取网络数据
     *
     * @param url 请求地址
     * @return {@link Response}
     */
    public Response getData(String url) throws IOException {
        // 构造 Request
        Request.Builder builder = new Request.Builder();
        Request request = builder.get().url(url).build();
        // 将 Request 封装为 Call
        Call call = okHttpClient.newCall(request);
        // 执行 Call，得到 Response
        Response response = call.execute();
        return response;
    }

    /**
     * GET，同步方式，获取网络数据
     *
     * @param url 请求地址
     * @return {@link Response}
     */
    public Response getData(String url, Map<String, String> headerParams) throws IOException {
        // 构造 Request
        Request.Builder builder = new Request.Builder();
        // 构造 headers
        Headers.Builder headersBuilder = new Headers.Builder();
        headerParams.forEach(headersBuilder::add);

        Request request = builder
                .headers(headersBuilder.build())
                .get().url(url).build();
        // 将 Request 封装为 Call
        Call call = okHttpClient.newCall(request);
        // 执行 Call，得到 Response
        Response response = call.execute();
        return response;
    }

    /**
     * GET，同步方式，获取网络数据
     *
     * @param url 请求地址
     * @return {@link Response}
     */
    public Response     getData(String url, Map<String, String> requestParams, Map<String, String> headerParams) throws IOException {

        HttpUrl.Builder httpBuilder = HttpUrl.parse(url).newBuilder();
        if (requestParams != null) {
            for(Map.Entry<String, String> param : requestParams.entrySet()) {
                httpBuilder.addQueryParameter(param.getKey(),param.getValue());
            }
        }

        // 构造 Request
        Request.Builder builder = new Request.Builder();
        // 构造 headers
        Headers.Builder headersBuilder = new Headers.Builder();
        if(headerParams!=null) {
            headerParams.forEach(headersBuilder::add);
        }

        Request request = builder
                .headers(headersBuilder.build())
                .get().url(httpBuilder.build()).build();
        // 将 Request 封装为 Call
        Call call = okHttpClient.newCall(request);
        // 执行 Call，得到 Response
        Response response = call.execute();
        return response;
    }

    /**
     * POST 请求，同步方式，提交数据
     *
     * @param url        请求地址
     * @param bodyParams 请求参数
     * @return {@link Response}
     */
    public Response postData(String url, Map<String, String> bodyParams) throws IOException {
        // 构造 RequestBody
        RequestBody body = setRequestBody(bodyParams);
        // 构造 Request
        Request.Builder requestBuilder = new Request.Builder();
        Request request = requestBuilder.post(body).url(url).build();
        // 将 Request 封装为 Call
        Call call = okHttpClient.newCall(request);
        // 执行 Call，得到 Response
        Response response = call.execute();
        return response;
    }

    /**
     * POST 请求，同步方式，提交数据
     *
     * @param url        请求地址
     * @param bodyParams 请求参数
     * @return {@link Response}
     */
    public Response postData(String url,Map<String, String> headerParams, Map<String, String> bodyParams) throws IOException {
        // 构造 RequestBody
        RequestBody body = setRequestBody(bodyParams);
        // 构造 headers
        Headers.Builder headersBuilder = new Headers.Builder();
        headerParams.forEach(headersBuilder::add);

        // 构造 Request
        Request.Builder requestBuilder = new Request.Builder();
        Request request = requestBuilder
                .headers(headersBuilder.build())
                .post(body)
                .url(url).build();
        // 将 Request 封装为 Call
        Call call = okHttpClient.newCall(request);
        // 执行 Call，得到 Response
        Response response = call.execute();
        return response;
    }

    /**
     * POST 请求，同步方式，提交数据
     *
     * @param url        请求地址
     * @param strBody 请求参数
     * @return {@link Response}
     */
    public Response postData(String url,Map<String, String> headerParams, String strBody) throws IOException {
        // 构造 RequestBody
        RequestBody body = RequestBody.create(strBody, JSON);
        // 构造 headers
        Headers.Builder headersBuilder = new Headers.Builder();
        headerParams.forEach(headersBuilder::add);

        // 构造 Request
        Request.Builder requestBuilder = new Request.Builder();
        Request request = requestBuilder
                .headers(headersBuilder.build())
                .post(body)
                .url(url).build();
        // 将 Request 封装为 Call
        Call call = okHttpClient.newCall(request);
        // 执行 Call，得到 Response
        Response response = call.execute();
        return response;
    }

    /**
     * POST 请求，同步方式，提交数据
     *
     * @param url        请求地址
     * @param strBody 请求参数
     * @return {@link Response}
     */
    public Response postData(String url,String strBody) throws IOException {
        // 构造 RequestBody
        RequestBody body = RequestBody.create(strBody, JSON);

        // 构造 Request
        Request.Builder requestBuilder = new Request.Builder();
        Request request = requestBuilder
                .post(body)
                .url(url).build();
        // 将 Request 封装为 Call
        Call call = okHttpClient.newCall(request);
        // 执行 Call，得到 Response
        Response response = call.execute();
        return response;
    }

    /**
     * GET 请求，异步方式，获取网络数据
     *
     * @param url       请求地址
     * @param myNetCall 回调函数
     */
    public void getDataAsync(String url, final MyNetCall myNetCall) {
        // 构造 Request
        Request.Builder builder = new Request.Builder();
        Request request = builder.get().url(url).build();
        // 将 Request 封装为 Call
        Call call = okHttpClient.newCall(request);
        // 执行 Call
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                myNetCall.failed(call, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                myNetCall.success(call, response);
            }
        });
    }

    /**
     * POST 请求，异步方式，提交数据
     *
     * @param url        请求地址
     * @param bodyParams 请求参数
     * @param myNetCall  回调函数
     */
    public void postDataAsync(String url, Map<String, String> bodyParams, final MyNetCall myNetCall) {
        // 构造 RequestBody
        RequestBody body = setRequestBody(bodyParams);
        // 构造 Request
        buildRequest(url, myNetCall, body);
    }

    /**
     * 同步 POST 请求，使用 JSON 格式作为参数
     *
     * @param url  请求地址
     * @param json JSON 格式参数
     * @return 响应结果
     * @throws IOException 异常
     */
    public String postJson(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(json, JSON);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = okHttpClient.newCall(request).execute();
        if (response.isSuccessful()) {
            return response.body().string();
        } else {
            throw new IOException("Unexpected code " + response);
        }
    }

    /**
     * 异步 POST 请求，使用 JSON 格式作为参数
     *
     * @param url       请求地址
     * @param json      JSON 格式参数
     * @param myNetCall 回调函数
     * @throws IOException 异常
     */
    public void postJsonAsync(String url, String json, final MyNetCall myNetCall) throws IOException {
        RequestBody body = RequestBody.create(json, JSON);
        // 构造 Request
        buildRequest(url, myNetCall, body);
    }

    /**
     * 构造 POST 请求参数
     *
     * @param bodyParams 请求参数
     * @return {@link RequestBody}
     */
    private RequestBody setRequestBody(Map<String, String> bodyParams) {
        RequestBody body = null;
        FormBody.Builder formEncodingBuilder = new FormBody.Builder();
        if (bodyParams != null) {
            Iterator<String> iterator = bodyParams.keySet().iterator();
            String key = "" ;
            while (iterator.hasNext()) {
                key = iterator.next().toString();
                formEncodingBuilder.add(key, bodyParams.get(key));
            }
        }
        body = formEncodingBuilder.build();
        return body;
    }

    /**
     * 构造 Request 发起异步请求
     *
     * @param url       请求地址
     * @param myNetCall 回调函数
     * @param body      {@link RequestBody}
     */
    private void buildRequest(String url, MyNetCall myNetCall, RequestBody body) {
        Request.Builder requestBuilder = new Request.Builder();
        Request request = requestBuilder.post(body).url(url).build();
        // 将 Request 封装为 Call
        Call call = okHttpClient.newCall(request);
        // 执行 Call
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                myNetCall.failed(call, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                myNetCall.success(call, response);
            }
        });
    }

    public static String getParams(Map<String, String> params) {
        StringBuffer sb = new StringBuffer("?");
        if (StringUtils.isNotEmpty(params)) {
            for (Map.Entry<String, String> item : params.entrySet()) {
                String value = item.getValue();
                if (StringUtils.isNotEmpty(value)) {
                    sb.append("&");
                    sb.append(item.getKey());
                    sb.append("=");
                    sb.append(value);
                }
            }
            return sb.toString();
        } else {
            return "";
        }
    }

    /**
     * 自定义网络回调接口
     */
    public interface MyNetCall {
        /**
         * 请求成功的回调处理
         *
         * @param call     {@link Call}
         * @param response {@link Response}
         * @throws IOException 异常
         */
        void success(Call call, Response response) throws IOException;

        /**
         * 请求失败的回调处理
         *
         * @param call {@link Call}
         * @param e    异常
         */
        void failed(Call call, IOException e);
    }

}
