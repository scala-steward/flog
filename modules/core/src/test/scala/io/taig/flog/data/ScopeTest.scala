package io.taig.flog.data

import cats.syntax.all.*
import io.circe.Json
import io.circe.syntax.*
import munit.FunSuite

final class ScopeTest extends FunSuite:
  test("show"):
    assertEquals(obtained = Scope.Root.show, expected = "/")
    assertEquals(obtained = (Scope.Root / "foo" / "bar").show, expected = "foo / bar")

  test("encode"):
    assertEquals(obtained = (Scope.Root / "foo" / "bar").asJson, expected = Json.fromString("foo / bar"))

  test("contains"):
    assert((Scope.Root / "foo" / "bar").contains(Scope.one("foo")))
    assert(!(Scope.Root / "foo" / "bar").contains(Scope.one("baz")))

  test("fromClassName (class)"):
    assertEquals(
      obtained = Scope.fromClassName[Level],
      expected = Scope.Root / "io" / "taig" / "flog" / "data" / "Level"
    )

  test("fromClassName (object)"):
    assertEquals(
      obtained = Scope.fromClassName[Level.type],
      expected = Scope.Root / "io" / "taig" / "flog" / "data" / "Level"
    )

  test("fromSimpleClassName (class)"):
    assertEquals(obtained = Scope.fromSimpleClassName[Level], expected = Scope.Root / "Level")

  test("fromSimpleClassName (object)"):
    assertEquals(obtained = Scope.fromSimpleClassName[Level.type], expected = Scope.Root / "Level")
