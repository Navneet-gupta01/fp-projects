package com.realworld.accounts.model

import com.realworld.{AppError, ResponsePayLoad}
import io.circe.generic.JsonCodec
import sun.security.util.Password

final case class AuthRepsonse(email: String, token: String, username: String, bio: String = "", image: String = "") extends ResponsePayLoad

sealed trait AccountError extends AppError

final case class AuthenticationFailed(msg: String, code: Int) extends AccountError

final case object UserDoesNotExist extends AccountError

//final case class LoginForm(email: String, password: String)
//final case class AccountForm(username: Option[String] , email: String, bio: Option[String], image: Option[String], password: Option[String], confirmPassword: Option[String], id: Option[Long])

case class AccountForm(username: Option[String], email: String, password: Option[String], confirmPassword: Option[String], bio: Option[String], image: Option[String])

case class RegisterUserForm(username: String, email: String, password: String)

case class UpdateUserForm(email: String, bio: Option[String], image: Option[String])

case class LoginForm(email: String, password: String)

case class UpdatePasswordForm(email: String, password: String)

@JsonCodec case class ParsedJWTToken(exp: Long, iat: Long, sub: String)


object AccountForm {

  import cats.implicits._
  import com.realworld.app.errorhandler.ErrorManagement._
  import scala.language.implicitConversions

  val validateEmail = notEmptyString("Email is invalid")(_)
  val validateUsername = notEmptyString("Username is Invalid")(_)
  val validatePassword = notEmptyString("Password is Invalid")(_)
  val validateCPassword = notEmptyString("Confirm-Password is Invalid")(_)
  val validatePasswordMatch = stringMatch("Passsword/Confirm-password mismatch")(_)

  def registerUserForm(accountForm: AccountForm): Validated[RegisterUserForm] =
    (
      validateUsername(accountForm.username),
      validateEmail(accountForm.email),
      validatePassword(accountForm.password),
      validateCPassword(accountForm.confirmPassword),
      validatePasswordMatch((accountForm.password, accountForm.confirmPassword)))
      .mapN((a, b, c, _, _) =>
        RegisterUserForm(a, b, c))

  def updateUserForm(accountForm: AccountForm): Validated[UpdateUserForm] =
    validateEmail(accountForm.email).map(a => UpdateUserForm(a, accountForm.bio, accountForm.image))

  def loginForm(accountForm: AccountForm): Validated[LoginForm] =
    (validateEmail(accountForm.email), validatePassword(accountForm.password)).mapN(LoginForm)

  def updatePassword(accountForm: AccountForm): Validated[UpdatePasswordForm] =
    (
      validateEmail(accountForm.email),
      validatePassword(accountForm.password),
      validateCPassword(accountForm.confirmPassword),
      validatePasswordMatch(accountForm.password, accountForm.confirmPassword))
      .mapN((a, b, _, _) =>
        UpdatePasswordForm(a, b))
}

