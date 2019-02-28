package com.realworld.accounts

import cats.{Monad, MonadError}
import cats.implicits._
import com.realworld.AppError
import com.realworld.accounts.model._
import com.realworld.app.errorhandler.{ErrorResp, HttpErrorHandler, RoutesHttpErrorHandler}
import org.http4s.{HttpRoutes, Response}
import org.http4s.dsl.Http4sDsl
import io.circe.generic.auto._
import io.circe.syntax._
import org.http4s.circe._

class AccountsHttpErrorHandler[F[_]: MonadError[?[_], AccountDomainErrors]](implicit M: Monad[F]) extends HttpErrorHandler[F, AccountDomainErrors] with Http4sDsl[F] {
  private val handler: AccountDomainErrors => F[Response[F]] = {
    case InvalidCredentials(errorMsg) => UnprocessableEntity(ErrorResp(errorMsg).asJson)
    case a: AccountDomainErrors => UnprocessableEntity(ErrorResp(a.errorMsg).asJson)
  }

  override def handle(routes: HttpRoutes[F]): HttpRoutes[F] =
    RoutesHttpErrorHandler(routes)(handler)

}
