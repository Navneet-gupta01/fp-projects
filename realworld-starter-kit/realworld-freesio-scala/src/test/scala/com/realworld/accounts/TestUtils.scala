package com.realworld.accounts

import cats.data.StateT
import com.realworld.accounts.model.{AccountDomainErrors, AccountEntity}

object TestUtils {
  case class AccountTestState(accounts: List[AccountEntity])

  type OrError[A] = Either[AccountDomainErrors, A]
  type AccountTest[A] = StateT[OrError, AccountTestState, A]
}
