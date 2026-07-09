package io.taig.flog.data

import io.circe.JsonObject
import io.circe.syntax.*

import java.util.UUID

final case class Context(prefix: Scope, presets: JsonObject):
  def modifyScope(f: Scope => Scope): Context = copy(prefix = f(prefix))

  def withScope(scope: Scope): Context = modifyScope(_ => scope)

  def modifyPresets(f: JsonObject => JsonObject): Context = copy(presets = f(presets))

  def withPresets(presets: JsonObject): Context = modifyPresets(_ => presets)

  def append(suffix: Scope): Context = modifyScope(_ ++ suffix)

  def prepend(prefix: Scope): Context = modifyScope(prefix ++ _)

  def combine(payload: JsonObject): Context = modifyPresets(_ `deepMerge` payload)

  def correlation(uuid: UUID): Context = combine(JsonObject("correlation" := uuid))

object Context:
  val Empty: Context = Context(Scope.Root, JsonObject.empty)
