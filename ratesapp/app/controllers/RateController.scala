package controllers

import javax.inject._
import play.api.mvc._
import play.api.libs.json._
import repositories.RateRepository
import models.Rate

@Singleton
class RateController @Inject()(cc: ControllerComponents, rateRepo: RateRepository) extends AbstractController(cc) {

  implicit val rateFormat: OFormat[Rate] = Json.format[Rate]

  def searchRates(rateType:String,
    mode: Option[String],
    orgCountry: Option[String],
    destCountry: Option[String],
    orgPort: Option[String],
    destPort: Option[String]
  ) = Action {
    try {
      val filters = Map(
        "mode" -> mode,
        "originCountryCode" -> orgCountry,
        "destinationCountryCode" -> destCountry,
        "originPortCode" -> orgPort,
        "destinationPortCode" -> destPort
      )
      val rates = rateRepo.searchRates(rateType, filters)
      if (rates.isEmpty) {
        NotFound(Json.obj("message" -> "No matching buying rates found"))
      } else {
        Ok(Json.toJson(rates))
      }
    } catch {
      case e: IllegalArgumentException => BadRequest(Json.obj("error" -> e.getMessage))
      case ex: Exception => InternalServerError(Json.obj("error" -> "Internal server error", "details" -> ex.getMessage))
    }
  }

  def listPorts(mode:String) = Action {
    try{
      val portList = rateRepo.listPorts(mode)
      if (portList.isEmpty) {
        NotFound(Json.obj("message" -> "No matching ports found"))
      } else {
        Ok(Json.toJson(portList))
      }
    } catch {
      case e: IllegalArgumentException => BadRequest(Json.obj("error" -> e.getMessage))
      case ex: Exception => InternalServerError(Json.obj("error" -> "Internal server error", "details" -> ex.getMessage))
    }
  }

  def insertRate(rateType:String) = Action(parse.json) { request =>
    request.body.validate[Rate].fold(
      _ => BadRequest(Json.obj("error" -> "Invalid JSON format")),
      rate => {
        val success = rateRepo.insertRate(rate.copy(rateType = rateType))
        if (success) Created(Json.obj("message" -> "Buying rate added successfully"))
        else InternalServerError(Json.obj("error" -> "Failed to insert rate"))
      }
    )
  }


}