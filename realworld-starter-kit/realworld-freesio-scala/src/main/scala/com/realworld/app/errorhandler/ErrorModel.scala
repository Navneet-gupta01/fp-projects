package com.realworld.app.errorhandler

import cats.data.NonEmptyList

final case class Body(body: List[String])
final case class ErrorResp(errors: Body )


object ErrorResp {
  def apply(errorMsgs: NonEmptyList[String]): ErrorResp = ErrorResp(Body(errorMsgs.toList))
}
