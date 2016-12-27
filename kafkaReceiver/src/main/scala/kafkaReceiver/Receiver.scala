package kafkaReceiver

import org.apache.spark.SparkContext
import org.apache.spark.SparkConf
import org.apache.spark.streaming.StreamingContext
import org.apache.spark.streaming.Seconds
import org.apache.spark.streaming.kafka.KafkaUtils

object Receiver {
  def main (args: Array[String]){
    val conf = new SparkConf().setAppName("sparkReceiver").setMaster("local[8]")
    val ssc = new StreamingContext(conf, Seconds(10))
    val kafkaStream = KafkaUtils.createStream(ssc, "localhost:2181", "spark-consumer", Map("spark-topic" -> 5))
    kafkaStream.print()
    ssc.start()
    ssc.awaitTermination(2000)
    
  }
  
}