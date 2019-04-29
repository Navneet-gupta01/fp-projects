package com.realworld.accounts.persistence

import cats.data.StateT
import cats.data.StateT._
import cats.implicits._
import com.realworld.accounts.TestUtils._
import com.realworld.accounts.model.AccountEntity

class InMemoryAccountsRepository extends AccountRepository.Handler[AccountTest] {

  override def insert(account: AccountEntity): AccountTest[Option[AccountEntity]] = ???

  override def update(account: AccountEntity): AccountTest[Option[AccountEntity]] = ???

  override def updatePassword(account: AccountEntity): AccountTest[Option[AccountEntity]] = for {
    state <- StateT.get[OrError, AccountTestState]
    accountE <- pure[OrError, AccountTestState, Option[AccountEntity]](state.accounts.find(_.email === account.email))
    updatedState <- modify[OrError, AccountTestState](state =>
      state.copy(accounts =
        (state.accounts.filter(_.email =!= account.email) ++ accountE.map(ae => ae.copy(password = account.password)))))
    updatedAccountE <- pure[OrError, AccountTestState, Option[AccountEntity]](state.accounts.find(_.email === account.email))
  } yield updatedAccountE

  override def getByEmail(email: String): AccountTest[Option[AccountEntity]] = for {
    state <- StateT.get[OrError, AccountTestState]
    res <- pure[OrError, AccountTestState, Option[AccountEntity]](state.accounts.find(_.email === email))
  } yield res

  override def getById(id: Long): AccountTest[Option[AccountEntity]] = for {
    state <- StateT.get[OrError, AccountTestState]
    res <- pure[OrError, AccountTestState, Option[AccountEntity]](state.accounts.find(_.id === id.some))
  } yield res

  override def getByUserName(username: String): AccountTest[Option[AccountEntity]] = for {
    state <- StateT.get[OrError, AccountTestState]
    res <- pure[OrError, AccountTestState, Option[AccountEntity]](state.accounts.find(_.username === username))
  } yield res

  override def getUser(id: Option[Long], username: Option[String], email: Option[String]): AccountTest[List[AccountEntity]] = for {
    state <- StateT.get[OrError, AccountTestState]
    res <- pure[OrError, AccountTestState, List[AccountEntity]](state.accounts.filter(a => (a.id === id || a.username.some == username || a.email.some == email)))
  } yield res

  override def delete(id: Long): AccountTest[Int] = for {
    state <- StateT.get[OrError, AccountTestState]
    _ <- modify[OrError, AccountTestState](state => state.copy(accounts = state.accounts.filter(a => a.id.get =!= id)))
    res <- state.accounts.find(_.id === id.some) match {
      case Some(x) => pure[OrError,AccountTestState, Int](1)
      case _       => pure[OrError,AccountTestState, Int](0)
    }
  } yield res

  override def list: AccountTest[List[AccountEntity]] = for {
    state <- StateT.get[OrError, AccountTestState]
    result <- pure[OrError, AccountTestState, List[AccountEntity]](state.accounts)
  } yield result

  override def drop: AccountTest[Int] = ???

  override def create: AccountTest[Int] = ???

  override def init: AccountTest[Int] = ???
}
