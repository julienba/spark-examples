package org.jba.mongo

import reactivemongo.api._
import reactivemongo.bson._

import scala.concurrent.ExecutionContext.Implicits.global

import play.api.libs.iteratee.Iteratee

trait MongoHelper {
  def connect(colName: String) = {
	
	// gets an instance of the driver
	// (creates an actor system)
	val driver = new MongoDriver
	val connection = driver.connection(List("localhost"), Nil, 2)

	// Gets a reference to the database "plugin"
	val db = connection("test")

	// Gets a reference to the collection "acoll"
	// By default, you get a BSONCollection.
	val collection = db(colName)
	collection
  }  
}

object ReadTest extends MongoHelper {

  
  def main(args: Array[String]) {
    
    val coll = connect("myevents")
    
    val doc = BSONDocument( "uid" -> "xxf")
    coll.insert(doc)
  }
}