# Labs 8/9
1. Створив нову Resource group та Azure Databricks.
![alt text](screenshots/1.png "Опис")
2. Створив Storage account. В Advanced налаштуваннях дозволив Hierarchical namespace.
![alt text](screenshots/2.png "Опис")
3. Створив контейнер всередирі Storage account.
![alt text](screenshots/3.png "Опис")
4. Створив директорію всередині контейнеру.
![alt text](screenshots/4.png "Опис")
5. Всередині воркспейсу Azure Databricks створив кластер.
![alt text](screenshots/5.png "Опис")
6. Створив нову App registration в Azure Active Directory.
![alt text](screenshots/6.png "Опис")
7. Додав новий client secret.
8. Налаштував доступ у сторедж акаунті у розділі Access Control (IAM).
![alt text](screenshots/7.png "Опис")
9. Всередині кластеру інсталював необхідну бібліотеку.
![alt text](screenshots/8.png "Опис")
10. Створив 2 ноутбуки в Azure Databricks, на пайтоні та скалі відповідно.
![alt text](screenshots/9.png "Опис")
11. У ноутбук з пайтоном вставив наступний код для конфігурації. Усі необхідні ключі потрібно взяти із власної App registation. 

configs = {"fs.azure.account.auth.type": "OAuth",
         "fs.azure.account.oauth.provider.type": "org.apache.hadoop.fs.azurebfs.oauth2.ClientCredsTokenProvider",
         "fs.azure.account.oauth2.client.id": "131a35ab-36ce-46cc-b5f0-ee92e2cf9903",
         "fs.azure.account.oauth2.client.secret": "CN-1~Xf52V27UkmU4LGtsPn--NZeUZqYqQ",
         "fs.azure.account.oauth2.client.endpoint": "https://login.microsoftonline.com/540857fe-3605-46a4-a8fe-866dab25b840/oauth2/token",
         "fs.azure.createRemoteFileSystemDuringInitialization": "true"}

dbutils.fs.mount(
        source = "abfss://lab9container@lab9storageacc.dfs.core.windows.net",
        mount_point = "/mnt/labs",
        extra_configs = configs)

12. Встановив аплікацію Azure Storage Explorer.
13. Налаштував доступ до створеної раніше директорії.
![alt text](screenshots/10.png "Опис")
14. Для скали ноутбука вставив наступний код.

import org.apache.spark.eventhubs.{ ConnectionStringBuilder, EventHubsConf, EventPosition }
import org.apache.spark.sql.types._
import org.apache.spark.sql.functions._

// To connect to an Event Hub, EntityPath is required as part of the connection string.
// Here, we assume that the connection string from the Azure portal does not have the EntityPath part.
val appID = "131a35ab-36ce-46cc-b5f0-ee92e2cf9903"
val password = "CN-1~Xf52V27UkmU4LGtsPn--NZeUZqYqQ"
val tenantID = "540857fe-3605-46a4-a8fe-866dab25b840"
val fileSystemName = "lab9container";
var storageAccountName = "lab9storageacc";
val connectionString = ConnectionStringBuilder("Endpoint=sb://labfive.servicebus.windows.net/;SharedAccessKeyName=RootManageSharedAccessKey;SharedAccessKey=vTWd+WE3a8-9aess0U62ZVBOyS0XGJBlObvp6C2afXxas=")
  .setEventHubName("labfive")
  .build
val eventHubsConf = EventHubsConf(connectionString)
  .setStartingPosition(EventPosition.fromEndOfStream)

var dataset = 
  spark.readStream
    .format("eventhubs")
    .options(eventHubsConf.toMap)
    .load()
      
val filtered = dataset.select(
    from_unixtime(col("enqueuedTime").cast(LongType)).alias("enqueuedTime")
      , get_json_object(col("body").cast(StringType), "$.year").alias("year")
      , get_json_object(col("body").cast(StringType), "$.leading_case").alias("leading_case")
      , get_json_object(col("body").cast(StringType), "$.sex").alias("sex")
        , get_json_object(col("body").cast(StringType), "$.race").alias("race")
        , get_json_object(col("body").cast(StringType), "$.deaths").alias("deaths").cast(DoubleType)
        , get_json_object(col("body").cast(StringType), "$.death_rate").alias("death_rate").cast(DoubleType)
        , get_json_object(col("body").cast(StringType), "$.death_rate_adjusted").alias("death_rate_adjusted").cast(DoubleType)
  )
  
filtered.writeStream
  .format("com.databricks.spark.csv")
  .outputMode("append")
  .option("checkpointLocation", "/mnt/labs/lab9dir")
  .start("/mnt/labs/lab9dir")

  15. Після виконання у директорії успішно згенерувались дані.
![alt text](screenshots/11.png "Опис")










