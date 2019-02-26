package com.realworld.accounts.utils

import com.realworld.accounts.model.{AccountEntity, AuthRepsonse, ParsedJWTToken}
import freestyle.tagless.tagless

@tagless(true)
trait Tokens[F[_]] {
  def getToken(registeredAccount: Option[AccountEntity]): F[Option[AuthRepsonse]]
  def parseAccount(token: String): F[Option[ParsedJWTToken]]
}
