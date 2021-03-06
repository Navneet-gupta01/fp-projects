package com.realworld.articles.persistence

import com.realworld.accounts.model.AccountEntity
import com.realworld.articles.model.{ArticleEntity, ArticleResponse}
import freestyle.tagless.tagless

@tagless(true)
trait ArticlesRepository[F[_]] {

  def insertArticle(articleEntity: ArticleEntity, user_id: Long, tags: List[String]): F[Option[Long]]
  def getOwnedArticle(slug: String, user_id: Long): F[Option[ArticleEntity]]
  def getArticleBySlug(slug: String): F[Option[ArticleEntity]]
  def getArticle(slug: String, user_id: Long): F[(Option[ArticleResponse], Long)]
  def getArticleById(id: Long, user_id: Long): F[Option[ArticleResponse]]
  def getRecentFollowedArticles(limit: Long, offset: Long, user_id: Long) : F[List[ArticleResponse]]
  def getRecentArticles(user_id: Long,limit: Long, offset: Long): F[List[ArticleResponse]]
  def getRecentArticles2(user_id: Long,limit: Long, offset: Long): F[List[ArticleResponse]]
  def getTags(article_id :Long): F[List[String]]
  def updateArticle(articleEntity: ArticleEntity): F[Int]
  def deleteArticle(slug: String, user_id: Long): F[Int]
  def init: F[Int]
  def create: F[Int]
  def drop: F[Int]
}
