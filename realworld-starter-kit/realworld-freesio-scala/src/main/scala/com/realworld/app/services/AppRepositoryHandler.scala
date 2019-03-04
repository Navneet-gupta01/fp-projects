package com.realworld.app.services

import cats.Monad
import com.realworld.accounts.persistence.AccountQueries
import com.realworld.articles.persistence.{ArticlesQueries, TagsQueries}
import doobie.util.transactor.Transactor

class AppRepositoryHandler[F[_]: Monad](implicit T: Transactor[F]) extends AppRepository.Handler[F] {

  import com.realworld.accounts.persistence.AccountQueries._
  import com.realworld.articles.persistence.TagsQueries._
  import com.realworld.articles.persistence.ArticlesQueries._
  import com.realworld.articles.persistence.ArticleTagsQueries._



  override def reset: F[Int] =
    for {
      dropedAccount <- AccountQueries.dropQuery
      createAccount <- AccountQueries.createQuery
      dropArticles <- ArticlesQueries.dropQuery
      createArticles <- ArticlesQueries.createQuery
      dropTags <- TagsQueries.dropTagsQuery
      createTags <- TagsQueries.createTagsQuery
      dropProfile <- Profile
    }

  override def init: F[Int] = ???

  override def drop: F[Int] = ???
}
