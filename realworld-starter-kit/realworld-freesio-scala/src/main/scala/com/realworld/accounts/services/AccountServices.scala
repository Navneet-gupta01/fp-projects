package com.realworld.accounts.services

import cats.Monad
import cats.implicits._
import com.realworld.accounts.model._
import com.realworld.accounts.persistence.AccountRepository
import com.realworld.accounts.utils.{ServerValidations, Tokens, ValidationHandler, Validations}
import freestyle.tagless.effects.error.ErrorM
import freestyle.tagless.logging.LoggingM
import freestyle.tagless._
import freestyle.tagless.effects._
import cats.data.Validated._
import cats.data._
import cats.implicits._
import cats.instances.map

@module
trait AccountServices[F[_]] {
  implicit val M: Monad[F]
  implicit val L: LoggingM[F]
//  implicit val V: Validations[F]
  implicit val S: ServerValidations[F]
  implicit val T: Tokens[F]

  val model = classOf[AccountEntity].getSimpleName

  val repo : AccountRepository[F]

  val error: ErrorM

  val vl = validation[NonEmptyChain[AccountDomainErrors]]
  import vl.implicits._

  def login(loginForm: LoginForm): F[Option[AuthRepsonse]] =
    for {
      account <- repo.getByEmail(loginForm.email)
      validPassword <- S.validateCredentials(account, loginForm.password)
      resp <- if(validPassword) T.getToken(account) else none[AuthRepsonse].pure[F]
    } yield resp

  def updateUser(accountForm: AccountForm): F[Option[AccountEntity]] = for {
    _ <- L.info(s"Updating model: $model for email: ${accountForm.email}")
    account <- repo.getByEmail(accountForm.email)
    u <- error.either[AccountEntity](account.toRight(new NoSuchElementException("Invalid User Email")))
    updatedAccount <-
      account match {
      case Some(x) => repo.update(x.copy(bio = accountForm.bio,image = accountForm.image))
      case _ => none[AccountEntity].pure[F]
    }

    _ <- L.info(s"Updated model: $model for email: ${accountForm.email}")
  } yield updatedAccount

  def getCurrentUser(email: String): F[Option[AccountEntity]] =
    for {
      _ <- L.info(s"Getting User Details From Token for model: $model")
      account <- repo.getByEmail(email)
      u <- error.either[AccountEntity](account.toRight(new NoSuchElementException("Invalid Auth Token")))
      _ <- L.info(s"Tried Getting model : $model details for email: $email")
    } yield account

  def registerUser(account: AccountEntity): F[Option[AuthRepsonse]] =
    for {
      _ <- L.info(s"Registering model: $model with username: ${account.username} and email: ${account.email}")
      registeredAccount <- repo.insert(account)
      u <- error.either[AccountEntity](registeredAccount.toRight(new NoSuchElementException("Invalid Input Params")))
      authResponse <- T.getToken(registeredAccount)
      _ <- L.info(s"Registered model: $model with username: ${account.username} and email: ${account.email}")
    } yield authResponse

  def deleteUser(id: Long) : F[Int] =
    for {
      _ <- L.info(s"Deleting model: $model for id: $id")
      deletedAccount <- repo.delete(id)
      _ <- L.info(s"Deleted model: $model for id: $id")
    } yield deletedAccount

  def updatePassword(accountForm: AccountForm): F[Option[AccountEntity]] =
    for {
      _ <- L.info(s"Updating Password in model: $model for email: ${accountForm.email}")
      account <- repo.getByEmail(accountForm.email)
      updatedAccount <-
        account match {
          case Some(x) => repo.updatePassword(x.copy(password = accountForm.password.get))
          case _ => none[AccountEntity].pure[F]
        }
      _ <- L.info(s"Updated Password  in model: $model for email: ${accountForm.email}")
    } yield updatedAccount

  def fetch(id: Option[Long], email: Option[String], username: Option[String]): F[List[AccountEntity]] =
    for {
      _ <- L.info(s"Fetching account for id: $id , email : $email, and username: $username")
      accountsFetched <- repo.getUser(id,username,email)
      _ <- L.info(s"Successfully Fetched record for model $model")
    } yield accountsFetched

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

}


