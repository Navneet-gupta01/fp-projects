package com.realworld.app.reset

import cats.effect.Effect
import cats.implicits._
import com.realworld.app.errorhandler.HttpErrorHandler
import com.realworld.app.services.AppServices
import freestyle.tagless.logging.LoggingM
import org.http4s.dsl.Http4sDsl
import io.circe.generic.auto._
import io.circe.syntax._
import org.http4s._
import org.http4s.circe._

class AppDBResetApis[F[_]: Effect](implicit appServices: AppServices[F], log: LoggingM[F]) extends Http4sDsl[F] {
  val endPoints = HttpRoutes.of[F] {
    case POST -> Root / "app" / "reset" =>
      for {
        _ <- log.debug("POST /app DB reset")
        reset <- appServices.resetDB
        res <- Ok(reset.asJson)
      } yield res
  }
}

object AppDBResetApis {
  implicit def instance[F[_]: Effect](implicit appServices: AppServices[F], log: LoggingM[F]) : AppDBResetApis[F] = new AppDBResetApis[F]
}
