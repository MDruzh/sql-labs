package com.labs.stratagy;

import org.json.JSONObject;

public class Console implements Storage {
    public void storeData(JSONObject jsonObject, Object connect) {
        System.out.println(jsonObject);
    }
}
