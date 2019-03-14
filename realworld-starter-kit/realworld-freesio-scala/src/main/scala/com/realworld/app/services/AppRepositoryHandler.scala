package com.realworld.app.services

import cats.Monad
import com.realworld.accounts.persistence.AccountQueries
import com.realworld.articles.persistence.{ArticleTagsQueries, ArticlesQueries, TagsQueries}
import com.realworld.favorites.persistence.FavoritesQueries
import com.realworld.comments.persistence.CommentsQueries
import com.realworld.profile.persistence.ProfileQueries
import doobie.util.transactor.Transactor
import doobie.implicits._

class AppRepositoryHandler[F[_]: Monad](implicit T: Transactor[F]) extends AppRepository.Handler[F] {

  override def reset: F[Int] =
    (for {
      dropedAccount <- AccountQueries.dropQuery.run
      dropProfile <- ProfileQueries.dropQuery.run
      dropArticles <- ArticlesQueries.dropQuery.run
      dropTags <- TagsQueries.dropTagsQuery.run
      dropArticleTagsAssoc <- ArticleTagsQueries.dropATQuery.run
      dropComments <- CommentsQueries.dropQuery.run
      dropFavorites <- FavoritesQueries.dropFavoriteQuery.run

      createAccount <- AccountQueries.createQuery.run
      createArticles <- ArticlesQueries.createQuery.run
      createTags <- TagsQueries.createTagsQuery.run
      createProfile <- ProfileQueries.createQuery.run
      createAssoc <- ArticleTagsQueries.createATQuery.run
      createComments <- CommentsQueries.createQuery.run
      createFavoriteAssoc <- FavoritesQueries.createFavoriteQuery.run
    } yield dropedAccount +
      dropProfile +
      dropArticles +
      dropTags +
      dropArticleTagsAssoc +
      dropComments +
      dropFavorites +
      createAccount +
      createArticles +
      createTags +
      createProfile +
      createAssoc +
      createComments +
      createFavoriteAssoc).transact(T)
}
