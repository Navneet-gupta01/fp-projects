package com.realworld.accounts.persistence.runtime

import cats.Monad
import com.realworld.accounts.model.AccountEntity
import com.realworld.accounts.persistence.AccountRepository
import doobie.implicits._
import doobie.util.transactor.Transactor

class AccountRepositoryHandler[F[_]: Monad](implicit T: Transactor[F])
  extends AccountRepository.Handler[F] {

  import com.realworld.accounts.persistence.AccountQueries._

  def insert(account: AccountEntity): F[Option[AccountEntity]]  =
    insertQuery(account)
      .withUniqueGeneratedKeys[Int]("id")
      .flatMap(getByIdQuery(_).option)
      .transact(T)

//  def insert(account: AccountEntity): F[Either[AccountDomainErrors, (Long, String, String)]] =
//    insertQuery(account)
//      .withUniqueGeneratedKeys[(Long, String, String)]("id", "username", "email")
//      .attemptSomeSqlState[AccountDomainErrors]{case sqlstate.class23.UNIQUE_VIOLATION => UsernameAlreadyExists(account.username) }
//      .transact(T)

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
    getByEmailQuery(email)
      .option
      .transact(T)

  def getById(id: Long): F[Option[AccountEntity]] =
    getByIdQuery(id)
      .option
      .transact(T)

  def getByUserName(username: String) : F[Option[AccountEntity]] =
    getByUsernameQuery(username)
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

  def list:F[List[AccountEntity]] =
    listQuery
      .to[List]
      .transact(T)


  def init: F[Int] =
    dropQuery
      .run
      .flatMap(drops =>
        createQuery
          .run
          .map(_ + drops))
      .transact(T)

  def getUser(id: Option[Long], username: Option[String], email: Option[String]):
    F[List[AccountEntity]] =
    getQuery(id, username, email)
      .to[List]
      .transact(T)
}
