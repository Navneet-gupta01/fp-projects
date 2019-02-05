package com.realworld.accounts.services

import cats.Monad
import cats.implicits._
import com.realworld.accounts.model._
import com.realworld.accounts.persistence.AccountRepository
import com.realworld.accounts.utils.{ServerValidations, ValidationHandler, Validations}
import freestyle.tagless.effects.error.ErrorM
import freestyle.tagless.logging.LoggingM
import freestyle.tagless._
import freestyle.tagless.effects._
import cats.data.Validated._
import cats.data._
import cats.implicits._

@module
trait AccountServices[F[_]] {
  implicit val M: Monad[F]
  implicit val L: LoggingM[F]
  implicit val V: Validations[F]
  implicit val S: ServerValidations[F]

  val model = classOf[AccountEntity].getSimpleName

  val repo : AccountRepository[F]

  val error: ErrorM

  val vl = validation[NonEmptyChain[AccountDomainErrors]]
  import vl.implicits._

  def login(loginForm: LoginForm): F[Option[AuthRepsonse]] = ???
//    for {
//
//      account <- repo.getByEmail(loginForm.email)
//      u <- error.either[AccountEntity](account.toRight(new NoSuchElementException("Invalid Email")))
//      valid <- V.validAccount(account)
//      createdAccount <- if(valid) repo.insert(account)
//      else error.either[AccountEntity](account.toRight(new NoSuchElementException("Invalid Email"))
//    } yield createdAccount

  def updateUser(accountForm: AccountForm): F[Option[AccountEntity]] = ???

  def getCurrentUser: F[Option[AccountEntity]] = ???

  def registerUser(accountForm: AccountForm): F[AuthRepsonse] = ???
//    for {
//      registeredAccount <- repo.insert(accountForm)
//    } yield registeredAccount

  def deleteUser(id: Long) : F[Int] = ???

  def updatePassword(accountForm: AccountForm): F[Option[AccountEntity]] = ???

  import cats.syntax.either._

  def insert(account: AccountEntity): F[Either[List[AccountDomainErrors], Option[AccountEntity]]] =
    for {
      _ <- L.info(s"Inserting model: $model with username: ${account.username} and email: ${account.email}")
      accounts <- repo.getUser(None, account.username.some, account.email.some)
      (emailExists,usernameExists) <- S.hasUniqueFields(accounts, account.email, account.username)
      _ <- S.validationErrors(emailExists, usernameExists).asLeft
      eitherAccount <- repo.insert(account)
      ai <- eitherAccount.flatMap(x => repo.getById(x._1).flatMap(a => a.asRight[AccountDomainErrors]))
      accountInserted  <- ai
      _ <- L.info(s"Successfully Inserted the record for model $model")
    } yield accountInserted

  def fetch(id: Option[Long], email: Option[String], username: Option[String]): F[List[AccountEntity]] =
    for {
      _ <- L.info(s"Fetching account for id: $id , email : $email, and username: $username")
      accountsFetched <- repo.getUser(id,username,email)
      _ <- L.info(s"Successfully Fetched record for model $model")
    } yield accountsFetched

  def delete(id:Long) = ???

  val reset: F[Int] =
    for {
      _ <- L.debug(s"Trying to reset the model: $model")
      resetedItems <- repo.init
      _ <- L.debug(s"Tried Resetting the model: $model")
    } yield resetedItems

  val list: F[List[AccountEntity]] =
    for {
      _ <- L.debug(s"Trying to list the model: $model")
      accountsList <- repo.list
      _ <- L.debug(s"Tried listing the model: $model")
    } yield accountsList


  def updateAccount(account: AccountEntity): F[Option[AccountEntity]] =
    for {
      _ <- L.debug(s"Trying to update the model: $model")
      updatedAccount <- repo.update(account)
      _ <- L.debug(s"Tried updating the model: $model")
    } yield updatedAccount
}


