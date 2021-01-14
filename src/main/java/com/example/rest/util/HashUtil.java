package com.example.rest.util;

import org.apache.commons.codec.digest.DigestUtils;

public class HashUtil {

    private HashUtil() {
    }

    public static String sha256hex(String data, int times) {

        do {
            data = DigestUtils.sha256Hex(data);
        } while (--times > 0);

        return data;
    }
}