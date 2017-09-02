package com.techmonad.csv

import java.io.{FileOutputStream, OutputStreamWriter}

import com.univocity.parsers.csv.{CsvWriter, CsvWriterSettings}

import scala.util.control.NonFatal

class CSVWriter(delimiter: Char, path: String, headers: Array[String]) {

  private val outputStreamWriter = new OutputStreamWriter(new FileOutputStream(path), "UTF-8")


  private val writer = {
    val csvWriterSettings = new CsvWriterSettings
    csvWriterSettings.setQuoteAllFields(true)
    csvWriterSettings.setNullValue("")
    csvWriterSettings.setEmptyValue("")
    val format = csvWriterSettings.getFormat
    format.setDelimiter(delimiter)
    new CsvWriter(outputStreamWriter, csvWriterSettings)
  }
  //write headers
  writer.writeHeaders(headers: _*)


  def write(row: Array[String]): Unit =
    try {
      writer.writeRow(row)
    } catch {
      case NonFatal(th) =>
        th.printStackTrace()
    }

  def close = {
    writer.flush()
    writer.close()
    outputStreamWriter.close()
  }

}
