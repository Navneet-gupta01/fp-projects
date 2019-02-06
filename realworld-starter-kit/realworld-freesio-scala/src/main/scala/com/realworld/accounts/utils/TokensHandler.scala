package com.realworld.accounts.utils

import cats.Monad
import cats.implicits._
import com.realworld.accounts.model.{AccountEntity, AuthRepsonse}

class TokensHandler[F[_]: Monad] extends Tokens.Handler[F] {
  override def getToken(registeredAccount: Option[AccountEntity]): F[Option[AuthRepsonse]] =
    registeredAccount.map(ae => AuthRepsonse(ae.email,ae.email + "USerToken", ae.username, ae.bio.getOrElse(""), ae.image.getOrElse(""), ae.id.get)).pure[F]

  override def parseAccount(token: String): F[Option[String]] = ???
}
