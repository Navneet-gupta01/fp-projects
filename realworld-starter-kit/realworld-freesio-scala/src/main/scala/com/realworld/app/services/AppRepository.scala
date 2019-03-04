package com.realworld.app.services

import freestyle.tagless.tagless

@tagless(true)
trait AppRepository[F[_]] {
  def reset: F[Int]
  def init: F[Int]
  def drop: F[Int]
}
