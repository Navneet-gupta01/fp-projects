package com.realworld.accounts.services

import cats.Monad
import com.realworld.accounts.model._
import freestyle.tagless.effects.error.ErrorM
import freestyle.tagless.logging.LoggingM
import freestyle.tagless.module
import cats.implicits._
import com.realworld.accounts.persistence.AccountRepository
import com.realworld.accounts.utils.{AccountValidator, Tokens}

@module
trait AuthServices[F[_]] {
  implicit val M : Monad[F]
  implicit val L: LoggingM[F]
  val error: ErrorM[F]
  val repo: AccountRepository[F]
  val T: Tokens[F]


    def login(form: AccountForm): F[Option[AuthRepsonse]] =
      for {
        _ <- L.info(s"Trying Logging User ${form.email}")
        loginForm <- error.either[LoginForm](AccountForm.loginForm(form).toEither.leftMap(l => InvalidInputParams(l)))
        account <- repo.getByEmail(loginForm.email)
        _ <- error.either[Boolean](AccountValidator.validateCredentials(account, loginForm).toEither.leftMap(l => InvalidCredentials(l)))
        resp <- T.getToken(account)
      } yield resp


//    def getCurrentUser(email: String): F[Option[AccountEntity]] =
//      for {
//        _ <- L.info(s"Getting User Details From Token for model: $model")
//        account <- repo.getByEmail(email)
//        u <- error.either[AccountEntity](account.toRight(new NoSuchElementException("Invalid Auth Token")))
//        _ <- L.info(s"Tried Getting model : $model details for email: $email")
//      } yield account
}
