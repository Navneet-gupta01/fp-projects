package com.realworld.accounts.model

import com.realworld.AppError

sealed trait AccountDomainErrors extends AppError {
  def errorMsg: String
}
final case class InvalidInput(field: String, msg: String) extends AccountDomainErrors {
  override def errorMsg: String = s"Invalid Input Field: $field, $field: $msg"
}
final case class InvalidInputParams(errorMsg: String) extends AccountDomainErrors

final case class UsernameAlreadyExists(username: String) extends AccountDomainErrors {
  override def errorMsg: String = s"$username already taken. Please try again."
}

final case object PasswordConfirmPasswordMismatch extends AccountDomainErrors {
  override def errorMsg: String = "Password/Confirm Password mismatch. Please Enter carefully."
}
final case class EmailAlreadyExists(email: String) extends AccountDomainErrors {
  override def errorMsg: String = s"$email already taken. Please try again."
}

final case class AccountDoesNotExist(email: String) extends AccountDomainErrors {
  override def errorMsg: String = s"No account associated with $email"
}