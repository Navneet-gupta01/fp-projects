package com.realworld.favorites.persistence

import freestyle.tagless.tagless

@tagless(true)
trait FavoritesRepository[F[_]] {
  def favoriteArticle(article_id: Long, user_id: Long): F[Int]
  def unFavoriteArticle(article_id: Long, user_id: Long): F[Int]
}
