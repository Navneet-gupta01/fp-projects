package com.realworld.accounts.model

import com.realworld.{AppError, ResponsePayLoad}

final case class AuthRepsonse(email: String, token: String,username : String, bio: String = "", image: String = "", id: Long) extends ResponsePayLoad

sealed trait AccountError extends AppError
final case class AuthenticationFailed(msg: String, code: Int) extends AccountError
final case object UserDoesNotExist extends AccountError

final case class LoginForm(email: String, password: String)
final case class AccountForm(email: String, bio: String, image: String, password: Option[String], confirmPassword: Option[String], id: Option[Long])