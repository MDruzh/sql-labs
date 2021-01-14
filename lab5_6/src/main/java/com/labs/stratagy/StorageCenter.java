package com.labs.stratagy;

import org.json.JSONObject;
import org.springframework.stereotype.Component;

public class StorageCenter {
    private Storage storage;

    public StorageCenter(Storage storage) {
        this.storage = storage;
    }

    public void setStorage(Storage storage) {
        this.storage = storage;
    }

    public void storeData(JSONObject jsonObject,Object connect){
         storage.storeData(jsonObject,connect);
    }
}
