package com.realworld.accounts.services

import cats.Monad
import freestyle.tagless.logging.LoggingM
import freestyle.tagless.module

@module
trait AuthServices[F[_]] {
  implicit val M : Monad[F]
  implicit val L: LoggingM[F]


  //  def login(loginForm: LoginForm): F[Option[AuthRepsonse]] =
  //    for {
  //      account <- repo.getByEmail(loginForm.email)
  //      validPassword <- S.validateCredentials(account, loginForm.password)
  //      resp <- if(validPassword) T.getToken(account) else none[AuthRepsonse].pure[F]
  //    } yield resp


  //  def getCurrentUser(email: String): F[Option[AccountEntity]] =
  //    for {
  //      _ <- L.info(s"Getting User Details From Token for model: $model")
  //      account <- repo.getByEmail(email)
  //      u <- error.either[AccountEntity](account.toRight(new NoSuchElementException("Invalid Auth Token")))
  //      _ <- L.info(s"Tried Getting model : $model details for email: $email")
  //    } yield account
}
