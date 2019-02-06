package com.realworld.accounts

import cats.effect.Effect
import cats.implicits._
import com.realworld.accounts.model.AccountEntity
import com.realworld.accounts.services.AccountServices
import freestyle.tagless.logging.LoggingM
import io.circe.generic.auto._
import io.circe.syntax._
import org.http4s._
import org.http4s.circe._
import org.http4s.dsl.Http4sDsl



class AccountApi[F[_]: Effect](implicit services: AccountServices[F], log: LoggingM[F]) extends Http4sDsl[F] {
  private val prefix = "users"

  import Codecs._

  val endPoints = HttpRoutes.of[F] {
    case POST -> Root / prefix =>
      for {
        _        <- log.debug("POST /users reset")
        reset <- services.reset
        res <- Ok(reset.asJson)
      } yield res
    case req@POST -> Root / prefix / "register" =>
        for {
          account <- req.as[AccountEntity]
          insertedAccount <- services.registerUser(account)
          response <- insertedAccount.fold(NotFound(s"Could not register ${services.model} with ${account.email}"))(account => Ok(account.asJson))
        } yield response

    case GET -> Root / prefix / (email) =>
      services.getCurrentUser(email) flatMap { item =>
        item.fold(NotFound(s"Could not find ${services.model} with $email"))(account => Ok(account.asJson))
    }


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
