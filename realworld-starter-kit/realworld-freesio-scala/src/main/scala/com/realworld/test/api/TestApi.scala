package com.realworld.test.api

import cats.effect.Effect
import cats.implicits._
import freestyle.tagless.logging.LoggingM
import io.circe.Json
import org.http4s._
import org.http4s.dsl.Http4sDsl
import org.http4s.circe._

class TestApi[F[_]: Effect](implicit log: LoggingM[F]) extends Http4sDsl[F] {
  private val prefix = "test"

  val endPoints = HttpRoutes.of[F] {
    case GET -> Root / prefix / "ping" =>
      for {
        _        <- log.error("Not really an error")
        _        <- log.warn("Not really a warn")
        _        <- log.debug("GET /ping")
        response <- Ok(Json.fromLong(Pong.current.time))
      } yield response

    case GET -> Root / prefix / "hello" =>
      for {
        _        <- log.error("Not really an error")
        _        <- log.warn("Not really a warn")
        _        <- log.debug("GET /Hello")
        response <- Ok("Hello World")
      } yield response
  }
}

object TestApi {
  implicit def instance[F[_]: Effect](implicit log: LoggingM[F]): TestApi[F] = new TestApi[F]
}