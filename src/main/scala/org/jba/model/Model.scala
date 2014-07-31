package org.jba.model

import play.api.libs.json.Json
import play.api.libs.functional.syntax._

case class EventLog(`type`: String, content: String, timestamp: Long)

object Model {
  //implicit val eventWriter = Json.writes[EventLog]
  implicit val eventFormat = Json.format[EventLog]
}