package org.jba

import scala.util.Random
import java.io.FileWriter

import org.jba.model.EventLog
import org.jba.model.Model._

object RandomLogWriter extends App {
  
  if (args.length < 2) {
    System.err.println("Usage: RandomLogWriter <nb_row> <destFile>")
    System.exit(1)
  }  
  
  val rand = new Random()
  
  val nbRow = args(0).toInt
  val filePath = args(1)
  
  println("Writing log...")
  
  val fw = new FileWriter(filePath, true)
  
  try {
    (1 to nbRow).map { index =>
      val timestamp = System.currentTimeMillis()
      val eventType = s"event-${rand.nextInt(10)}"
      val content = rand.nextString(10)
      
      val log = EventLog(eventType, content, timestamp)      
      fw.write(eventFormat.writes(log).toString() + "\n")
      
      if (index % 10000 == 0) {
        println(s"Write $index event!")   
      }
    }    
  } finally fw.close()
}