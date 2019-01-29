package com.realworld.accounts.model

import com.realworld.{AppError, ResponsePayLoad}

final case class AuthRepsonse(email: String, token: String,username : String, bio: Option[String], image: Option[String]) extends ResponsePayLoad

sealed trait AccountError extends AppError
final case class AuthenticationFailed(msg: String, code: Int) extends AccountError
final case object UserDoesNotExist extends AccountError