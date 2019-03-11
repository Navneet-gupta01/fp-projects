package com.realworld.app.tags.persistence

import freestyle.tagless.tagless

@tagless(true)
trait TagsRepository[F[_]] {
  def getAllTags: F[List[String]]
}
