package models

import play.api.libs.json._

case class Rate(
  id: Option[Long] = None,
  mode: String,
  originCountryCode: String,
  destinationCountryCode: String,
  originPortCode: String,
  destinationPortCode: String,
  rateType: String,
  currencyCode: String,
  containerLength: Double,
  containerWidth: Double,
  containerHeight: Double,
  amount: Double
)

object Rate {
  implicit val rateFormat: OFormat[Rate] = Json.format[Rate]
}
