package com.realworld.articles

import java.util.Date

import cats.Applicative
import cats.effect.Sync
import com.realworld.app.AppCodecs
import com.realworld.articles.model._
import io.circe.{Decoder, Encoder}
import io.circe.generic.auto._
import org.http4s.circe.{jsonEncoderOf, jsonOf}
import org.http4s.{EntityDecoder, EntityEncoder}

object ArticleCodecs extends AppCodecs {

  implicit def articleEntityEncoder[F[_] : Applicative]: EntityEncoder[F, ArticleEntity] = jsonEncoderOf[F, ArticleEntity]

  implicit def articleEntityDecoder[F[_] : Sync]: EntityDecoder[F, ArticleEntity] = jsonOf[F, ArticleEntity]

  implicit val dateEncodeJson: Encoder[Date] = Encoder.forProduct1("date") {
    d : Date => d.getTime
  }
  implicit val dateDecodeJson: Decoder[Date] = Decoder.forProduct1("date") {
    time: Long => new Date(time)
  }

  implicit def articleFormEncoder[F[_] : Applicative]: EntityEncoder[F, ArticleForm] = jsonEncoderOf[F, ArticleForm]

  implicit def articleFormDecoder[F[_] : Sync]: EntityDecoder[F, ArticleForm] = jsonOf[F, ArticleForm]

  implicit def articleResponseEncoder[F[_] : Applicative]: EntityEncoder[F, ArticleResponse] = jsonEncoderOf[F, ArticleResponse]

  implicit def articleResponseDecoder[F[_] : Sync]: EntityDecoder[F, ArticleResponse] = jsonOf[F, ArticleResponse]

  implicit def articleReqDecoder[F[_] : Sync]: EntityDecoder[F, ArticleReq] = jsonOf[F, ArticleReq]
}
