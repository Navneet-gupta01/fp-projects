package com.realworld.app.services

import cats.Monad
import com.realworld.accounts.persistence.AccountQueries
import com.realworld.articles.persistence.{ArticleTagsQueries, ArticlesQueries, TagsQueries}
import com.realworld.profile.persistence.ProfileQueries
import doobie.util.transactor.Transactor

class AppRepositoryHandler[F[_]: Monad](implicit T: Transactor[F]) extends AppRepository.Handler[F] {

  import com.realworld.accounts.persistence.AccountQueries._
  import com.realworld.articles.persistence.TagsQueries._
  import com.realworld.articles.persistence.ArticlesQueries._
  import com.realworld.articles.persistence.ArticleTagsQueries._



  override def reset: F[Int] =
    for {
      dropedAccount <- AccountQueries.dropQuery
      dropProfile <- ProfileQueries.dropQuery
      dropArticles <- ArticlesQueries.dropQuery
      dropTags <- TagsQueries.dropTagsQuery
      dropArticleTagsAssoc <- ArticleTagsQueries.dropATQuery

      createAccount <- AccountQueries.createQuery
      createArticles <- ArticlesQueries.createQuery
      createTags <- TagsQueries.createTagsQuery
      createProfile <- ProfileQueries.createQuery
      createAssoc <- ArticleTagsQueries.createATQuery

  override def init: F[Int] = ???

  override def drop: F[Int] = ???
}
