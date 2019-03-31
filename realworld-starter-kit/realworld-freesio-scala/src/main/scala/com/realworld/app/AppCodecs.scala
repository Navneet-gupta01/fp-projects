package com.realworld.app

import cats.Applicative
import cats.effect.Sync
import com.realworld.app.errorhandler.ErrorResp
import io.circe.generic.auto._
import org.http4s.circe.{jsonEncoderOf, jsonOf}
import org.http4s.{EntityDecoder, EntityEncoder}

trait AppCodecs {
  implicit def errorRespEncoder[F[_] : Applicative]: EntityEncoder[F, ErrorResp] = jsonEncoderOf[F, ErrorResp]

  implicit def errorRespDecoder[F[_] : Sync]: EntityDecoder[F, ErrorResp] = jsonOf[F, ErrorResp]
}
