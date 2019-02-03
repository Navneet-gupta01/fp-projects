package com.realworld.accounts.utils

import cats.Monad
import cats.implicits._

import scala.util.matching.Regex

class ValidationHandler[F[_]: Monad] extends Validations[F] {

  val passwordRegex:Regex = "[0-9]".r

  def passwordMatch(password: String, confirmPassword: String): F[Boolean] =
    (!password.isEmpty && password.matches(confirmPassword)).pure[F]

  def validUsername(username: String): F[Boolean] = (username.length > 6).pure[F]

  def validPassword(password: String): F[Boolean] = (password.length > 8 && password.matches(passwordRegex))

  def validEmail(email: String): F[Boolean] = ???
}
