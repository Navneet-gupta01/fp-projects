package com.realworld.accounts.model

import com.realworld.AppError

final case class AuthRepsonse(email: String, token: String,username : String, bio: Option[String], image: Option[String])

sealed trait AccountError extends AppError
final case class AuthenticationFailed(msg: String, code: Int)