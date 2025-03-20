package repositories

import javax.inject.Inject
import java.sql.{Connection, PreparedStatement, ResultSet}
import play.api.db.Database
import models.Rate
import scala.collection.mutable.ListBuffer
import utils.commonFunctions._

class RateRepository @Inject()(db: Database) {

  def searchRates(rateType: String, filters: Map[String, Option[String]]): List[Rate] = {
    db.withConnection { conn =>
      val baseQuery = new StringBuilder(s"SELECT * FROM rates WHERE rate_type = '$rateType'")

      val conditions = filters.collect {
        case ("mode", Some(value)) => s"mode = '$value'"
        case ("originCountryCode", Some(value)) => s"origin_country_code IN (${parseCommaSeparated(value)})"
        case ("destinationCountryCode", Some(value)) => s"destination_country_code IN (${parseCommaSeparated(value)})"
        case ("originPortCode", Some(value)) => s"origin_port_code IN (${parseCommaSeparated(value)})"
        case ("destinationPortCode", Some(value)) => s"destination_port_code IN (${parseCommaSeparated(value)})"
      }

      if (conditions.isEmpty) {
        throw new IllegalArgumentException("At least one search criterion is required.")
      }

      baseQuery.append(" AND ").append(conditions.mkString(" AND "))

      val stmt = conn.prepareStatement(baseQuery.toString())
      val rs = stmt.executeQuery()
      val rates = ListBuffer[Rate]()

      while (rs.next()) {
        rates.append(Rate(
          Some(rs.getLong("id")),
          rs.getString("mode"),
          rs.getString("origin_country_code"),
          rs.getString("destination_country_code"),
          rs.getString("origin_port_code"),
          rs.getString("destination_port_code"),
          rs.getString("rate_type"),
          rs.getString("currency_code"),
          rs.getDouble("container_length"),
          rs.getDouble("container_width"),
          rs.getDouble("container_height"),
          rs.getDouble("amount")
        ))
      }

      rates.toList
    }
  }

  def listPorts(mode:String):List[String]={
     db.withConnection { conn =>

      val baseQuery = new StringBuilder(s"SELECT origin_country_code, destination_country_code FROM rates WHERE mode = '$mode")

      val stmt = conn.prepareStatement(baseQuery.toString())
      val rs = stmt.executeQuery()
      val rates = ListBuffer[String]()

      while (rs.next()) {
        rates.appendAll(List(
          rs.getString("origin_country_code"),
          rs.getString("destination_country_code")
        ))
      }
      rates.distinct.toList
    }
  }

  def insertRate(rate: Rate): Boolean = {
    db.withConnection { conn: Connection =>
      val stmt = conn.prepareStatement(
        """INSERT INTO rates (mode, origin_country_code, destination_country_code, origin_port_code, destination_port_code, rate_type, currency_code, container_length, container_width, container_height, amount)
           VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"""
      )

      stmt.setString(1, rate.mode)
      stmt.setString(2, rate.originCountryCode)
      stmt.setString(3, rate.destinationCountryCode)
      stmt.setString(4, rate.originPortCode)
      stmt.setString(5, rate.destinationPortCode)
      stmt.setString(6, rate.rateType) 
      stmt.setString(7, rate.currencyCode)
      stmt.setDouble(8, rate.containerLength)
      stmt.setDouble(9, rate.containerWidth)
      stmt.setDouble(10, rate.containerHeight)
      stmt.setDouble(11, rate.amount)

      stmt.executeUpdate() > 0
    }
  }
 
}