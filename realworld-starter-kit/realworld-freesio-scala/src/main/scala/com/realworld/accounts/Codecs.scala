package com.realworld.accounts

import cats.Applicative
import cats.effect.Sync
import com.realworld.accounts.model._
import com.realworld.app.AppCodecs
import io.circe.generic.auto._
import org.http4s.circe.{jsonEncoderOf, jsonOf}
import org.http4s.{EntityDecoder, EntityEncoder}

object Codecs extends AppCodecs {

  implicit def accountEntityEncoder[F[_] : Applicative]: EntityEncoder[F, AccountEntity] = jsonEncoderOf[F, AccountEntity]

  implicit def accountEntityDecoder[F[_] : Sync]: EntityDecoder[F, AccountEntity] = jsonOf[F, AccountEntity]

  implicit def accountFormEncoder[F[_] : Applicative]: EntityEncoder[F, AccountForm] = jsonEncoderOf[F, AccountForm]

  implicit def accountFormDecoder[F[_] : Sync]: EntityDecoder[F, AccountForm] = jsonOf[F, AccountForm]

  implicit def authResponseEncoder[F[_] : Applicative]: EntityEncoder[F, AuthRepsonse] = jsonEncoderOf[F, AuthRepsonse]

  implicit def authResponseDecoder[F[_] : Sync]: EntityDecoder[F, AuthRepsonse] = jsonOf[F, AuthRepsonse]

  implicit def formReqDecoder[F[_] : Sync]: EntityDecoder[F, FormReq] = jsonOf[F, FormReq]
}
