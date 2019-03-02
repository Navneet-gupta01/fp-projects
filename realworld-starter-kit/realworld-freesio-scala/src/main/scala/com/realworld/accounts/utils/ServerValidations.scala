package com.realworld.accounts.utils

import com.realworld.accounts.model.{AccountDomainErrors, AccountEntity}
import freestyle.tagless.tagless

@tagless(true)
trait ServerValidations[F[_]] {
  def isEmailTaken(account: List[AccountEntity], email: String): F[Boolean]

  def isUsernameTaken(account: List[AccountEntity], username: String): F[Boolean]

  def hasUniqueFields(accounts: List[AccountEntity], email: String, username: String): F[(Boolean, Boolean)]

  def validationErrors(emailExits: Boolean, usernameExist: Boolean): F[List[AccountDomainErrors]]

  def validateCredentials(account: Option[AccountEntity], inputPassword: String): F[Boolean]
}
