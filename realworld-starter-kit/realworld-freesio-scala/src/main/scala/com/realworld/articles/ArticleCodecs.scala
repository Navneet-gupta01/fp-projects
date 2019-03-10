package com.realworld.articles

import java.util.Date

import cats.Applicative
import cats.effect.Sync
import com.realworld.app.AppCodecs
import com.realworld.articles.model._
import io.circe.{Decoder, Encoder, HCursor, Json}
import io.circe.generic.auto._
import org.http4s.circe.{jsonEncoderOf, jsonOf}
import org.http4s.{EntityDecoder, EntityEncoder}

object ArticleCodecs extends AppCodecs {

  implicit def articleEntityEncoder[F[_] : Applicative]: EntityEncoder[F, ArticleEntity] = jsonEncoderOf[F, ArticleEntity]

  implicit def articleEntityDecoder[F[_] : Sync]: EntityDecoder[F, ArticleEntity] = jsonOf[F, ArticleEntity]

//  implicit val dateEncodeJson: Encoder[Date] = new Encoder[Date] {
//    override def apply(a: Date): Json = Encoder.encodeLong.apply(a.getTime)
//  }
////  Encoder.forProduct1("date") {
////    d : Date => d.getTime
////  }
//  implicit val dateDecodeJson: Decoder[Date] = Decoder.forProduct1("date") {
//    time: Long => new Date(time)
//  }

  implicit val DateFormat : Encoder[Date] with Decoder[Date] = new Encoder[Date] with Decoder[Date] {
    override def apply(a: Date): Json = Encoder.encodeLong.apply(a.getTime)

    override def apply(c: HCursor): Decoder.Result[Date] = Decoder.decodeLong.map(s => new Date(s)).apply(c)
  }

  implicit def articleFormEncoder[F[_] : Applicative]: EntityEncoder[F, ArticleForm] = jsonEncoderOf[F, ArticleForm]

  implicit def articleFormDecoder[F[_] : Sync]: EntityDecoder[F, ArticleForm] = jsonOf[F, ArticleForm]

  implicit def articleResponseEncoder[F[_] : Applicative]: EntityEncoder[F, ArticleResponse] = jsonEncoderOf[F, ArticleResponse]

  implicit def articleResponseDecoder[F[_] : Sync]: EntityDecoder[F, ArticleResponse] = jsonOf[F, ArticleResponse]

  implicit def articleReqDecoder[F[_] : Sync]: EntityDecoder[F, ArticleReq] = jsonOf[F, ArticleReq]
}
