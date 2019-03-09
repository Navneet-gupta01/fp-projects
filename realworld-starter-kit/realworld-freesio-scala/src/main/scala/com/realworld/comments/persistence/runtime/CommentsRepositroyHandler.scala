package com.realworld.comments.persistence.runtime

import cats.Monad
import com.realworld.comments.model.{CommentsEntity, CommentsResponse}
import com.realworld.comments.persistence.CommentsRepository
import doobie.util.transactor.Transactor
import doobie.implicits._

class CommentsRepositroyHandler[F[_]: Monad](implicit T: Transactor[F]) extends CommentsRepository.Handler[F] {
  import com.realworld.comments.persistence.CommentsQueries._

  override def createComments(commentsEntity: CommentsEntity, author_id: Long): F[Option[CommentsResponse]] =
    insertQuery(commentsEntity,author_id)
      .withUniqueGeneratedKeys[Long]("id")
      .flatMap(id => getQuery(id)
        .map(a => CommentsResponse(a._1.body,a._1.created_at, a._1.updated_at,id,a._2))
        .option)
      .transact(T)

  override def deleteComment(comment_id: Long, user_id: Long, article_id: Long): F[Int] =
    deleteQuery(comment_id, user_id, article_id)
      .run
      .transact(T)

  override def getComments(article_id: Long, user_id: Long): F[List[CommentsResponse]] =
    listQuery(article_id,user_id)
      .map(a =>
        CommentsResponse(a._1.body,a._1.created_at, a._1.updated_at,a._1.id.get,a._2))
      .to[List]
      .transact(T)


  override def init: F[Int] =
    dropQuery.run
      .flatMap(
        drops =>
          createQuery.run
            .map(_ + drops))
      .transact(T)

  override def drop: F[Int] =
    dropQuery.run
      .transact(T)

  override def create: F[Int] =
    createQuery.run
      .transact(T)
}
