package com.ddylan.hydrogen.api.util;

import com.nimbusds.jose.shaded.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public final class ResponseUtil {
    private ResponseUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static final ResponseEntity<JSONObject> success;

    static {
        JSONObject json = new JSONObject();
        json.put("success", Boolean.valueOf(true));
        success = new ResponseEntity(json, HttpStatus.OK);
    }
}
