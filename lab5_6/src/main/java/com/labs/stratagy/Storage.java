package com.labs.stratagy;

import org.json.JSONObject;

import java.util.Map;

public interface Storage {
    void storeData(JSONObject jsonObject, Object connect);
}
