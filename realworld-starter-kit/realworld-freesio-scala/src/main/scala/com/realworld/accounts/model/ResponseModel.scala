package com.realworld.accounts.model

import cats.data.NonEmptyList

final case class LoginResp(user: Option[AuthRepsonse])
final case class FormReq(user: AccountForm)
final case class Body(body: List[String])
final case class ErrorResp(errors: Body )


object ErrorResp {
  def apply(errorMsgs: NonEmptyList[String]): ErrorResp = ErrorResp(Body(errorMsgs.toList))
}