package com.realworld.accounts.utils

import com.realworld.accounts.model.AccountEntity
import freestyle.tagless.tagless

@tagless(true)
trait Validations[F[_]] {
  def passwordMatch(password: String, confirmPassword: String) : F[Boolean]
  def validUsername(username: String): F[Boolean]
  def validPassword(password: String): F[Boolean]
  def validEmail(email: String): F[Boolean]
  def validAccount(account: AccountEntity): F[Boolean]
}
