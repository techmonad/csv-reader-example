package com.techmonad.csv

import java.nio.charset.CodingErrorAction

import com.univocity.parsers.csv.{CsvParser, CsvParserSettings}

import scala.io.{Codec, Source}
import scala.util.control.NonFatal

class CsvReader(delimiter: Char, path: String) {
  val codec = Codec("UTF-8")
  codec.onMalformedInput(CodingErrorAction.REPLACE)
  codec.onUnmappableCharacter(CodingErrorAction.REPLACE)
  private val reader = Source.fromFile(path)(codec).reader()

  private val parser: CsvParser = {
    val parserSettings = new CsvParserSettings
    val format = parserSettings.getFormat
    format.setLineSeparator("\n")
    format.setDelimiter(delimiter)
    format.setComment('\0')
    parserSettings.setIgnoreLeadingWhitespaces(true)
    parserSettings.setIgnoreTrailingWhitespaces(true)
    parserSettings.setReadInputOnSeparateThread(false)
    parserSettings.setNullValue("")
    parserSettings.setEmptyValue("")
    parserSettings.setMaxCharsPerColumn(-1)
    parserSettings.setMaxColumns(40000)
    parserSettings.setHeaderExtractionEnabled(false)
    new CsvParser(parserSettings)
  }

  parser.beginParsing(reader)

  def read(): Array[String] = parser.parseNext()

  def close() =
    try {
      reader.close()
      parser.stopParsing()
    } catch {
      case NonFatal(th) =>
        th.printStackTrace()
    }


}
