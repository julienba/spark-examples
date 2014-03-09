package org.jba.spark

import com.twitter.algebird._
import com.twitter.algebird.mutable.PriorityQueueMonoid

import org.apache.spark.rdd.RDD
import org.apache.spark.SparkContext._

import scala.reflect.ClassTag
import scala.collection.JavaConverters._


object RDDGroupOps {
  implicit def toOps[K: ClassTag,V: ClassTag](r: RDD[(K,Seq[V])]) = new RDDGroupOps(r)
} 

class RDDGroupOps[K: ClassTag,V: ClassTag](that: RDD[(K,Seq[V])]) {
    
  def sortedTake(k: Int)(implicit ord: Ordering[V]) = {
    that.mapValues { iter: Seq[V] =>    
      val mon = new PriorityQueueMonoid[V](k)
      mon.build(iter.toIterable).asScala.toList.sorted
    }
  }
  
  def sortedReverseTake(k: Int)(implicit ord: Ordering[V]) = {
    sortedTake(k)(ord.reverse)
  }
}