package com.realworld.articles

import cats.{Monad, MonadError}
import com.realworld.app.errorhandler.{ErrorResp, HttpErrorHandler, RoutesHttpErrorHandler}
import com.realworld.articles.model.ArticleDomainErrors
import org.http4s.{HttpRoutes, Response}
import org.http4s.dsl.Http4sDsl
import io.circe.generic.auto._
import io.circe.syntax._
import org.http4s.circe._

class ArticleHttpErrorHandler[F[_] : MonadError[?[_], ArticleDomainErrors]](implicit M: Monad[F]) extends HttpErrorHandler[F, ArticleDomainErrors] with Http4sDsl[F] {
  private val handler: ArticleDomainErrors => F[Response[F]] = {
    case a: ArticleDomainErrors => UnprocessableEntity(ErrorResp(a.errorMsg).asJson)
  }

  override def handle(routes: HttpRoutes[F]): HttpRoutes[F] =
    RoutesHttpErrorHandler(routes)(handler)

}
