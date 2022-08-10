/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package io.uhha.coin.common.api;

import com.aliyun.oss.common.auth.HmacSHA1Signature;
import io.uhha.coin.common.util.DateUtils;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.aliyun.oss.common.utils.CodingUtils.assertTrue;

public class SignUtils {

    private static final String NEW_LINE = "\n";

    public static String buildCanonicalString(String method, String resourcePath,
                                              Map<String, String> parameters) {

        StringBuilder canonicalString = new StringBuilder();

        //请求方式
        canonicalString.append(method + NEW_LINE);

        // 请求参数
        canonicalString.append(buildCanonicalizedResource(resourcePath, parameters));

        return canonicalString.toString();
    }

    private static String buildCanonicalizedResource(String resourcePath, Map<String, String> parameters) {

        assertTrue(resourcePath.startsWith("/"), "Resource path should start with slash character");

        StringBuilder builder = new StringBuilder();
        builder.append(resourcePath);

        if (parameters != null) {
            String[] parameterNames = parameters.keySet().toArray(
                    new String[parameters.size()]);
            Arrays.sort(parameterNames);

            char separater = '?';
            for (String paramName : parameterNames) {
                builder.append(separater);
                builder.append(paramName);
                String paramValue = parameters.get(paramName);
                if (paramValue != null) {
                    builder.append("=").append(paramValue);
                }

                separater = '&';
            }
        }

        return builder.toString();
    }

    /**
     * 验证签名是否合法
     *
     * @return
     */
    public static boolean isSignValid(HttpServletRequest request) {
        String accessKeyId = request.getParameter("AccessKeyId");
        String method = request.getMethod();
        String signature = "";

        String accessKeySecret = "tErJtyrof9RRS1Rreu6CtKHg54aKc9";
        String canonicalString = buildCanonicalString("GET", "/api", null);
        String newSign = HmacSHA1Signature.create().computeSignature(accessKeySecret, canonicalString);
        return newSign.equals(signature);
    }

    

    public static void main(String[] args) {
        String accessKeyId = "LTAInK7QsbIEgBvH";
        String accessKeySecret = "tErJtyrof9RRS1Rreu6CtKHg54aKc9";

        SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
        df2.setTimeZone(TimeZone.getTimeZone("UTC"));
        String timestamp = DateUtils.format(Calendar.getInstance().getTime(), df2);

        Map<String, String> parameters = new HashMap<>();
        parameters.put("username", "chenlei");
        parameters.put("userid", "123123");
        parameters.put("AccessKeyId", accessKeyId);
        parameters.put("Timestamp", timestamp);
        parameters.put("SignatureVersion", "1.0");
        parameters.put("SignatureMethod", "HMAC-SHA1");
        //parameters.put("Version","2016-01-20");
        //parameters.put("Format","json");
        //parameters.put("Action","CreateKey");

        String canonicalString = buildCanonicalString("GET", "/api", parameters);
        System.out.println(canonicalString);
        String signature = HmacSHA1Signature.create().computeSignature(accessKeySecret, canonicalString);
        System.out.println(signature);
    }


}
