import org.apache.spark.eventhubs.{ ConnectionStringBuilder, EventHubsConf, EventPosition }
import org.apache.spark.sql.types._
import org.apache.spark.sql.functions._

// To connect to an Event Hub, EntityPath is required as part of the connection string.
// Here, we assume that the connection string from the Azure portal does not have the EntityPath part.
val appID = "131a35ab-36ce-46cc-b5f0-ee92e2cf9903"
val password = "_06iwR6XAEIQ2Ph6-N~d9EY05~w7.R4xg-"
val tenantID = "540857fe-3605-46a4-a8fe-866dab25b840"
val fileSystemName = "newlabcontainer"
val storageAccountName = "lab9storageacc"
val connectionString = ConnectionStringBuilder("Endpoint=sb://labnine.servicebus.windows.net/;SharedAccessKeyName=RootManageSharedAccessKey;SharedAccessKey=dj0fTGzj6Jpve02D6dc+tV9OwDIWGorH/HoII1c59/Y=")
  .setEventHubName("labnine")
  .build
val eventHubsConf = EventHubsConf(connectionString)
  .setStartingPosition(EventPosition.fromEndOfStream)

var streamingInputDF = 
  spark.readStream
    .format("eventhubs")
    .options(eventHubsConf.toMap)
    .load()

val filtered = streamingInputDF.select (
  from_unixtime(col("enqueuedTime").cast(LongType)).alias("enqueuedTime")
     , get_json_object(col("body").cast(StringType), "$.boro").alias("boro")
     , get_json_object(col("body").cast(StringType), "$.vic_race").alias("vic_race")
     , get_json_object(col("body").cast(StringType), "$.vic_sex").alias("vic_sex")
     , get_json_object(col("body").cast(StringType), "$.vic_age_group").alias("vic_age_group")
)


filtered.writeStream
  .format("com.databricks.spark.csv")
  .outputMode("append")
  .option("checkpointLocation", "/mnt/newlabs/newdirectory/")
  .start("/mnt/newlabs/newdirectory/")