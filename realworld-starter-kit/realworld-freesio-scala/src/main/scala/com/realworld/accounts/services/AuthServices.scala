package com.realworld.accounts.services

import cats.Monad
import freestyle.tagless.logging.LoggingM
import freestyle.tagless.module

@module
trait AuthServices[F[_]] {
  implicit val M : Monad[F]
  implicit val L: LoggingM[F]

}
