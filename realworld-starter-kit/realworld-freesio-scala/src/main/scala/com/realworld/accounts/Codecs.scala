package com.realworld.accounts

import cats.Applicative
import cats.effect.Sync
import com.realworld.accounts.model._
import io.circe.{Decoder, ObjectEncoder}
import io.circe.generic.auto._
import org.http4s.circe.{jsonEncoderOf, jsonOf}
import org.http4s.{EntityDecoder, EntityEncoder}
import io.circe.syntax._
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

object Codecs {

  implicit def accountEntityEncoder[F[_] : Applicative]: EntityEncoder[F, AccountEntity] = jsonEncoderOf[F, AccountEntity]

  implicit def accountEntityDecoder[F[_] : Sync]: EntityDecoder[F, AccountEntity] = jsonOf[F, AccountEntity]

  implicit def accountFormEncoder[F[_]: Applicative]: EntityEncoder[F, AccountForm] = jsonEncoderOf[F, AccountForm]

  implicit def accountFormDecoder[F[_]: Sync]: EntityDecoder[F, AccountForm] = jsonOf[F, AccountForm]

  implicit def authResponseEncoder[F[_]: Applicative]: EntityEncoder[F, AuthRepsonse] = jsonEncoderOf[F, AuthRepsonse]

  implicit def authResponseDecoder[F[_]:Sync] : EntityDecoder[F, AuthRepsonse] = jsonOf[F, AuthRepsonse]

  implicit def formReqDecoder[F[_]:Sync]: EntityDecoder[F, FormReq] = jsonOf[F, FormReq]

  implicit def errorRespEncoder[F[_]:Sync]: EntityEncoder[F, ErrorResp] = jsonEncoderOf[F, ErrorResp]

  implicit def errorRespDecoder[F[_]:Sync]: EntityDecoder[F, ErrorResp] = jsonOf[F, ErrorResp]
//
//  implicit def accountFormEncodeJson: ObjectEncoder[AccountForm] = deriveEncoder
//  implicit def accountFormDecodeJson: Decoder[AccountForm] = deriveDecoder
//
//  implicit def formReqEncodeJson: ObjectEncoder[FormReq[AccountForm]] = deriveEncoder
//  implicit def fromReqDecodeJson: Decoder[FormReq[AccountForm]] = deriveDecoder



}
