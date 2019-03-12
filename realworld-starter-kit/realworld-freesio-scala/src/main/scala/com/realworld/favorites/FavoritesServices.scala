package com.realworld.favorites

import cats.Monad
import cats.implicits._
import com.realworld.articles.model.ArticleResponse
import com.realworld.articles.persistence.ArticlesRepository
import com.realworld.favorites.model.ArticleDoesNotExist
import com.realworld.favorites.persistence.FavoritesRepository
import freestyle.tagless.effects.error.ErrorM
import freestyle.tagless.logging.LoggingM
import freestyle.tagless.module

@module
trait FavoritesServices[F[_]] {
  implicit val M: Monad[F]
  implicit val L: LoggingM[F]


  val articleRepo: ArticlesRepository[F]
  val repo: FavoritesRepository[F]
  val error: ErrorM[F]

  def favoriteAnArticle(slug: String, user_id: Long) : F[Option[ArticleResponse]] =
    for {
      _ <- L.info("Trying to Favorite an Article with slug: ${slug} by user_id: ${user_id}")
      article <- articleRepo.getArticle(slug, user_id)
      _ <- error.either[ArticleResponse](article._1.toRight(ArticleDoesNotExist(slug)))
      favorited <- if(!article._1.get.favorited) repo.favoriteArticle(article._2, user_id) else 0.pure[F]
      _ <- L.info("Tryied to Favorite an Article with slug: ${slug} by user_id: ${user_id}")
    } yield article._1.map(ar => ar.copy(favorited=true, favoritesCount = ar.favoritesCount + favorited))

  def unFavoriteAnArticle(slug: String, user_id: Long) : F[Option[ArticleResponse]] =
    for {
      _ <- L.info("Trying to UnFavorite an Article with slug: ${slug} by user_id: ${user_id}")
      article <- articleRepo.getArticle(slug, user_id)
      _ <- error.either[ArticleResponse](article._1.toRight(ArticleDoesNotExist(slug)))
      favorited <- if(article._1.get.favorited) repo.unFavoriteArticle(article._2, user_id) else 0.pure[F]
      _ <- L.info("Tryied to UnFavorite an Article with slug: ${slug} by user_id: ${user_id}")
    } yield article._1.map(ar => ar.copy(favorited=true, favoritesCount = ar.favoritesCount - favorited))

}
