package com.labs;

import com.labs.dao.RedisRepository;
import com.labs.stratagy.Console;
import com.labs.stratagy.EventHub;
import com.labs.stratagy.StorageCenter;
import com.microsoft.azure.eventhubs.ConnectionStringBuilder;
import com.microsoft.azure.eventhubs.EventHubClient;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@RestController
public class MainController {
    Logger logger = LoggerFactory.getLogger(MainController.class);
    @Autowired
    RedisRepository redisRepository;

    @Value("${eventhub.namespace}")
    private String namespace;

    @Value("${eventhub.hub.name}")
    private String hubName;

    @Value("${eventhub.connection.string}")
    private String connectionString;

    @Value("${eventhub.primary.key}")
    private String privateKey;

    @Value("${api.url}")
    private String url;


    @GetMapping("/url")
    public void addNewUrl() {
        String[] spitedUrl = url.split("/");
        ConnectionStringBuilder connStr = new ConnectionStringBuilder()
                .setNamespaceName(namespace)
                .setEventHubName(hubName)
                .setSasKeyName(connectionString)
                .setSasKey(privateKey);
        ScheduledExecutorService executorService = null;
        EventHubClient ehClient = null;
        try {
            String name = spitedUrl[spitedUrl.length-1];
            boolean isExist = checkIfExist(name);
            if (!isExist) {
                redisRepository.addFile(name, "Repeat");
                store(ehClient, executorService, connStr.toString(), isExist);
                redisRepository.addFile(name, "Completed");

            }
        } catch (Exception e) {
            logger.error("Can not connect to url:" + url);
        }

    }

    private void store(EventHubClient ehClient, ScheduledExecutorService executorService, String connection, boolean isExist) {
        try {
            System.out.println("================================================================================");
            System.out.println("                                    CONSOLE                                     ");
            System.out.println("================================================================================");
            StorageCenter storage = new StorageCenter(new Console());
            JSONArray jsonArray = groupDataIntoList();
            int mid = jsonArray.length() / 2;
            int limit = 100;
            int lastIndex = 1;
            int counter = 0;
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if (mid == i) {
                    System.out.println("================================================================================");
                    System.out.println("                                    EVENT HUB                                   ");
                    System.out.println("================================================================================");
                    storage.setStorage(new EventHub());
                    executorService = Executors.newScheduledThreadPool(4);
                    ehClient = EventHubClient.createSync(connection, executorService);
                }
                if (i == limit) {
                    counter++;
                    redisRepository.addRaws(String.valueOf(counter), lastIndex + ":" + limit);
                    limit = limit + 100;
                    lastIndex = i + 1;
                }

                storage.storeData(jsonObject, ehClient);
            }
            counter++;
            redisRepository.addRaws(String.valueOf(counter), lastIndex + ":" + String.valueOf(jsonArray.length()));
        } catch (Exception e) {
            logger.error("Error while storing data");
        } finally {
            try {
                ehClient.closeSync();
                executorService.shutdown();
            } catch (Exception e) {
                logger.error("Can not close connection to Event Hub", e);
            }
            System.out.println(redisRepository.findAllFile());
            System.out.println(redisRepository.findAllRaws());
        }
    }

    private boolean checkIfExist(String name) {
        if (redisRepository.findOneFile(name) != null) {
            return true;
        }
        return false;
    }

    private JSONArray groupDataIntoList() throws IOException {
        URL data = new URL(url);
        HttpURLConnection con = (HttpURLConnection) data.openConnection();
        BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
        StringBuilder response = new StringBuilder();
        String inputLine;
        while ((inputLine = br.readLine()) != null) {
            response.append(inputLine);
        }
        br.close();
        return new JSONArray(response.toString());
    }
}
