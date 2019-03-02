package com.realworld.accounts.utils

import cats.Monad
import cats.implicits._
import com.realworld.accounts.model.{AccountDomainErrors, AccountEntity}

class ServerValidationsHandler[F[_] : Monad] extends ServerValidations.Handler[F] {
  private def isEmailTaken(account: AccountEntity, email: String): F[Boolean] =
    (account.email === email).pure[F]

  private def isUsernameTaken(account: AccountEntity, username: String): F[Boolean] =
    (account.username === username).pure[F]

  def isEmailTaken(accounts: List[AccountEntity], email: String): F[Boolean] =
    for {
      resp <- accounts.traverse(a => isEmailTaken(a, email))
      res <- resp.foldLeft(false)(_ || _).pure[F]
    } yield res

  def isUsernameTaken(accounts: List[AccountEntity], username: String): F[Boolean] =
    for {
      resp <- accounts.traverse(a => isUsernameTaken(a, username))
      res <- resp.foldLeft(false)((a, b) => a || b).pure[F]
    } yield res


  def hasUniqueFields(accounts: List[AccountEntity], email: String, username: String): F[(Boolean, Boolean)] =
    for {
      emailTaken <- isEmailTaken(accounts, email)
      usernameTaken <- isUsernameTaken(accounts, username)
    } yield (emailTaken, usernameTaken)

  //  def validationErrors(emailExits: Boolean, usernameExist: Boolean, account: AccountEntity): F[List[AccountDomainErrors]] = ???
  //    (emailExits, usernameExist) match {
  //      case (true, true) => List(EmailAlreadyExists(account.email), UsernameAlreadyExists(account.username)).pure[F]
  //      case (false , true) => List(UsernameAlreadyExists(account.username)).pure[F]
  //      case (true, false) => List(EmailAlreadyExists(account.email)).pure[F]
  //      case _ => Nil.pure[F]
  //    }

  def validateCredentials(account: Option[AccountEntity], password: String): F[Boolean] =
    account.fold(false)(a => a.password === password).pure[F]

  override def validationErrors(emailExits: Boolean, usernameExist: Boolean): F[List[AccountDomainErrors]] = ???
}
