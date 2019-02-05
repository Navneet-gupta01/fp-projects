package com.realworld.accounts.utils

import com.realworld.accounts.model.AccountEntity
import freestyle.tagless.tagless

@tagless(true)
trait ServerValidations[F[_]] {
  def isEmailTaken(account: List[AccountEntity], email: String): F[Boolean]
  def isUsernameTaken(account: List[AccountEntity], username: String): F[Boolean]
}
