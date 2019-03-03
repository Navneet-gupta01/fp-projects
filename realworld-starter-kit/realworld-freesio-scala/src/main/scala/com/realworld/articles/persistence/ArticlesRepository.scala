package com.realworld.articles.persistence

import com.realworld.accounts.model.AccountEntity
import com.realworld.articles.model.ArticleEntity
import freestyle.tagless.tagless

@tagless(true)
trait ArticlesRepository[F[_]] {
  import ArticleEntity._

  def getArticle(slug: String): F[Option[ArticleEntity]]
  val list: F[List[ArticleEntity]]
  def getRecentFollowedArticles(limit: Int, offset: Int, user_id: Long) : F[List[ArticleEntity]]
  def getRecentArticles(limit: Int, offset: Int): F[List[ArticleEntity]]
  def updateArticle(articleEntity: ArticleEntity): F[Option[ArticleEntity]]
  def deleteArticle(slug: String): F[Option[ArticleEntity]]
  val init: F[Int]
  val create: F[Int]
  val drop: F[Int]
}
