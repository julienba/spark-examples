package org.jba.spark

import com.twitter.algebird._
import com.twitter.algebird.mutable.PriorityQueueMonoid

import org.apache.spark.rdd.RDD
import org.apache.spark.SparkContext._

import scala.reflect.ClassTag
import scala.collection.JavaConverters._

object RDDGroupOps {
  implicit def toOps[K: ClassTag,V: ClassTag](r: RDD[(K,Iterable[V])]) = new RDDGroupOps(r)
  
  def pqm[V](k: Int, in: Iterable[V])(implicit ord: Ordering[V]) = {
    val mon = new PriorityQueueMonoid[V](k)
    mon.build(in.toIterable).asScala.toList.sorted    
  }
} 

class RDDGroupOps[K: ClassTag,V: ClassTag](that: RDD[(K,Iterable[V])]) {
  
  import RDDGroupOps._
  
  def sortedTake(k: Int)(implicit ord: Ordering[V]) = {
    that.reduceByKey { 
      pqm(k,_) ++ pqm(k,_) 
    }.mapValues {
      pqm(k, _)    
    }
  }
    
  def sortedReverseTake(k: Int)(implicit ord: Ordering[V]) = {
    sortedTake(k)(ord.reverse)
  }
}