package com.realworld.comments

import java.util.Date

import cats.Monad
import cats.implicits._
import com.realworld.articles.model.ArticleEntity
import com.realworld.articles.persistence.ArticlesRepository
import com.realworld.comments.model.{ArticleDoesNotExist, CommentForm, CommentsEntity, CommentsResponse}
import com.realworld.comments.persistence.CommentsRepository
import freestyle.tagless.effects.error.ErrorM
import freestyle.tagless.logging.LoggingM
import freestyle.tagless.module
import freestyle.tagless.effects._
import cats.data.Validated._
import cats.data._

@module
trait CommentsServices[F[_]] {
  implicit val M: Monad[F]
  implicit val L: LoggingM[F]

  val repo: CommentsRepository[F]
  val articleRepo: ArticlesRepository[F]
  val error: ErrorM[F]

  def createQuery(body: CommentForm, user_id: Long, slug: String): F[Option[CommentsResponse]] =
    for {
      _ <- L.info(s"Trying to create a comment for article by slug: ${slug} by user_id: ${user_id}")
      article <- articleRepo.getArticleBySlug(slug)
      _ <- error.either[ArticleEntity](article.toRight(ArticleDoesNotExist(slug)))
      comment <- repo.createComments(CommentsEntity(body.body, new Date(), new Date(), article.get.id), user_id)
      _ <- L.info(s"Tryied to create a comment for article by slug: ${slug} by user_id: ${user_id}")
    } yield comment


  def listComments(slug: String, user_id: Long): F[List[CommentsResponse]] =
    for {
      _ <- L.info(s"Trying to get comments list for article by slug: ${slug} by user_id: ${user_id}")
      article <- articleRepo.getArticleBySlug(slug)
      _ <- error.either[ArticleEntity](article.toRight(ArticleDoesNotExist(slug)))
      comments <- repo.getComments(article.get.id.get, user_id)
      _ <- L.info(s"Tryied to get comments list for article by slug: ${slug} by user_id: ${user_id}")
    } yield comments

  def deleteComment(slug:String, comment_id:Long, user_id: Long): F[Int] =
    for {
      _ <- L.info(s"Trying to delete comment for article by slug: ${slug} and comment_id=${comment_id} by user_id: ${user_id}")
      article <- articleRepo.getArticleBySlug(slug)
      _ <- error.either[ArticleEntity](article.toRight(ArticleDoesNotExist(slug)))
      deletedComments <- repo.deleteComment(comment_id,user_id,article.get.id.get)
      _ <- L.info(s"Tryied to delete comment for article by slug: ${slug} and comment_id=${comment_id} by user_id: ${user_id}")
    } yield deletedComments
}
