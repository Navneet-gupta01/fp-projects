package com.realworld.accounts

import cats.{Monad, MonadError}
import com.realworld.AppError
import com.realworld.app.errorhandler.{HttpErrorHandler, RoutesHttpErrorHandler}
import org.http4s.{HttpRoutes, Response}
import org.http4s.dsl.Http4sDsl

class AccountsHttpErrorHandler[F[_]: MonadError[?[_], AppError]](implicit M: Monad[F]) extends HttpErrorHandler[F, AppError] with Http4sDsl[F] {
  private val handler: AppError => F[Response[F]] = {
    case InvalidInputParams(msg) => BadRequest(msg)
    case EntityNotFound(msg) => BadRequest(msg)
  }

  override def handle(routes: HttpRoutes[F]): HttpRoutes[F] =
    RoutesHttpErrorHandler(routes)(handler)

}
