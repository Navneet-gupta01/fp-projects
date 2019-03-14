package com.realworld.favorites

import cats.effect.Effect
import cats.implicits._
import com.realworld.app.errorhandler.HttpErrorHandler
import com.realworld.articles.model.{ArticleReq, ArticleResp, ArticlesResp}
import com.realworld.favorites.model.FavoritesDomainErrors
import freestyle.tagless.logging.LoggingM
import org.http4s.HttpRoutes
import io.circe.generic.auto._
import io.circe.syntax._
import org.http4s._
import org.http4s.circe._
import org.http4s.dsl.Http4sDsl

class FavoriteApi[F[_]: Effect](implicit services: FavoritesServices[F],
                                H: HttpErrorHandler[F, FavoritesDomainErrors],
                                log: LoggingM[F]) extends Http4sDsl[F] {

  import FavoriteCodecs._
  import com.realworld.articles.ArticleCodecs._

  val endPoints = HttpRoutes.of[F] {
    case POST -> Root / "articles" / slug / "favorite" =>
      services.favoriteAnArticle(slug, 2L) flatMap { item =>
        Ok(ArticleResp(item).asJson)
      }

    case DELETE -> Root / "articles" / slug / "favorite" =>
      services.unFavoriteAnArticle(slug, 2L) flatMap {
        item => Ok(item.asJson)
      }
  }

  val routes: HttpRoutes[F] = H.handle(endPoints)

}

object FavoriteApi {
  implicit def instance[F[_]: Effect](implicit services: FavoritesServices[F],
                                      H: HttpErrorHandler[F, FavoritesDomainErrors],
                                      log: LoggingM[F]): FavoriteApi[F] = new FavoriteApi[F]
}