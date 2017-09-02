package com.techmonad.csv

object CSV2CSV extends App {

  val inputCSVPath = args(0)

  val outputCSVPath = args(1)

  val csvReader = new CsvReader(',', inputCSVPath)

  val headers = csvReader.read()

  val writer = new CSVWriter('\t', outputCSVPath, headers)

  println("Start writing.....")

  Iterator.continually(csvReader.read()).takeWhile(row => Option(row).isDefined)
    .foreach { row =>
      val updatedRow = row.map(_.replaceAll("""[\n\r\t]""", " "))
      writer.write(updatedRow)
    }

  csvReader.close()
  writer.close

}
