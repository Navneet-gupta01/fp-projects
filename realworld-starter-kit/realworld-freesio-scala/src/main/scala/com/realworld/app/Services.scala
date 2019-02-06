package com.realworld.app

import com.realworld.accounts.services.AccountServices
import freestyle.tagless.module

@module
trait Services[F[_]] {
  val accountServices: AccountServices[F]
}
