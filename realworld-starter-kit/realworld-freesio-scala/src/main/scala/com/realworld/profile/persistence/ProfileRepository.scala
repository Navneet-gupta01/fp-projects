package com.realworld.profile.persistence

import com.realworld.profile.model.ProfileEntity
import freestyle.tagless.tagless

@tagless(true)
trait ProfileRepository[F[_]] {
  def getProfile(user_id: Long, followee_username: String): F[Option[ProfileEntity]]
  def follow(username_to_follow: String, follower_id: Long): F[Int]
  def unfollow(username_to_unfollow: String, follower_id: Long): F[Int]
  def init: F[Int]
  def drop: F[Int]
  def create: F[Int]
}
