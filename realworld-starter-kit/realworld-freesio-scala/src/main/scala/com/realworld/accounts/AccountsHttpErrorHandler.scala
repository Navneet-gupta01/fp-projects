package com.realworld.accounts

import cats.{Monad, MonadError}
import cats.implicits._
import com.realworld.AppError
import com.realworld.accounts.model.{AccountDoesNotExist, AccountDomainErrors, InvalidInputParams}
import com.realworld.app.errorhandler.{HttpErrorHandler, RoutesHttpErrorHandler}
import org.http4s.{HttpRoutes, Response}
import org.http4s.dsl.Http4sDsl

class AccountsHttpErrorHandler[F[_]: MonadError[?[_], AccountDomainErrors]](implicit M: Monad[F]) extends HttpErrorHandler[F, AccountDomainErrors] with Http4sDsl[F] {
  private val handler: AccountDomainErrors => F[Response[F]] = {
    case InvalidInputParams(msg) => BadRequest(msg)
    case a: AccountDomainErrors => BadRequest(a.errorMsg)
  }

  override def handle(routes: HttpRoutes[F]): HttpRoutes[F] =
    RoutesHttpErrorHandler(routes)(handler)

}
