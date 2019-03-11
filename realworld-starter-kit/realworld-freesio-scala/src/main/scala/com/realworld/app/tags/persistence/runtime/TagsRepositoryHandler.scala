package com.realworld.app.tags.persistence.runtime

import cats.Monad
import cats.implicits._
import com.realworld.app.tags.persistence.TagsRepository
import doobie.util.transactor.Transactor
import doobie.implicits._

class TagsRepositoryHandler[F[_]: Monad](implicit T: Transactor[F]) extends TagsRepository.Handler[F] {

  import com.realworld.articles.persistence.TagsQueries._

  override def getAllTags: F[List[String]] =
    listTags
      .to[List]
      .transact(T)
}
