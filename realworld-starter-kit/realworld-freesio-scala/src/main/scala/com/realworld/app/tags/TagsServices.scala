package com.realworld.app.tags

import cats.Monad
import cats.implicits._
import com.realworld.app.tags.persistence.TagsRepository
import freestyle.tagless.logging.LoggingM
import freestyle.tagless.module

@module
trait TagsServices[F[_]] {
  implicit val M: Monad[F]
  implicit val L: LoggingM[F]

  val repo: TagsRepository[F]

  def listTags: F[List[String]] =
    for {
      _ <- L.info("Trying to list the existing tags")
      tagsList <- repo.getAllTags
      _ <- L.info("Tried listing all the existing tags")
    } yield tagsList
}
