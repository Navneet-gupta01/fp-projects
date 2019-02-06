package com.realworld.accounts

import cats.effect.Effect
import cats.implicits._
import com.realworld.accounts.services.AccountServices
import freestyle.tagless.logging.LoggingM
import io.circe.syntax._
import org.http4s._
import org.http4s.circe._
import org.http4s.dsl.Http4sDsl



class AccountApi[F[_]: Effect](implicit services: AccountServices[F], log: LoggingM[F]) extends Http4sDsl[F] {
  private val prefix = "users"

  val endPoints = HttpRoutes.of[F] {
    case POST -> Root / prefix =>
      for {
        _        <- log.debug("POST /users reset")
        reset <- services.reset
        res <- Ok(reset.asJson)
      } yield res


    case GET -> Root / prefix / "hello" =>
      for {
        _        <- log.error("Not really an error")
        _        <- log.warn("Not really a warn")
        _        <- log.debug("GET /Hello")
        response <- Ok("Hello World")
      } yield response
  }

}


object AccountApi {
  implicit def instance[F[_]: Effect](implicit services: AccountServices[F], log: LoggingM[F]): AccountApi[F] = new AccountApi[F]
}
