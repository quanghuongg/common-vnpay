package vn.vnpay.utils;

import vn.vnpay.logs.WriteLog;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Map;

public class RestUtil {

    private final static int timeout = 3000;

    public static String doGet(String url) throws IOException {

        HttpGet httpGet = new HttpGet(url);

        httpGet.setHeader(HttpHeaders.ACCEPT, "application/json");
        httpGet.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");

        CloseableHttpResponse response = HttpClients.createDefault().execute(httpGet);

        if (response.getStatusLine().getStatusCode() == 200) {
            return EntityUtils.toString(response.getEntity(), "UTF-8");
        }

        httpGet.releaseConnection();
        return null;
    }


    public static String doGetSetTimeout(String url, int timeout) throws IOException {

        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader(HttpHeaders.ACCEPT, "application/json");
        httpGet.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");

        final HttpParams httpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, timeout);
        HttpClient httpClient = new DefaultHttpClient(httpParams);
        HttpResponse httpResponse = httpClient.execute(httpGet);
        if (httpResponse.getStatusLine().getStatusCode() == 200) {
            return EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
        }

        httpGet.releaseConnection();
        return null;
    }

    public static String doPost(String url, Object data, Map<String, String> headers) throws IOException {

        HttpPost httpPost = new HttpPost(url);

        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(timeout)
                .setConnectionRequestTimeout(timeout)
                .setSocketTimeout(timeout)
                .build();

        httpPost.setConfig(requestConfig);
        httpPost.setHeader(HttpHeaders.ACCEPT, "application/json");
        httpPost.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
        if (headers != null) {
            for (String key : headers.keySet()) {
                httpPost.setHeader(key, headers.get(key));
            }
        }

        if (Utils.isNotEmpty(data)) {
            String body = JsonUtil.toJsonString(data);
            WriteLog.info("request body " + body);
            StringEntity encodedFormEntity = new StringEntity(body, "UTF-8");
            httpPost.setEntity(encodedFormEntity);
        }

        WriteLog.info("call " + url);
        CloseableHttpResponse response = HttpClients.createDefault().execute(httpPost);
        WriteLog.info("response status " + response.getStatusLine().getStatusCode());

        if (response.getStatusLine().getStatusCode() == 200) {
            String body = EntityUtils.toString(response.getEntity(), "UTF-8");
            WriteLog.info("response body:" + body);
            return body;
        }
        httpPost.releaseConnection();
        return null;
    }

    public static String doPostGetResponse(String url, Object data, Map<String, String> headers) throws IOException {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader(HttpHeaders.ACCEPT, "application/json");
        httpPost.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
        for (String key : headers.keySet()) {
            httpPost.setHeader(key, headers.get(key));
        }

        if (Utils.isNotEmpty(data)) {
            String body = JsonUtil.toJsonString(data);
            WriteLog.info("request body " + body);
            StringEntity encodedFormEntity = new StringEntity(body, "UTF-8");
            httpPost.setEntity(encodedFormEntity);
        }

        WriteLog.info("call " + url);
        CloseableHttpResponse response = HttpClients.createDefault().execute(httpPost);
        WriteLog.info("response status " + response.getStatusLine().getStatusCode());


        String body = EntityUtils.toString(response.getEntity(), "UTF-8");
        WriteLog.info("response body:" + body);
        httpPost.releaseConnection();
        return body;
    }
}
