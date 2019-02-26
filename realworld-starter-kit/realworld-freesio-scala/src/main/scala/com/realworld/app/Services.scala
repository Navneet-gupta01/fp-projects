package com.realworld.app

import com.realworld.accounts.services.{AccountServices, AuthServices}
import freestyle.tagless.module

@module
trait Services[F[_]] {
  val accountServices: AccountServices[F]
  val authServices: AuthServices[F]
}
