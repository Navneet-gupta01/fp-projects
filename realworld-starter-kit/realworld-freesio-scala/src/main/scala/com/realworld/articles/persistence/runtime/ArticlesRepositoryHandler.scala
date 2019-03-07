package com.realworld.articles.persistence.runtime

import cats.Monad
import cats.implicits._
import com.realworld.articles.model.{ArticleEntity, ArticleResponse, Tags}
import com.realworld.articles.persistence.ArticlesRepository
import doobie.implicits._
import doobie.util.transactor.Transactor

class ArticlesRepositoryHandler[F[_]: Monad](implicit T: Transactor[F]) extends ArticlesRepository.Handler[F] {

  import com.realworld.articles.persistence.ArticlesQueries._
  import com.realworld.articles.persistence.TagsQueries._
  import com.realworld.articles.persistence.ArticleTagsQueries._


  override def insertArticle(articleEntity: ArticleEntity, user_id: Long, tags: List[String]): F[Option[Long]] =
    (for {
      article_id <- insertQuery(articleEntity, user_id).withUniqueGeneratedKeys[Long]("id")
      tags_ids <- tags.map(t => insertTagsQuery(Tags(t)).withUniqueGeneratedKeys[Long]("id")).sequence
      _ <- tags_ids.map(t => insertATQuery(article_id, t).run).sequence
    } yield article_id.some).transact(T)

  override def getArticle(slug: String, user_id: Long): F[Option[ArticleResponse]] =
    (for {
      article <- getQuery(slug, user_id).unique
      tags <- getTagsQuery(article._1.id.get).to[List]
    } yield ArticleResponse(article).copy(tagList = tags).some).transact(T)

  override def getRecentFollowedArticles(limit: Long, offset: Long, user_id: Long): F[List[ArticleResponse]] =
    (for {
      articles <- getFollowedArticleQuery(limit, offset, user_id).to[List]
      resp <- articles.map(ar => {
        val arResp = ArticleResponse(ar)
        getTagsQuery(ar._1.id.get).to[List].map(t => arResp.copy(tagList = t))
      }).sequence
    } yield resp).transact(T)

  override def getRecentArticles(user_id: Long, limit: Long, offset: Long): F[List[ArticleResponse]] = (for {
    articles <- getRecentQuery(user_id, limit, offset).to[List]
    resp <- articles.map(ar => {
      val arResp = ArticleResponse(ar)
      getTagsQuery(ar._1.id.get).to[List].map(t => arResp.copy(tagList = t))
    }).sequence
  } yield resp).transact(T)

  override def updateArticle(articleEntity: ArticleEntity): F[Int] =
    updateQuery(articleEntity).run.transact(T)

  override def deleteArticle(slug: String, user_id: Long): F[Int] =
    deleteQuery(slug, user_id).run.transact(T)

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
