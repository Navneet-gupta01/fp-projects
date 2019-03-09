package com.realworld.comments.persistence

import com.realworld.comments.model.{CommentsEntity, CommentsResponse}
import freestyle.tagless.tagless

@tagless(true)
trait CommentsRepository[F[_]] {
  def createComments(commentsEntity: CommentsEntity, author_id: Long): F[Option[CommentsResponse]]
  def deleteComment(comment_id: Long, user_id: Long, article_id: Long): F[Int]
  def getComments(article_id: Long, user_id: Long): F[List[CommentsResponse]]
  def init: F[Int]
  def drop: F[Int]
  def create: F[Int]
}
