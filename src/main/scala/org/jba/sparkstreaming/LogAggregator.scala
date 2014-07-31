package org.jba.sparkstreaming

import org.apache.spark.streaming._
import org.apache.spark.streaming.StreamingContext._
import org.apache.spark.streaming.api._

import play.api.libs.json.Json
import play.api.libs.functional.syntax._

import org.jba.model.EventLog
import org.jba.model.Model._

object LogAggregator extends App with org.jba.mongo.MongoHelper {
  val ssc = new StreamingContext("local[2]", "LogAggregator", Seconds(5))
  
  val dataDirectory = args(0)
    
  val lines = ssc.textFileStream(dataDirectory)
  val jsonLines = lines.map(line => eventFormat.reads(Json.parse(line)).get)
  
  val eventByType = jsonLines.map( e => (e.`type`,e)).groupByKey
  
  val sortEvents = eventByType.map { case(tpe, events) => (tpe, events.toList.sortBy(_.timestamp))}
  
  sortEvents.repartition(10).foreachRDD { rrd =>
    val listOfList = rrd.collect
    
    import reactivemongo.bson._
    import reactivemongo.api._
    import scala.concurrent.ExecutionContext.Implicits.global      
  
    val coll = connect("myevents")

    listOfList.foreach { case(tpe, allEvents) =>
      
      val eventsBatch = allEvents.grouped(100)
      
      eventsBatch.foreach { events =>
        val mongoEvents = events.map( e => BSONDocument("time" -> e.timestamp, "content" -> e.content))
        val doc = BSONDocument("type" -> tpe, "contents" -> mongoEvents)
        coll.uncheckedInsert(doc)
      }
    } 
  }
  
  
//  jsonLines.foreachRDD { rrd =>
//    val list = rrd.collect
//    
//    import reactivemongo.bson._
//    import reactivemongo.api._
//    import scala.concurrent.ExecutionContext.Implicits.global      
//  
//    val coll = connect("myevents")
//
//    list.foreach { case(e) =>      
//      val doc = BSONDocument("type" -> e.`type`, "content" -> e.content, "timestamp" -> e.timestamp)
//      coll.save(doc)
//    }
//  }
  
  ssc.start()
  ssc.awaitTermination() 

}