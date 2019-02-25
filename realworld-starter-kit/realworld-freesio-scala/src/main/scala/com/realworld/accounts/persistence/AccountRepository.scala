package com.realworld.accounts.persistence

import com.realworld.accounts.model.AccountEntity
import freestyle.tagless.tagless

@tagless(true)
trait AccountRepository[F[_]] {
  import AccountEntity._

  def insert(account: AccountEntity): F[Option[AccountEntity]]
  def update(account: AccountEntity) : F[Option[AccountEntity]]
  def updatePassword(account: AccountEntity) : F[Option[AccountEntity]]
  def getByEmail(email: String): F[Option[AccountEntity]]
  def getById(id: Long): F[Option[AccountEntity]]
  def getByUserName(username: String) : F[Option[AccountEntity]]
  def getUser(id: Option[Long] = None, username: Option[String], email: Option[String]): F[List[AccountEntity]]
  def delete(id: Long): F[Int]
  def list: F[List[AccountEntity]]
  def drop: F[Int]
  def create: F[Int]
  def init: F[Int]
}
