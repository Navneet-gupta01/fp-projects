package com.realworld.accounts.persistence.runtime

import cats.Monad
import com.realworld.accounts.model.AccountEntity
import com.realworld.accounts.persistence.AccountRepository
import doobie.util.transactor.Transactor
import doobie.implicits._

class AccountRepositoryHandler[F[_]: Monad](implicit T: Transactor[F])
  extends AccountRepository[F] {

  import com.realworld.accounts.persistence.AccountQueries._

  def insert(account: AccountEntity): F[Option[AccountEntity]]  =
    insertQuery(account)
      .withUniqueGeneratedKeys[Long]("id")
      .flatMap(getByIdQuery(_).option)
      .transact(T)

  def update(account: AccountEntity) : F[Option[AccountEntity]] =
    updateQuery(account)
      .run
      .flatMap(getByIdQuery(_).option)
      .transact(T)

  def updatePassword(account: AccountEntity) : F[Option[AccountEntity]] =
    updatePasswordQuery(account)
      .run
      .flatMap(getByIdQuery(_).option)
      .transact(T)

  def getByEmail(email: String): F[Option[AccountEntity]] =
    getQuery(email = Some(email))
      .option
      .transact(T)

  def getById(id: Int): F[Option[AccountEntity]] =
    getQuery(id = Some(id))
      .option
      .transact(T)

  def getByUserName(username: String) : F[Option[AccountEntity]] =
    getQuery(username = Some(username))
      .option
      .transact(T)

  def delete(id: Long): F[Int] =
    deleteQuery(id)
      .run
      .transact(T)

  def drop: F[Int] =
    dropQuery
      .run
      .transact(T)

  def create: F[Int] =
    createQuery
      .run
      .transact(T)

  def init: F[Int] =
    dropQuery
      .run
      .flatMap(drops =>
        createQuery
          .run
          .map(_ + drops))
      .transact(T)
}
