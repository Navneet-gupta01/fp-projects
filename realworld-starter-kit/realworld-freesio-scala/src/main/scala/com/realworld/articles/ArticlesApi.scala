package com.realworld.articles

import cats.effect.Effect
import cats.implicits._
import com.realworld.accounts.model.{FormReq, LoginResp}
import com.realworld.app.errorhandler.HttpErrorHandler
import com.realworld.articles.model.{ArticleDomainErrors, ArticleReq, ArticleResp, ArticlesResp}
import com.realworld.articles.services.ArticlesServices
import com.realworld.profile.ProfileResp
import freestyle.tagless.logging.LoggingM
import io.circe.generic.auto._
import io.circe.syntax._
import org.http4s._
import org.http4s.circe._
import org.http4s.dsl.Http4sDsl


class ArticlesApi[F[_]: Effect](implicit services: ArticlesServices[F], log: LoggingM[F], H: HttpErrorHandler[F, ArticleDomainErrors]) extends Http4sDsl[F] {
  import com.realworld.articles.ArticleCodecs._
  object LimitQueryParamMatcher extends OptionalQueryParamDecoderMatcher[Long]("limit")
  object OffsetQueryParamMatcher extends OptionalQueryParamDecoderMatcher[Long]("offset")

  val endPoints = HttpRoutes.of[F] {
    case GET -> Root / "articles" :? LimitQueryParamMatcher(limit) +& OffsetQueryParamMatcher(offset) =>
      services.getRecentArticlesGlobally( 2L,limit.getOrElse(20L), offset.getOrElse(0L)) flatMap { item =>
        Ok(ArticlesResp(item).asJson)
      }
    case GET -> Root / "articles" / slug =>
      services.getArticle(slug, 2L) flatMap { item =>
        Ok(ArticleResp(item).asJson)
      }
    case GET -> Root / "articles" / "feed" :? LimitQueryParamMatcher(limit) +& OffsetQueryParamMatcher(offset) =>
      services.getRecentFollowedUsersArticles( 2L,limit.getOrElse(20L), offset.getOrElse(0L)) flatMap { item =>
        Ok(ArticlesResp(item).asJson)
      }

    case req@POST -> Root / "articles" =>
      for {
        wrappedReq <- req.as[ArticleReq]
        insertedArticle <- services.insertArticles(wrappedReq.article, 2L)
        res <- Ok(ArticleResp(insertedArticle).asJson)
      } yield res

    case req@PUT -> Root / "articles" / slug =>
      for {
        wrappedReq <- req.as[ArticleReq]
        updatedArticle <- services.updateArticle(wrappedReq.article, 2L)
        res <- Ok(ArticleResp(updatedArticle).asJson)
      } yield res

    case DELETE -> Root / "articles" / slug =>
      services.deleteArticle(slug, 2L) flatMap {
        item => Ok(item.asJson)
      }
  }

  val routes: HttpRoutes[F] = H.handle(endPoints)
}

object ArticlesApi {
  implicit def instance[F[_]: Effect](implicit services: ArticlesServices[F], log: LoggingM[F], H: HttpErrorHandler[F, ArticleDomainErrors]) : ArticlesApi[F] = new ArticlesApi[F]
}