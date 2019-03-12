package com.realworld.articles.persistence.runtime

import cats.Monad
import cats.implicits._
import cats.instances.map
import com.realworld.articles.model.{ArticleEntity, ArticleResponse, Tags}
import com.realworld.articles.persistence.ArticlesRepository
import doobie.implicits._
import doobie.util.transactor.Transactor
import freestyle.tagless.logging.LoggingM

class ArticlesRepositoryHandler[F[_]: Monad](implicit T: Transactor[F]) extends ArticlesRepository.Handler[F] {

  import com.realworld.articles.persistence.ArticlesQueries._
  import com.realworld.articles.persistence.TagsQueries._
  import com.realworld.articles.persistence.ArticleTagsQueries._
  import com.realworld.favorites.persistence.FavoritesQueries._


  override def insertArticle(articleEntity: ArticleEntity, user_id: Long, tags: List[String]): F[Option[Long]] =
    (for {
      article_id <- insertQuery(articleEntity, user_id).withUniqueGeneratedKeys[Long]("id")
//      tags_existing <- getTagsQuery()
      tags_ids <- tags.map(t => insertTagsQuery(Tags(t)).withUniqueGeneratedKeys[Long]("id")).sequence
      _ <- tags_ids.map(t => insertATQuery(article_id, t).run).sequence
    } yield article_id.some).transact(T)

  override def getOwnedArticle(slug: String, user_id: Long): F[Option[ArticleEntity]] =
    getOwnedyArticleQuery(slug,user_id).option.transact(T)

  override def getArticleBySlug(slug: String): F[Option[ArticleEntity]] =
    getArticleBySlugQuery(slug).option.transact(T)

  override def getArticle(slug: String, user_id: Long): F[(Option[ArticleResponse], Long)] =
    (for {
      article <- getQuery(slug, user_id).unique
      articleResp <- getTagsQuery(article._1.id.get).to[List].flatMap(a => {
        val arResp = ArticleResponse(article)
        getfavoritesQuery(article._1.id.get).to[List].map(f => arResp.copy(tagList = a, favorited = f.contains(user_id), favoritesCount = f.length))
      })
    } yield (articleResp.some, article._1.id.get)).transact(T)

  override def getArticleById(id: Long, user_id: Long): F[Option[ArticleResponse]] =
    (for {
      article <- getByIdQuery(id, user_id).unique
      articleResp <- getTagsQuery(article._1.id.get).to[List].flatMap(a => {
        val arResp = ArticleResponse(article)
        getfavoritesQuery(article._1.id.get).to[List].map(f => arResp.copy(tagList = a, favorited = f.contains(user_id), favoritesCount = f.length))
      })
    } yield articleResp.some).transact(T)


  override def getRecentFollowedArticles(limit: Long, offset: Long, user_id: Long): F[List[ArticleResponse]] =
    (for {
      articles <- getFollowedArticleQuery(limit, offset, user_id).to[List]
      resp <- articles.map(ar => {
        val arResp = ArticleResponse(ar)
        val a = getTagsQuery(ar._1.id.get).to[List].flatMap(t =>
          getfavoritesQuery(ar._1.id.get).to[List].map(f =>
            arResp.copy(tagList = t, favorited = f.contains(user_id), favoritesCount = f.length)))
        a
      }).sequence
    } yield resp).transact(T)

  override def getRecentArticles(user_id: Long, limit: Long, offset: Long): F[List[ArticleResponse]] = (for {
    articles <- getRecentQuery(user_id, limit, offset).to[List]
    resp <- articles.map(ar => {
      val arResp = ArticleResponse(ar)
      getTagsQuery(ar._1.id.get).to[List].flatMap(t =>
        getfavoritesQuery(ar._1.id.get).to[List].map(f =>
          arResp.copy(tagList = t, favorited = f.contains(user_id), favoritesCount = f.length)))
    }).sequence
  } yield resp).transact(T)

  override def getRecentArticles2(user_id: Long, limit: Long, offset: Long): F[List[ArticleResponse]] =
    getRecentQuery(user_id, limit, offset)
      .to[List]
      .map(ar => ar.map(ArticleResponse(_)))
      .transact(T)

  override def getTags(article_id: Long): F[List[String]] =
    getTagsQuery(article_id)
      .to[List]
      .transact(T)

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
