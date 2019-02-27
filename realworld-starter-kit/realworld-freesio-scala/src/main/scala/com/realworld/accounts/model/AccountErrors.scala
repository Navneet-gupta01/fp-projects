package com.realworld.accounts.model

import cats.data.NonEmptyList
import com.realworld.AppError
import cats.implicits._
import cats.syntax.list._

sealed trait AccountDomainErrors extends Exception {
  def errorMsg: NonEmptyList[String]
}
case class InvalidInput(field: String, msg: String) extends AccountDomainErrors {
  override def errorMsg = NonEmptyList.one(s"Invalid Input Field: $field, $field: $msg")
}

case class InvalidInputParams(msg: NonEmptyList[String]) extends AccountDomainErrors {
  override def errorMsg = msg
}

case class UsernameAlreadyExists(username: String) extends AccountDomainErrors {
  override def errorMsg = NonEmptyList.one(s"$username already taken. Please try again.")
}

case object PasswordConfirmPasswordMismatch extends AccountDomainErrors {
  override def errorMsg = NonEmptyList.one("Password/Confirm Password mismatch. Please Enter carefully.")
}
case class EmailAlreadyExists(email: String) extends AccountDomainErrors {
  override def errorMsg = NonEmptyList.one(s"$email already taken. Please try again.")
}

case class AccountDoesNotExist(email: String) extends AccountDomainErrors {
  override def errorMsg = NonEmptyList.one(s"No account associated with $email")
}

case class InvalidCredentials(errorMsg: NonEmptyList[String]) extends AccountDomainErrors