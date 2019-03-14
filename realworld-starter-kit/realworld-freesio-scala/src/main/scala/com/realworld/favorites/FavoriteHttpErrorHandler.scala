package com.realworld.favorites

import cats.{Monad, MonadError}
import com.realworld.app.errorhandler.{ErrorResp, HttpErrorHandler, RoutesHttpErrorHandler}
import com.realworld.favorites.model.FavoritesDomainErrors
import org.http4s.{HttpRoutes, Response}
import org.http4s.dsl.Http4sDsl
import io.circe.generic.auto._
import io.circe.syntax._
import org.http4s.circe._

class FavoriteHttpErrorHandler[F[_] : MonadError[?[_], FavoritesDomainErrors]](implicit M: Monad[F]) extends HttpErrorHandler[F, FavoritesDomainErrors] with Http4sDsl[F] {
  private val handler: FavoritesDomainErrors => F[Response[F]] = {
    case a: FavoritesDomainErrors => UnprocessableEntity(ErrorResp(a.errorMsg).asJson)
  }

  override def handle(routes: HttpRoutes[F]): HttpRoutes[F] =
    RoutesHttpErrorHandler(routes)(handler)

}
