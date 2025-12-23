package com.example.restservice.serial;

import com.google.gson.Gson;
import lombok.Data;
import org.springframework.boot.json.GsonJsonParser;
import org.springframework.http.converter.json.GsonBuilderUtils;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @className: App
 * @author: geeker
 * @date: 8/29/25 8:25 AM
 * @Version: 1.0
 * @description:
 */
public class SerialApp {
    public static void main(String[] args) {
        GsonBuilderUtils.gsonBuilderWithBase64EncodedByteArrays();

        GsonJsonParser jsonParser = new GsonJsonParser();
        //jsonParser.parseList("");

        Gson gson = new Gson();
        gson.toJson(1);
        gson.toJson("abcd");
        gson.toJson(Long.valueOf(2));
        int[] values = {1};
        gson.toJson(values);


        // d-ser
        int iv = gson.fromJson("1", Integer.class);
        long lv = gson.fromJson("1", Long.class);
        String i = gson.fromJson("1", String.class);

        String[] arr = gson.fromJson("[abc,123]",String[].class);

        System.out.println(Arrays.asList(arr));

        BagObj bagObj = new BagObj();
        System.out.println(gson.toJson(bagObj));

        BagObj bagObj1 = gson.fromJson("{a:5, v:666, larr:[1,2,3]}", BagObj.class);
        System.out.println(bagObj1);

    }


    @Data
    static class BagObj {
        private int a = 1;
        private transient String v = "123";
        private long[] larr = {123,1222};
    }

    // Inner class


}
