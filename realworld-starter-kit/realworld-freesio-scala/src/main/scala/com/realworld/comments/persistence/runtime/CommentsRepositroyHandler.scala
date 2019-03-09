package com.realworld.comments.persistence.runtime

import cats.Monad
import com.realworld.comments.model.{CommentsEntity, CommentsResponse}
import com.realworld.comments.persistence.CommentsRepository
import doobie.util.transactor.Transactor

class CommentsRepositroyHandler[F[_]: Monad](implicit T: Transactor[F]) extends CommentsRepository.Handler[F] {
  override def createComments(commentsEntity: CommentsEntity, author_id: Long): F[Option[CommentsResponse]] = ???

  override def deleteComment(comment_id: Long): F[Int] = ???

  override def getComments(article_id: Long, user_id: Long): F[List[CommentsResponse]] = ???

  override def init: F[Int] = ???

  override def drop: F[Int] = ???

  override def create: F[Int] = ???
}
