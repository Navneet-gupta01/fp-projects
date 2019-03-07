package com.realworld.articles

import cats.Applicative
import cats.effect.Sync
import com.realworld.accounts.model.{AccountEntity, AccountForm, AuthRepsonse, FormReq}
import com.realworld.app.AppCodecs
import com.realworld.articles.model.{ArticleEntity, ArticleForm, ArticleReq, ArticleResponse}
import io.circe.generic.auto._
import org.http4s.circe.{jsonEncoderOf, jsonOf}
import org.http4s.{EntityDecoder, EntityEncoder}
import io.circe.syntax._
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

object ArticleCodecs extends AppCodecs {
  implicit def articleEntityEncoder[F[_] : Applicative]: EntityEncoder[F, ArticleEntity] = jsonEncoderOf[F, ArticleEntity]

  implicit def articleEntityDecoder[F[_] : Sync]: EntityDecoder[F, ArticleEntity] = jsonOf[F, ArticleEntity]

  implicit def articleFormEncoder[F[_] : Applicative]: EntityEncoder[F, ArticleForm] = jsonEncoderOf[F, ArticleForm]

  implicit def articleFormDecoder[F[_] : Sync]: EntityDecoder[F, ArticleForm] = jsonOf[F, ArticleForm]

  implicit def articleResponseEncoder[F[_] : Applicative]: EntityEncoder[F, ArticleResponse] = jsonEncoderOf[F, ArticleResponse]

  implicit def articleResponseDecoder[F[_] : Sync]: EntityDecoder[F, ArticleResponse] = jsonOf[F, ArticleResponse]

  implicit def articleReqDecoder[F[_] : Sync]: EntityDecoder[F, ArticleReq] = jsonOf[F, ArticleReq]
}
