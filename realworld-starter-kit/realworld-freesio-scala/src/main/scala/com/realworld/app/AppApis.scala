package com.realworld.app

import cats.effect.Effect
import com.realworld.test.api.TestApi

class AppApis[F[_]: Effect](implicit testApi: TestApi[F]) {
  val endPoints = testApi.endPoints
}

object AppApis {
  implicit def instance[F[_] : Effect](implicit testApi: TestApi[F]): AppApis[F] = new AppApis[F]
}