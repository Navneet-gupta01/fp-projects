package com.realworld.accounts.model

import com.realworld.{AppError, ResponsePayLoad}
import sun.security.util.Password

final case class AuthRepsonse(email: String, token: String,username : String, bio: String = "", image: String = "", id: Long) extends ResponsePayLoad

sealed trait AccountError extends AppError
final case class AuthenticationFailed(msg: String, code: Int) extends AccountError
final case object UserDoesNotExist extends AccountError

//final case class LoginForm(email: String, password: String)
//final case class AccountForm(username: Option[String] , email: String, bio: Option[String], image: Option[String], password: Option[String], confirmPassword: Option[String], id: Option[Long])

sealed trait AccountForm {
  def email: String
}
case class RegisterUserForm(username: String, email: String, password: String, confirmPassword: String) extends AccountForm
case class UpdateUserForm(email: String, bio: Option[String], image: Option[String]) extends AccountForm
case class LoginForm(email: String, password: String) extends AccountForm
case class UpdatePassword(email: String, password: String, confirmPassword: String, otp: Option[Int])



import com.realworld.app.errorhandler.ErrorManagement._

object AccountForm {
  val validateEmail = notEmptyString("Email is invalid")(_)
  val validateUsername = notEmptyString("Username is Invalid")(_)
  val validatePassword = notEmptyString("Password is Invalid")(_)
  val validateCPassword = notEmptyString("Confirm-Password is Invalid")(_)
  val validatePasswordMatch = stringMatch("Passsword/Confirm-password mismatch")

  def registerUserForm()
}