package com.realworld.accounts.utils

import java.time.Instant

import cats.Monad
import cats.implicits._
import com.realworld.accounts.model.{AccountEntity, AuthRepsonse, ParsedJWTToken}
import pdi.jwt.{JwtAlgorithm, JwtCirce, JwtClaim}

class TokensHandler[F[_] : Monad] extends Tokens.Handler[F] {

  val secretKey = "iJKV1QiLCJhbGciOiJIUzI1NiJ9"

  override def getToken(registeredAccount: Option[AccountEntity]): F[Option[AuthRepsonse]] =
    registeredAccount.map(ae => AuthRepsonse(ae.email, getJWTToken(ae.id.get, ae.email), ae.username, ae.bio.getOrElse(""), ae.image.getOrElse(""))).pure[F]

  override def parseAccount(token: String): F[Option[ParsedJWTToken]] = ???

  //    JwtCirce.decodeJson(token,secretKey, Seq(JwtAlgorithm.HS256)).map(f => f.as[ParsedJWTToken].some.pure[F])
  //    for {
  //   json  <- JwtCirce.decodeJson(token,secretKey, Seq(JwtAlgorithm.HS256))
  //   parsedToken <- json.as[ParsedJWTToken].pure[F]
  //  } yield parsedToken


  private def getJWTToken(id: Long, email: String): String = {
    val claim = JwtClaim(
      expiration = Instant.now.plusSeconds(60 * 60 * 12).getEpochSecond.some,
      issuedAt = Instant.now.getEpochSecond.some,
      subject = (id + "").some
    )
    val algo = JwtAlgorithm.HS256
    JwtCirce.encode(claim, secretKey, algo)
  }

  //  implicit val parsedJWTTokenDecode: Decoder[ParsedJWTToken] = deriveDecoder
}
