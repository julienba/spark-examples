package org.jba

import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.rdd.RDD

import com.twitter.algebird._


object WordCount {
  
  
  def execute(master: String, args: List[String]) = {
    val sc = new SparkContext(master, "WordCountJob", null, Nil)
    val input = sc.textFile(args(0))
    val words = input.flatMap(line => tokenize(line))  
    val wordCounts: RDD[(String,Int)] = words.map(x => (x, 1)).reduceByKey(_ + _)
    wordCounts.saveAsTextFile(args(1))
  }
  
  // ~ with Monoid
  def execute2(master: String, args: List[String]) = {
    val sc = new SparkContext(master, "WordCountJob", null, Nil)
    val input = sc.textFile(args(0))
    val words = input.flatMap(line => tokenize(line))
    val wordCounts = words.map(x => Map(x -> 1))
    val result: RDD[(String,Int)] = wordCounts.mapPartitions { iter =>
      Monoid.sum(iter).toIterator
    }
     
    result.saveAsTextFile(args(1))   
  }
  
  
  def tokenize(text : String) : Array[String] = {
    // Lowercase each word and remove punctuation.
    text.toLowerCase.replaceAll("[^a-zA-Z0-9\\s]", "").split("\\s+")
  } 
}
