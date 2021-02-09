/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  okhttp3.Request$Builder
 */
package com.ddylan.hydrogen.util;

import com.ddylan.hydrogen.Settings;
import okhttp3.Request;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

public class HttpUtils {
    public static void authorize(Request.Builder builder) {
        builder.header("MHQ-Authorization", Settings.getApiKey());
    }

    public static String generateQueryString(Map<String, Object> parameters) {
        StringBuilder queryBuilder = new StringBuilder("?");
        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            if (queryBuilder.length() > 1) {
                queryBuilder.append("&");
            }
            try {
                queryBuilder.append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue().toString(), "UTF-8"));
            }
            catch (UnsupportedEncodingException unsupportedEncodingException) {}
        }
        return queryBuilder.toString();
    }
}

