package com.realworld.app

import cats.effect.Effect
import cats.implicits._
import org.http4s.implicits._
import com.realworld.accounts.AccountApi
import com.realworld.profile.ProfileApi
import com.realworld.test.api.TestApi

class AppApis[F[_]: Effect](implicit testApi: TestApi[F], accountApi: AccountApi[F], profileApi: ProfileApi[F]) {
  val routes = testApi.routes <+> profileApi.routes <+> accountApi.routes
}

object AppApis {
  implicit def instance[F[_] : Effect](implicit testApi: TestApi[F], accountApi: AccountApi[F], profileApi: ProfileApi[F]): AppApis[F] = new AppApis[F]
}