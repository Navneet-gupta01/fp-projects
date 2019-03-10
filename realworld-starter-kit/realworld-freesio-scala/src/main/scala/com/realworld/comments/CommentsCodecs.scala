package com.realworld.comments

import java.util.Date

import cats.Applicative
import cats.effect.Sync
import com.realworld.app.AppCodecs
import io.circe.generic.auto._
import org.http4s.circe.{jsonEncoderOf, jsonOf}
import com.realworld.comments.model.{CommentForm, CommentsEntity, CommentsReq, CommentsResponse}
import io.circe.{Decoder, Encoder}
import org.http4s.{EntityDecoder, EntityEncoder}

object CommentsCodecs extends AppCodecs {

  implicit def commentsEntityEncoder[F[_] : Applicative]: EntityEncoder[F, CommentsEntity] = jsonEncoderOf[F, CommentsEntity]

  implicit def commentsEntityDecoder[F[_] : Sync]: EntityDecoder[F, CommentsEntity] = jsonOf[F, CommentsEntity]

  implicit val dateEncodeJson: Encoder[Date] = Encoder.forProduct1("date") {
    d : Date => d.getTime
  }
  implicit val dateDecodeJson: Decoder[Date] = Decoder.forProduct1("date") {
    time: Long => new Date(time)
  }

  implicit def commentsFormEncoder[F[_] : Applicative]: EntityEncoder[F, CommentForm] = jsonEncoderOf[F, CommentForm]

  implicit def commentsFormDecoder[F[_] : Sync]: EntityDecoder[F, CommentForm] = jsonOf[F, CommentForm]

  implicit def commentsResponseEncoder[F[_] : Applicative]: EntityEncoder[F, CommentsResponse] = jsonEncoderOf[F, CommentsResponse]

  implicit def commentsResponseDecoder[F[_] : Sync]: EntityDecoder[F, CommentsResponse] = jsonOf[F, CommentsResponse]

  implicit def commentsReqDecoder[F[_] : Sync]: EntityDecoder[F, CommentsReq] = jsonOf[F, CommentsReq]

}
