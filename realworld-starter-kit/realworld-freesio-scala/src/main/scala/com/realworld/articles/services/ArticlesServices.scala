package com.realworld.articles.services

import cats.Monad
import com.realworld.accounts.persistence.AccountRepository
import com.realworld.articles.model._
import com.realworld.articles.persistence.ArticlesRepository
import freestyle.tagless.effects.error.ErrorM
import freestyle.tagless.logging.LoggingM
import freestyle.tagless.module
import freestyle.tagless.effects._
import cats.data.Validated._
import cats.data._
import cats.implicits._

@module
trait ArticlesServices[F[_]] {
  implicit val M: Monad[F]

  implicit val L: LoggingM[F]

  val model = classOf[ArticleEntity].getSimpleName
  val error: ErrorM[F]

  val repo: ArticlesRepository[F]

  def getRecentFollowedUsersArticles(user_id: Long, limit: Long, offset: Long) : F[List[ArticleResponse]] =
    for {
      _ <- L.info(s"Trying to fetch recent Articles by author user is folowing : ${user_id}")
      articles <- repo.getRecentFollowedArticles(limit, offset, user_id)
      _ <- L.info(s"Tried Fetching the Followed Authors Recent Articles")
    } yield articles

  def getRecentArticlesGlobally(user_id: Long, limit: Long, offset: Long) : F[List[ArticleResponse]] =
    for {
      _ <- L.info(s"Trying to fetch recent Articles globally for user : ${user_id}")
      articles <- repo.getRecentArticles(user_id, limit, offset)
      _ <- L.info(s"Tried Fetching the global Recent Articles")
    } yield articles

  def insertArticles(form: ArticleForm, user_id: Long): F[Option[ArticleResponse]] =
    for {
      _ <- L.info(s"Trying to fetch recent Articles globally for user : ${user_id}")
      createArticleForm <- error.either[CreateArticleForm](ArticleForm.createArticleForm(form).toEither.leftMap(l => InvalidInputParams(l)))
      article_id <- repo.insertArticle(ArticleEntity.createArticleFormToArticleEntity(createArticleForm), user_id, form.tags.getOrElse(List()))
      article <- repo.getArticleById(article_id.get, user_id)
    } yield article


  def updateArticle(form: ArticleForm, user_id: Long): F[Option[ArticleResponse]] =
    for {
      _ <- L.info(s"Trying to Update Article for  author : ${user_id} on his article: ${form.slug}")
      updateArticleForm <- error.either[UpdateArticleForm](ArticleForm.updateArticleForm(form).toEither.leftMap(l => InvalidInputParams(l)))
      article <- repo.getOwnedArticle(updateArticleForm.slug, user_id)
      _ <- error.either[ArticleEntity](article.toRight(ArticleDoesNotExist(updateArticleForm.slug)))
      articleUpdated <- repo.updateArticle(article.get.copy(title = updateArticleForm.title, description = updateArticleForm.description, body = updateArticleForm.body))
      _ <- L.info(s"Tried Updating the Article for  author : ${user_id} on his article: ${form.slug}")
      articleResp <- repo.getArticle(updateArticleForm.slug, user_id)
    } yield articleResp

  def deleteArticle(slug: String, user_id: Long): F[Int] =
  for {
    _ <- L.info(s"Trying to delete Article for  author : ${user_id} on his article: ${slug}")
    article <- repo.getOwnedArticle(slug, user_id)
    _ <- error.either[ArticleEntity](article.toRight(ArticleDoesNotExist(slug)))
    deleted <- repo.deleteArticle(slug, user_id)
  } yield deleted


  def getArticle(slug: String, user_id: Long): F[Option[ArticleResponse]] =
    for {
      _ <- L.info(s"Trying to fetch Article for slug : ${slug}")
      article <- repo.getArticle(slug, user_id)
      _ <- L.info(s"Tried Fetching article by slug: ${slug}")
    } yield article

}
