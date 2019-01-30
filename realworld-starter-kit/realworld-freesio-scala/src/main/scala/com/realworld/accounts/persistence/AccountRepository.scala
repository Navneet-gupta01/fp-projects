package com.realworld.accounts.persistence

import com.realworld.accounts.model.AccountEntity
import freestyle.tagless.tagless

@tagless(true)
trait AccountRepository[F[_]] {
  def insert(account: AccountEntity): F[Option[AccountEntity]]
  def update(account: AccountEntity) : F[Option[AccountEntity]]
  def updatePassword(account: AccountEntity) : F[Option[AccountEntity]]
  def getByEmail(email: String): F[Option[AccountEntity]]
  def getById(id: Int): F[Option[AccountEntity]]
  def getByUserName(username: String) : F[Option[AccountEntity]]
  def delete(id: Long): F[Int]
  def drop: F[Int]
  def create: F[Int]
  def init: F[Int]
}
