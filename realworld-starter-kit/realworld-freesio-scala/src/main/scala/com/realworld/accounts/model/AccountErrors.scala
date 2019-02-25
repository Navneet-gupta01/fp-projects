package com.realworld.accounts.model

import com.realworld.AppError

sealed trait AccountDomainErrors extends Exception {
  def errorMsg: String
}
case class InvalidInput(field: String, msg: String) extends AccountDomainErrors {
  override def errorMsg: String = s"Invalid Input Field: $field, $field: $msg"
}

case class InvalidInputParams(msg: String) extends AccountDomainErrors {
  override def errorMsg: String = msg
}

case class UsernameAlreadyExists(username: String) extends AccountDomainErrors {
  override def errorMsg: String = s"$username already taken. Please try again."
}

case object PasswordConfirmPasswordMismatch extends AccountDomainErrors {
  override def errorMsg: String = "Password/Confirm Password mismatch. Please Enter carefully."
}
case class EmailAlreadyExists(email: String) extends AccountDomainErrors {
  override def errorMsg: String = s"$email already taken. Please try again."
}

case class AccountDoesNotExist(email: String) extends AccountDomainErrors {
  override def errorMsg: String = s"No account associated with $email"
}