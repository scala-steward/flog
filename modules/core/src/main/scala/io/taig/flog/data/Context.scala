package io.taig.flog.data

import java.util.UUID

import cats.implicits._
import io.circe.JsonObject

final case class Context(prefix: Scope, presets: JsonObject, correlation: Option[String]) { self =>
  def append(prefix: Scope): Context = copy(prefix = self.prefix ++ prefix)

  def combine(payload: JsonObject): Context = copy(presets = self.presets deepMerge payload)

  def correlation(uuid: UUID): Context = copy(correlation = uuid.show.some)
}

object Context {
  val Empty: Context = Context(Scope.Root, JsonObject.empty, None)
}
