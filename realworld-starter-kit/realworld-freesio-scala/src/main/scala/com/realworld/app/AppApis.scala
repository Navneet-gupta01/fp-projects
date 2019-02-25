package com.realworld.app

import cats.effect.Effect
import cats.implicits._
import org.http4s.implicits._
import com.realworld.accounts.AccountApi
import com.realworld.test.api.TestApi

class AppApis[F[_]: Effect](implicit testApi: TestApi[F], accountApi: AccountApi[F]) {
  val routes = testApi.routes <+> accountApi.routes
}

object AppApis {
  implicit def instance[F[_] : Effect](implicit testApi: TestApi[F], accountApi: AccountApi[F]): AppApis[F] = new AppApis[F]
}