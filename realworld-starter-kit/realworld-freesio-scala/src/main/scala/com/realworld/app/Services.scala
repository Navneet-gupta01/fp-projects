package com.realworld.app

import com.realworld.accounts.services.{AccountServices, AuthServices}
import com.realworld.profile.ProfileServices
import freestyle.tagless.module

@module
trait Services[F[_]] {
  val accountServices: AccountServices[F]
  val authServices: AuthServices[F]
  val profileServices: ProfileServices[F]
}
