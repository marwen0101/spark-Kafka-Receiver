# spark-Kafka-Receiver
the scoop of this repository is to give a short hand tutorial on how to build spark-kafk receiver. 
The project is using Maven for the build. Here are the dependencies elements related to kafka  and spark to add to the pom file:
```xml
   <dependency>
			<groupId>org.apache.spark</groupId>
			<artifactId>spark-core_2.10</artifactId>
			<version>1.6.0</version>
		</dependency>
		<dependency>
			<groupId>org.apache.spark</groupId>
			<artifactId>spark-streaming_2.10</artifactId>
			<version>1.6.0</version>
		</dependency>
		<dependency>
			<groupId>org.apache.spark</groupId>
			<artifactId>spark-streaming-kafka_2.10</artifactId>
			<version>1.6.0</version>
		</dependency>
```
## 1) Starting zookeeper
the zookeeper kafka version built is used:
``` shell
./bin/zookeeper-server-start.sh config/zookeeper.properties
```
## 2) Starting kafka broker
After runing zookeeper, we can now sart the  kafka broker using this command:
``` shell
./bin/kafka-server-start.sh config/server.properties
```
## 3) Creating kafka topic

A topic is where the messages will be sent by a producer and where a consumer will read. The topic spark-topic is created using the following command:
``` shell
./bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic spark-topic
``` 
if you want check if the topic has been created you can use the following command wich lists the available topics list:

``` shell
./bin/kafka-topics.sh --list --zookeeper localhost:2181
``` 

## 4) Start sending
we can satrt sending message by creating a kafka producer using the following command:
``` shell
./bin/kafka-console-producer.sh --broker-list localhost:9092 --topic spark-topic
hi this is my firts message
hi hi this is my second message
```
## 5) start Receiving

we can receive the message using a kafka console consumer according to this command:
``` shell
./bin/kafka-console-consumer.sh --zookeeper localhost:2181 --topic spark-topic --from-beginning
hi this is my firts message
hi hi this is my second message
```
## 6) Integration with spark 
Spark has streaming lib and kafka streaming lib to handle the integration with kafka
-spark-streaming
-spark-streaming-kafka
 
 To develop spark-kafka receiver, we start by creationg the streaming application and further we create the spark-kafka consumer
 - creating streaming contexct:
 
 ``` scala
  import org.apache.spark.SparkContext
  import org.apache.spark.SparkConf
  import org.apache.spark.streaming.StreamingContext
  import org.apache.spark.streaming.Seconds
  object Receiver {
    def main (args: Array[String]){
      val conf = new SparkConf().setAppName("sparkReceiver").setMaster("local[8]")
      val ssc = new StreamingContext(conf, Seconds(20))
 ``` 
 - create consumer
 ``` scala 
    import org.apache.spark.streaming.kafka.KafkaUtils
    val kafkaStream = KafkaUtils.createStream(ssc, "localhost:2181", "spark-consumer", Map("spark-topic" -> 5))
    kafkaStream.print()
    ssc.start()
    ssc.awaitTermination(2000)
    
  }
  
}
 ``` 
 Now the spark consumer is connected to kafka and receives meagges from the spark-topic every 2à seconds.
