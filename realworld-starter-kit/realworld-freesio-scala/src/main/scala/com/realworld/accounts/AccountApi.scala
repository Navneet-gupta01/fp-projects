package com.realworld.accounts

import cats.effect.Effect
import cats.implicits._
import com.realworld.AppError
import com.realworld.accounts.model.{AccountDomainErrors, AccountEntity, AccountForm}
import com.realworld.accounts.services.AccountServices
import com.realworld.app.errorhandler.HttpErrorHandler
import freestyle.tagless.logging.LoggingM
import io.circe.generic.auto._
import io.circe.syntax._
import org.http4s._
import org.http4s.circe._
import org.http4s.dsl.Http4sDsl



class AccountApi[F[_]: Effect](implicit services: AccountServices[F], log: LoggingM[F], H: HttpErrorHandler[F, AccountDomainErrors]) extends Http4sDsl[F] {
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
          accountForm <- req.as[AccountForm]
          insertedAccount <- services.registerUser(accountForm)
          res <- Ok(insertedAccount.asJson)
        } yield res

    case GET -> Root / prefix / (email) =>
      services.fetch(None,Some(email),None) flatMap { item =>
        Ok(item.asJson)
    }

    case DELETE -> Root / prefix / LongVar(id) =>
      services.deleteUser(id) flatMap { item => Ok(item.asJson)}

    case req@PUT -> Root / prefix  =>
      for {
        account <- req.as[AccountForm]
        updatedAccount <- services.updateUser(account)
        res <- Ok(updatedAccount.asJson)
      } yield res

    case req@PUT -> Root / prefix / "password" =>
      for {
        account <- req.as[AccountForm]
        updatedAccount <- services.updatePassword(account)
        res <- Ok(updatedAccount.asJson)
      } yield res

    case GET -> Root / prefix / "hello" =>
      for {
        _        <- log.error("Not really an error")
        _        <- log.warn("Not really a warn")
        _        <- log.debug("GET /Hello")
        response <- Ok("Hello World")
      } yield response
  }

  val routes: HttpRoutes[F] = H.handle(endPoints)

}


object AccountApi {
  implicit def instance[F[_]: Effect](implicit services: AccountServices[F], log: LoggingM[F], H: HttpErrorHandler[F, AccountDomainErrors]): AccountApi[F] = new AccountApi[F]
}
