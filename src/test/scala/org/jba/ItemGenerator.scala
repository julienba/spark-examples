package org.jba

import org.scalacheck.Gen

object ItemGenerator {

  def gen() = {
    for {
      score <- Gen.chooseNum(1,100)
      content <- Gen.alphaStr
    } yield (score, content)
  }    
  
  def genItems(nb: Int) = for {
    catId <- Gen.chooseNum(1 ,1000)
    items <- Gen.listOfN(nb, gen()) 
  } yield items.map(t => Item(t._1, catId, t._2))
  
  def genDS(nb: Int) = {
    val g = Gen.listOfN(nb, genItems(nb))
    g.apply(Gen.Parameters.default).get.flatten
  }
}