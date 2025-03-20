package utils

object commonFunctions {
   def parseCommaSeparated(value: String): String = {
    value.split(",").map(_.trim).map("'" + _ + "'").mkString(", ")
  }
}
