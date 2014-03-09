package org.jba

import java.io.{FileWriter, File}

import scala.io.Source

import com.google.common.io.Files

import org.specs2.mutable.Specification

class WordCountSpec extends Specification {

  "A WordCount job" should {

    "count words correctly" in {

      val tempDir = Files.createTempDir()
      val inputFile = new File(tempDir, "input").getAbsolutePath
      val inWriter = new FileWriter(inputFile)
      inWriter.write("hello world hello")
      inWriter.close
      val outputDir = new File(tempDir, "output").getAbsolutePath

      WordCount.execute2("local", List(inputFile, outputDir))
      
      val outputFile = new File(outputDir, "part-00000")
      val actual = Source.fromFile(outputFile).mkString
      actual must_== "(world,1)\n(hello,2)\n"
    }
  }
}
