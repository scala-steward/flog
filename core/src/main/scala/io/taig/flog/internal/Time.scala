package io.taig.flog.internal

import java.time.Instant

import cats.effect.Sync

private[flog] object Time {
  def now[F[_]](implicit F: Sync[F]): F[Instant] = F.delay(Instant.now())
}
