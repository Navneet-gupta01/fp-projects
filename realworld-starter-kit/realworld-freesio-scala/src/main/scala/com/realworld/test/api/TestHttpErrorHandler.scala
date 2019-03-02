package com.realworld.test.api


import cats.{Monad, MonadError}
import cats.implicits._
import com.realworld.app.errorhandler.{HttpErrorHandler, RoutesHttpErrorHandler}
import org.http4s.{HttpRoutes, Response}
import org.http4s.dsl.Http4sDsl

class TestHttpErrorHandler[F[_] : MonadError[?[_], TestErrors]](implicit M: Monad[F]) extends HttpErrorHandler[F, TestErrors] with Http4sDsl[F] {
  private val handler: TestErrors => F[Response[F]] = {
    case a: TestErrors => BadRequest(a.errorMsg)
  }

  override def handle(routes: HttpRoutes[F]): HttpRoutes[F] =
    RoutesHttpErrorHandler(routes)(handler)

}
