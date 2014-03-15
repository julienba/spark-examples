package org.jba

import java.io.{FileWriter, File}
import scala.io.Source
import com.google.common.io.Files
import org.specs2.mutable.Specification


class TopKSpec extends Specification {
  def strItem(i: Item) = s"${i.score}\t${i.categoryId}\t${i.content}\n"
  
  "A TopK job" should {
    
    "find ordering result" in {
      val dataset = ItemGenerator.genDS(100)
      
      val itemsById = dataset.groupBy(_.categoryId)
      val topItemsById = itemsById.map { case(id,seq) => (id,seq.sorted(ItemOrdering).take(2))}
      
      val tempDir = Files.createTempDir()
      val inputFile = new File(tempDir, "input").getAbsolutePath
      val inWriter = new FileWriter(inputFile)
      
      dataset.foreach( x => inWriter.write(strItem(x)))
      inWriter.close
      
      val outputDir = new File(tempDir, "output").getAbsolutePath

      TopK.apply("local", List(inputFile, outputDir))
      
      val outputFile = new File(outputDir, "part-00000")
      val actual = Source.fromFile(outputFile).mkString
      //println("ACTUAL:\n" + actual)      
      
      
      topItemsById must have size(Source.fromFile(outputFile).getLines.toList.size)
      topItemsById.foreach(x => actual must contain(TopK.pp(x)))
      
      success
    }    
    
  }
}