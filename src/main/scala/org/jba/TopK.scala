package org.jba

import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._

import org.jba.spark.RDDGroupOps._

case class Item(score: Int, categoryId: Int, content: String)

object ItemOrdering extends Ordering[Item] {
  def compare(a: Item, b: Item) = {
    val comp1 = a.score compare b.score
    if(comp1 != 0) comp1
    else a.content compare b.content
  }
}


object TopK {
  def apply(master: String, args: List[String]) {
    val sc = new SparkContext(master, "TopK", null, Nil)
    val input = sc.textFile(args(0))
    
    val items = input.map(toItem)
    val itemsById = items.groupBy(_.categoryId)
    
    val topItems = itemsById.sortedTake(2)(ItemOrdering)
    
    topItems
      .map { pp }
      .saveAsTextFile(args(1))        
  }

  def pp(x: (Int,List[Item])) = x match { 
    case(ind,seq) => s"$ind -> ${seq.mkString}" 
  }
  
  def toItem(s: String): Item = {
    val arr = s.split("\t",3)
    Item(arr(0).toInt,arr(1).toInt,arr(2))
  }  
}