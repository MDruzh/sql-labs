package com.labs.stratagy;

import com.microsoft.azure.eventhubs.ConnectionStringBuilder;
import com.microsoft.azure.eventhubs.EventData;
import com.microsoft.azure.eventhubs.EventHubClient;
import com.microsoft.azure.eventhubs.EventHubException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class EventHub implements Storage {
    Logger logger = LoggerFactory.getLogger(EventHub.class);

    public void storeData(JSONObject jsonObject, Object connect) {
        try {
            EventHubClient ehClient = (EventHubClient) connect;
            byte[] payloadBytes = jsonObject.toString().getBytes(Charset.defaultCharset());
            EventData sendEvent = EventData.create(payloadBytes);
            ehClient.sendSync(sendEvent);
        }catch (Exception e){
            logger.error("Error while writing data to Event Hub", e);
        }

    }



}
