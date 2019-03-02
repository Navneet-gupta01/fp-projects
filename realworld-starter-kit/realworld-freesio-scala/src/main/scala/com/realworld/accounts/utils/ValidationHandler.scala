package com.realworld.accounts.utils

import cats.data.Validated._
import cats.data._
import cats.implicits._
import com.realworld.accounts.model._

class ValidationHandler {

  type ValidationResult[A] = ValidatedNec[AccountDomainErrors, A]

  def matchPasswordConfirmaPassword(password: String, confirmPassword: String): ValidationResult[String] =
    if (password.contentEquals(confirmPassword)) password.validNec
    else PasswordConfirmPasswordMismatch.invalidNec

  def isUsernameValid(userName: String): ValidationResult[String] =
    if (userName.matches("^[a-zA-Z0-9]+$")) userName.validNec
    else InvalidInput("username", "must contain only characters a-z, A-Z and Digits 0-9").invalidNec

  def isValidPassword(password: String): ValidationResult[String] =
    if (password.matches("(?=^.{6,}$)((?=.*\\d)|(?=.*\\W+))(?![.\\n])(?=.*[A-Z])(?=.*[a-z]).*$")) password.validNec
    else InvalidInput("password", "must be atleast 6 characters long and must contain at least one Character a-z/A-Z, at least one Digit 0-9 and atleast one Special character").invalidNec

  def isValidEmail(email: String): ValidationResult[String] =
    if (email.contains("@")) email.validNec
    else InvalidInput("email", "must be a proper email format").invalidNec

  def isValidPassword(password: String, confirmPassword: String): ValidationResult[String] =
    (matchPasswordConfirmaPassword(password, confirmPassword), isValidPassword(password)).mapN((a, b) => b)

  def isValidAccount(account: AccountForm): ValidationResult[AccountEntity] =
    (isUsernameValid(account.username.getOrElse("")),
      isValidPassword(account.password.getOrElse(""), account.confirmPassword.getOrElse("")),
      isValidEmail(account.email)
    ).mapN((a, b, c) => AccountEntity(c, b, a, account.bio, account.image))


  //  def matchPassword(password: String, confirmPassword: String): F[String] = matchPasswordConfirmaPassword(password,confirmPassword).pure[F]
  //
  //
  //  def validateUsername(username: String): F[String] = ??? // (username.length > 6).pure[F]
  //
  //  def validatePassword(password: String): F[String] = ??? // (password.length > 8 && password.matches(passwordRegex.toString())).pure[F]
  //
  //  def validateEmail(email: String): F[String] = ??? //email.contains("@").pure[F]
  //
  //  def validateAccount(account: AccountEntity): F[AccountEntity] = ???
}

object ValidationHandler {
  implicit def instance: ValidationHandler = new ValidationHandler
}