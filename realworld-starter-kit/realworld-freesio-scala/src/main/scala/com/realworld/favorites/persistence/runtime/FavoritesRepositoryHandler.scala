package com.realworld.favorites.persistence.runtime

import cats.Monad
import com.realworld.favorites.persistence.{FavoritesQueries, FavoritesRepository}
import doobie.util.transactor.Transactor
import doobie.implicits._

class FavoritesRepositoryHandler[F[_]: Monad](implicit T: Transactor[F]) extends FavoritesRepository.Handler[F] {

  import FavoritesQueries._

  override def favoriteArticle(article_id: Long, user_id: Long): F[Int] =
    favoriteArticleQuery(user_id, article_id)
      .run
      .transact(T)

  override def unFavoriteArticle(article_id: Long, user_id: Long): F[Int] =
    unFavoriteArticleQuery(user_id, article_id)
      .run
      .transact(T)

}
