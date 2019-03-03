package com.realworld.articles.persistence.runtime

import cats.Monad
import com.realworld.articles.model.ArticleEntity
import com.realworld.articles.persistence.ArticlesRepository
import doobie.util.transactor.Transactor

class ArticlesRepositoryHandler[F[_]: Monad](implicit T: Transactor[F]) extends ArticlesRepository.Handler[F] {

  import com.realworld.articles.persistence.ArticlesQueries._
  import com.realworld.articles.persistence.TagsQueries._

  override def getArticle(slug: String): F[Option[ArticleEntity]] = ???

  override def getRecentFollowedArticles(limit: Int, offset: Int, user_id: Long): F[List[ArticleEntity]] = ???

  override def getRecentArticles(limit: Int, offset: Int): F[List[ArticleEntity]] = ???

  override def updateArticle(articleEntity: ArticleEntity): F[Option[ArticleEntity]] = ???

  override def deleteArticle(slug: String): F[Option[ArticleEntity]] = ???

  override val list: F[List[ArticleEntity]] = ???
  override val init: F[Int] =  ???
  override val create: F[Int] = ???
  override val drop: F[Int] =  ???
}
