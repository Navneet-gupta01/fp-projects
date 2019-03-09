package com.realworld.comments

import cats.{Monad, MonadError}
import com.realworld.app.errorhandler.{ErrorResp, HttpErrorHandler, RoutesHttpErrorHandler}
import com.realworld.comments.model.CommentsDomainErrors
import io.circe.generic.auto._
import io.circe.syntax._
import org.http4s.circe._
import org.http4s.dsl.Http4sDsl
import org.http4s.{HttpRoutes, Response}

class CommentsHttpErrorHandler[F[_] : MonadError[?[_], CommentsDomainErrors]](implicit M: Monad[F]) extends HttpErrorHandler[F, CommentsDomainErrors] with Http4sDsl[F] {
  private val handler: CommentsDomainErrors => F[Response[F]] = {
    case a: CommentsDomainErrors => UnprocessableEntity(ErrorResp(a.errorMsg).asJson)
  }

  override def handle(routes: HttpRoutes[F]): HttpRoutes[F] =
    RoutesHttpErrorHandler(routes)(handler)

}
