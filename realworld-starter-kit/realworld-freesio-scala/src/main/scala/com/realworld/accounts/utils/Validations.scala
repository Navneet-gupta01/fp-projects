package com.realworld.accounts.utils

import com.realworld.accounts.model.AccountEntity
import freestyle.tagless.tagless

@tagless(true)
trait Validations[F[_]] {
  def matchPassword(password: String, confirmPassword: String) : F[String]
  def validateUsername(username: String): F[String]
  def validatePassword(password: String): F[String]
  def validateEmail(email: String): F[String]
  def validateAccount(account: AccountEntity): F[AccountEntity]
}
