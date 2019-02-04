package com.realworld.accounts.utils

import com.realworld.accounts.model.AccountForm
import freestyle.config.Config
import freestyle.tagless.tagless

@tagless(true)
trait Tokens[F[_]: Config] {
  def getToken(userId: String, id: Long, permissions: List[String]): F[String]
  def parseAccount(token: String): F[AccountForm]
}
