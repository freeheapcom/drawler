package com.freeheap.drawler.drivers;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by william on 7/11/16.
 */
public class DbConf {

    public static Map<String, Integer> parseHost(String connStr) {
        Map<String, Integer> result = new HashMap<>();

        String[] split = connStr.split(",");

        for (String s : split) {
            try {
                String[] tmp = s.split(":");
                if (tmp.length == 2) {
                    result.put(tmp[0], Integer.parseInt(tmp[1]));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
