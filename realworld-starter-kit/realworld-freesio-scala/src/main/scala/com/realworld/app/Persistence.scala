package com.realworld.app

import com.realworld.accounts.persistence.AccountRepository
import freestyle.tagless.module

@module
trait Persistence[F[_]] {
  val accountRepository: AccountRepository[F]
}
