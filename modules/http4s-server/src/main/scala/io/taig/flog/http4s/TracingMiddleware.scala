package io.taig.flog.http4s

import java.util.UUID

import cats.effect.Sync
import cats.syntax.all._
import io.taig.flog.ContextualLogger
import org.http4s.Http

object TracingMiddleware {
  def apply[F[_], G[_]](logger: ContextualLogger[F])(http: Http[F, G])(implicit F: Sync[F]): Http[F, G] =
    Http[F, G] { request =>
      F.delay(UUID.randomUUID()).flatMap(uuid => logger.local(http.run(request))(_.trace(uuid)))
    }
}
