package org.jba

import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._

import org.jba.spark.RDDGroupOps._

case class Item(score: Int, categoryId: Int, content: String)

object TopK {
  def apply(master: String, args: List[String]) {
    val sc = new SparkContext(master, "TopK", null, Nil)
    val input = sc.textFile(args(0))
    
    val items = input.map(toItem)
    val itemsById = items.groupBy(_.categoryId)
    
    val topItems = itemsById.sortedTake(2)(Ordering.by(_.score))
    
    topItems
      .map { case(ind, seq) => s"$ind -> ${seq.mkString}"  }
      .saveAsTextFile(args(1))        
  }

  def toItem(s: String): Item = {
    val arr = s.split("\t")
    Item(arr(0).toInt,arr(1).toInt,arr(2))
  }  
}