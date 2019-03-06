package com.realworld.app.services

import cats.Monad
import cats.implicits._
import freestyle.tagless.effects.error.ErrorM
import freestyle.tagless.logging.LoggingM
import freestyle.tagless.module

@module
trait AppServices[F[_]] {
  implicit val M: Monad[F]

  implicit val L: LoggingM[F]
  val error: ErrorM[F]

  val repo : AppRepository[F]

  def resetDB: F[Int] =
    for {
      _ <- L.debug(s"Trying to reset the Database tables")
      resetedItems <- repo.reset
      _ <- L.debug(s"Tried Resetting the Database tables")
    } yield resetedItems
}
