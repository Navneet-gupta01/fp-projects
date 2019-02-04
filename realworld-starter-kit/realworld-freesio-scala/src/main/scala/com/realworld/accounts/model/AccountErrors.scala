package com.realworld.accounts.model

sealed trait AccountDomainErrors {
  def errorMsg: String
}
final case class InvalidInput(field: String, msg: String) extends AccountDomainErrors {
  override def errorMsg: String = s"Invalid Input Field: $field, $field: $msg"
}
final case class UsernameAlreadyExists(username: String) extends AccountDomainErrors {
  override def errorMsg: String = s"$username already taken. Please try again."
}
final case object PasswordConfirmPasswordMismatch extends AccountDomainErrors {
  override def errorMsg: String = "Password/Confirm Password mismatch. Please Enter carefully."
}
final case class EmailAlreadyExists(email: String) extends AccountDomainErrors {
  override def errorMsg: String = s"$email already taken. Please try again."
}
