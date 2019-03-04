package com.realworld.articles.persistence

import com.realworld.accounts.model.AccountEntity
import com.realworld.articles.model.{ArticleEntity, ArticleResponse}
import freestyle.tagless.tagless

@tagless(true)
trait ArticlesRepository[F[_]] {
  import ArticleEntity._

  def getArticle(slug: String, user_id: Long): F[Option[ArticleResponse]]
  def list(limit: Int, offset: Int): F[List[ArticleResponse]]
  def getRecentFollowedArticles(limit: Int, offset: Int, user_id: Long) : F[List[ArticleResponse]]
  def getRecentArticles(user_id: Long,limit: Int, offset: Int): F[List[ArticleResponse]]
  def updateArticle(articleEntity: ArticleEntity): F[Option[ArticleEntity]]
  def deleteArticle(slug: String): F[Option[ArticleEntity]]
  def init: F[Int]
  def create: F[Int]
  def drop: F[Int]
}
