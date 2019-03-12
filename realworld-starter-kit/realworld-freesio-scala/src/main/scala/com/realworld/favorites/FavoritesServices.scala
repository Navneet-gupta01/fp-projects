package com.realworld.favorites

import cats.Monad
import cats.implicits._
import com.realworld.articles.persistence.ArticlesRepository
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

  def favoriteAnArticle(slug: String, user_id: Long) : F[Int] =
    for {
      _ <- L.info("Trying to Favorite an Article with slug: ${slug} by user_id: ${user_id}")
      article <- articleRepo
    }

}
