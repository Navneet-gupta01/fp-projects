package com.realworld.articles.services

import cats.Monad
import com.realworld.accounts.model.{AccountForm, InvalidInputParams, UpdateUserForm}
import com.realworld.accounts.persistence.AccountRepository
import com.realworld.articles.model.{ArticleEntity, ArticleForm, ArticleResponse, CreateArticleForm}
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
  import ArticleEntity._

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

//  def insertArticles(form: ArticleForm, user_id: Long): F[Option[ArticleResponse]] =
//    for {
//      _ <- L.info(s"Trying to fetch recent Articles globally for user : ${user_id}")
//      createArticleForm <- error.either[CreateArticleForm](ArticleForm.createArticleForm(form).toEither.leftMap(l => InvalidInputParams(l)))
//      article_id <- repo.insertArticle(createArticleForm, user_id, form.tags.getOrElse(List()))
//      _ <-
//    } yield articles

}
