package com.realworld.comments

import cats.effect.Effect
import cats.implicits._
import com.realworld.app.errorhandler.HttpErrorHandler
import com.realworld.comments.model.{CommentsDomainErrors, CommentsReq}
import freestyle.tagless.logging.LoggingM
import io.circe.generic.auto._
import io.circe.syntax._
import org.http4s._
import org.http4s.circe._
import org.http4s.dsl.Http4sDsl

class CommentsApi[F[_]: Effect](
                                 implicit services: CommentsServices[F],
                                 log: LoggingM[F],
                                 H: HttpErrorHandler[F, CommentsDomainErrors]) extends Http4sDsl[F] {

  import CommentsCodecs._

  val endPoints = HttpRoutes.of[F] {

    case GET -> Root / "articles" / slug / "comments" =>
      services.listComments(slug, 2L) flatMap { item =>
        Ok(item.asJson)
      }

    case req@POST -> Root / "articles" / slug / "comments" =>
      for {
        wrappedReq <- req.as[CommentsReq]
        insertedComments <- services.createQuery(wrappedReq.comment, 2L, slug)
        res <- Ok(insertedComments.asJson)
      } yield res

    case DELETE -> Root / "articles" / slug / "comments" / LongVar(id) =>
      services.deleteComment(slug, id, 2L) flatMap {
        item => Ok(item.asJson)
      }
  }

  val routes: HttpRoutes[F] = H.handle(endPoints)

}

object CommentsApi {
  implicit def instance[F[_]: Effect]( implicit services: CommentsServices[F],
                                       log: LoggingM[F],
                                       H: HttpErrorHandler[F, CommentsDomainErrors]) : CommentsApi[F] = new CommentsApi[F]
}