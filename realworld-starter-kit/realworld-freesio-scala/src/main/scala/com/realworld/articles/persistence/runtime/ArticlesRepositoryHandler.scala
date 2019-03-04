package com.realworld.articles.persistence.runtime

import cats.Monad
import cats.implicits._
import com.realworld.articles.model.{ArticleEntity, ArticleResponse}
import com.realworld.articles.persistence.ArticlesRepository
import doobie.implicits._
import doobie.util.transactor.Transactor

class ArticlesRepositoryHandler[F[_]: Monad](implicit T: Transactor[F]) extends ArticlesRepository.Handler[F] {

  import com.realworld.articles.persistence.ArticlesQueries._
  import com.realworld.articles.persistence.TagsQueries._
  import com.realworld.articles.persistence.ArticleTagsQueries._

  override def getArticle(slug: String, user_id: Long): F[Option[ArticleResponse]] =
    (for {
      article <- getQuery(slug, user_id).unique
      tags <- getTagsQuery(article._1.id.get).to[List]
    } yield ArticleResponse(article).copy(tagList = tags).some).transact(T)

  override def getRecentFollowedArticles(limit: Int, offset: Int, user_id: Long): F[List[ArticleResponse]] = ???

  override def getRecentArticles(user_id: Long, limit: Int, offset: Int): F[List[ArticleResponse]] = (for {
    articles <- getRecentQuery(user_id, limit, offset).to[List]
    resp <- articles.map(ar => {
      val arResp = ArticleResponse(ar)
      getTagsQuery(ar._1.id.get).to[List].map(t => arResp.copy(tagList = t))
    }).sequence
  } yield resp).transact(T)

  override def updateArticle(articleEntity: ArticleEntity): F[Option[ArticleEntity]] = ???

  override def deleteArticle(slug: String): F[Option[ArticleEntity]] = ???

  override def list(limit: Int, offset: Int): F[List[ArticleResponse]] = ???

  override def init: F[Int] =
    dropQuery
    .run
    .flatMap(drops =>
      createQuery
        .run
        .map(_ + drops))
    .transact(T)

  override def create: F[Int] =
    createQuery
      .run
      .transact(T)

  override def drop: F[Int] =
    dropQuery
      .run
      .transact(T)
}
