package com.realworld.app

import com.realworld.accounts.persistence.AccountRepository
import com.realworld.profile.persistence.ProfileRepository
import freestyle.tagless.module

@module
trait Persistence[F[_]] {
  val accountRepository: AccountRepository[F]
  val profileRepository: ProfileRepository[F]
}
