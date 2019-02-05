package com.realworld.accounts.utils

import cats.Monad
import cats.implicits._
import com.realworld.accounts.model.AccountEntity

class ServerValidationsHandler[F[_]: Monad] extends ServerValidations[F] {
  private def isEmailTaken(account: AccountEntity, email: String): F[Boolean] =
    (account.email === email).pure[F]

  private def isUsernameTaken(account: AccountEntity, username: String): F[Boolean] =
    (account.username === username).pure[F]

  override def isEmailTaken(accounts: List[AccountEntity], email: String): F[Boolean] =
    for {
      resp <- accounts.traverse(a => isEmailTaken(a,email))
      res <- resp.foldLeft(false)(_ || _ )
    } yield res

  override def isUsernameTaken(accounts: List[AccountEntity], username: String): F[Boolean] =
    for {
      resp <- accounts.traverse(a => isUsernameTaken(a,username))
      res <- resp.foldLeft(false)(_ || _ )
    } yield res


  def hasUniqueFields(accounts: List[AccountEntity], email: String, username: String) : F[(Boolean, Boolean)] = {
    val resp = accounts.map( a => (isEmailTaken(a, email), isUsernameTaken(a, username)))
    (false, false).pure[F]
  }

//    for {
//      resp <- accounts.traverse(a => (isEmailTaken(a, email), isUsernameTaken(a, username)))
//      res <- resp.foldLeft((false, false))((a,b) => (a._1 || b._1, a._2 || b._2))
//    } yield res
}
