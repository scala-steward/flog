package io.taig.flog

import cats.effect.IO
import cats.effect.Ref
import io.taig.flog.data.Event
import io.taig.flog.data.Scope
import munit.CatsEffectSuite

final class LoggerTest extends CatsEffectSuite:
  private def scopeOf(log: Logger[IO] => IO[Unit]): IO[Scope] = Ref[IO]
    .of(List.empty[Event])
    .flatTap(target => log(Logger.list[IO](target)))
    .flatMap(_.get.map(_.head.scope))

  test("append applies in declaration order"):
    assertIO(
      obtained = scopeOf(_.append(Scope.one("foo")).append(Scope.one("bar")).info(Scope.one("root"), "")),
      returns = Scope.of("root", "foo", "bar")
    )

  test("prepend applies in declaration order"):
    assertIO(
      obtained = scopeOf(_.prepend(Scope.one("foo")).prepend(Scope.one("bar")).info(Scope.one("root"), "")),
      returns = Scope.of("bar", "foo", "root")
    )

  test("append and prepend interleave in declaration order"):
    assertIO(
      obtained = scopeOf(_.append(Scope.one("foo")).prepend(Scope.one("bar")).info(Scope.one("root"), "")),
      returns = Scope.of("bar", "root", "foo")
    )

  test("queued flushes before close"):
    Ref[IO]
      .of(List.empty[Event])
      .flatTap(target => Logger.queued[IO](Logger.list[IO](target)).use(_.info("foobar")))
      .flatMap(target => assertIO(obtained = target.get.map(_.length), returns = 1))

  test("batched flushes before close"):
    Ref[IO]
      .of(List.empty[Event])
      .flatTap(target => Logger.batched[IO](Logger.list[IO](target), buffer = 5).use(_.info("foobar")))
      .flatMap(target => assertIO(obtained = target.get.map(_.length), returns = 1))
