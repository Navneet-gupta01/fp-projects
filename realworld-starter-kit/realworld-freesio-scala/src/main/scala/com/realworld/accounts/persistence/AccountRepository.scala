package com.realworld.accounts.persistence

import com.realworld.accounts.model.AccountEntity
import freestyle.tagless.tagless

@tagless(true)
trait AccountRepository[F[_]] {
  def insertAccount(account: AccountEntity): F[Option[AccountEntity]]
  def updateAccount(account: AccountEntity) : F[Option[AccountEntity]]
  def getAccountByEmail(email: String): F[Option[AccountEntity]]
  def getAccountById(id: Int): F[Option[AccountEntity]]
  def getAccountByUserName(username: String) : F[Option[AccountEntity]]
  def deleteAccount(username: String): F[Int]
}
