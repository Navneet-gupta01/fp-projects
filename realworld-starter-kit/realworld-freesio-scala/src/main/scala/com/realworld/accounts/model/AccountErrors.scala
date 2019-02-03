package com.realworld.accounts.model

sealed trait AccountErrors
final case class InvalidInput(field: String, msg: String) extends AccountErrors
final case class UsernameAlreadyExists(username: String) extends AccountErrors
final case class PasswordConfirmPasswordMismatch(msg: String) extends AccountErrors

