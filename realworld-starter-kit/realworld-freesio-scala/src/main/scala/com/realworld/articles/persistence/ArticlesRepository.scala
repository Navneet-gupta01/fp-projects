package com.realworld.articles.persistence

import com.realworld.accounts.model.AccountEntity
import com.realworld.articles.model.{ArticleEntity, ArticleResponse}
import freestyle.tagless.tagless

@tagless(true)
trait ArticlesRepository[F[_]] {
  import ArticleEntity._

  def insertArticle(articleEntity: ArticleEntity, user_id: Long, tags: List[String]): F[Option[Long]]
  def getArticle(slug: String, user_id: Long): F[Option[ArticleResponse]]
  def getRecentFollowedArticles(limit: Int, offset: Int, user_id: Long) : F[List[ArticleResponse]]
  def getRecentArticles(user_id: Long,limit: Int, offset: Int): F[List[ArticleResponse]]
  def updateArticle(articleEntity: ArticleEntity): F[Int]
  def deleteArticle(slug: String, user_id: Long): F[Int]
  def init: F[Int]
  def create: F[Int]
  def drop: F[Int]
}
