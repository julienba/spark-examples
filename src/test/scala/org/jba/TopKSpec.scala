package org.jba

import java.io.{FileWriter, File}
import scala.io.Source
import com.google.common.io.Files
import org.specs2.mutable.Specification

class TopKSpec extends Specification {
  
  "A TopK job" should {

    def strItem(i: Item) = s"${i.score}\t${i.categoryId}\t${i.content}\n"
    
    "find ordering result" in {

      val tempDir = Files.createTempDir()
      
      val input = strItem(Item(2,1,"c"))
      
      val inputFile = new File(tempDir, "input").getAbsolutePath
      val inWriter = new FileWriter(inputFile)
      
      inWriter.write(strItem(Item(2,1,"a")))
      inWriter.write(strItem(Item(1,1,"b")))
      inWriter.write(strItem(Item(3,1,"c")))
      inWriter.write(strItem(Item(2,2,"d")))
      
      inWriter.close
      val outputDir = new File(tempDir, "output").getAbsolutePath

      TopK.apply("local", List(inputFile, outputDir))
      
      val outputFile = new File(outputDir, "part-00000")
      val actual = Source.fromFile(outputFile).mkString
      //println("ACTUAL: " + actual)
      actual must contain("(2,List(Item(2,2,d)))")
      actual must contain("(1,List(Item(1,1,b), Item(2,1,a)))")
    }
  }
}