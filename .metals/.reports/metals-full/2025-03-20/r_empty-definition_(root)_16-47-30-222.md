error id: controllers/RateController#searchBuyingRates().(orgCountry)
file://<WORKSPACE>/ratesapp/app/controllers/RateController.scala
empty definition using pc, found symbol in pc: 
found definition using semanticdb; symbol controllers/RateController#searchBuyingRates().(orgCountry)
|empty definition using fallback
non-local guesses:
	 -

Document text:

```scala
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
      val rates = rateRepo.searchRates(rateType, orgCountry, destCountry, orgPort, destPort)
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
}
```

#### Short summary: 

empty definition using pc, found symbol in pc: 