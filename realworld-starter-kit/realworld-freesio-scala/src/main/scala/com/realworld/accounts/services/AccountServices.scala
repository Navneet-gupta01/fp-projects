package com.realworld.accounts.services

import cats.Monad
import cats.implicits._
import com.realworld.accounts.model._
import com.realworld.accounts.persistence.AccountRepository
import freestyle.tagless.effects.error.ErrorM
import freestyle.tagless.logging.LoggingM
import freestyle.tagless._
import freestyle.tagless.effects._
import cats.data.Validated._
import cats.data._
import cats.implicits._
import com.realworld.accounts.utils.AccountValidator

@module
trait AccountServices[F[_]] {
  implicit val M: Monad[F]
  implicit val L: LoggingM[F]

  val model = classOf[AccountEntity].getSimpleName

  val repo : AccountRepository[F]

  val error: ErrorM[F]

  def registerUser(form: AccountForm): F[Option[AccountEntity]] =
    for {
      _ <- L.info(s"Registering model: $model with username: ${form.username} and email: ${form.email}")
      registerUserForm <- error.either[RegisterUserForm](AccountForm.registerUserForm(form).toEither.leftMap(l => InvalidInputParams(l.mkString_("[ ", ", " , " ]"))))
      alreadyRegisteredUser <- repo.getUser(None, form.username, form.email.some)
      _ <- error.either[Boolean](AccountValidator.validateUniquness(alreadyRegisteredUser,registerUserForm).toEither.leftMap(l => InvalidInputParams(l.mkString_("[ ", ", " , " ]")) ))
      registeredAccount <- repo.insert(registerUserForm)
      _ <- L.info(s"Attempted Registering model: $model with username: ${form.username} and email: ${form.email}")
    } yield registeredAccount


  def updateUser(accountForm: AccountForm): F[Option[AccountEntity]] = for {
    _ <- L.info(s"Updating model: $model for email: ${accountForm.email}")
    updateUserForm <- error.either[UpdateUserForm](AccountForm.updateUserForm(accountForm).toEither.leftMap(l => InvalidInputParams(l.mkString_("[ ", ", ", " ]"))))
    account <- repo.getByEmail(updateUserForm.email)
    u <- error.either[AccountEntity](account.toRight(new AccountDoesNotExist(updateUserForm.email)))
    updatedAccount <- repo.update(account.get.copy(bio = updateUserForm.bio, image = updateUserForm.image))
  } yield updatedAccount


  def deleteUser(id: Long) : F[Int] =
    for {
      _ <- L.info(s"Deleting model: $model for id: $id")
      deletedAccount <- repo.delete(id)
      _ <- L.info(s"Deleted model: $model for id: $id")
    } yield deletedAccount

  def updatePassword(accountForm: AccountForm): F[Option[AccountEntity]] =
    for {
      _ <- L.info(s"Updating Password in model: $model for email: ${accountForm.email}")
      updatePasswordForm <- error.either[UpdatePasswordForm](AccountForm.updatePassword(accountForm).toEither.leftMap(l => InvalidInputParams(l.mkString_("[ ",", ", " ]"))))
      account <- repo.getByEmail(updatePasswordForm.email)
      u <- error.either[AccountEntity](account.toRight(new AccountDoesNotExist(updatePasswordForm.email)))
      updatedAccount <- repo.updatePassword(account.get.copy(password = updatePasswordForm.password))
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


