package com.realworld.profile

import cats.Monad
import com.realworld.profile.model.{ProfileEntity, ProfileNotFound, UnknownException}
import com.realworld.profile.persistence.ProfileRepository
import freestyle.tagless.effects.error.ErrorM
import freestyle.tagless.logging.LoggingM
import freestyle.tagless.module
import cats.implicits._
import freestyle.tagless.effects._

@module
trait ProfileServices[F[_]] {
  implicit val M: Monad[F]

  implicit val L: LoggingM[F]

  val model = classOf[ProfileEntity].getSimpleName
  val error : ErrorM[F]

  val repo: ProfileRepository[F]


  def getProfile(username: String, user_id: Long) : F[Option[ProfileEntity]] =
    for {
      _ <- L.info(s"Trying to get User Profile for username: ${username}")
      profile <- repo.getProfile(user_id, username)
      _ <- error.either[ProfileEntity](profile.toRight(ProfileNotFound(username)))
      _ <- L.info(s"Tried Fetching Prfile for ${username} by ${user_id}")
    } yield profile


  def followUser(username_to_follow: String, user_id: Long): F[Option[ProfileEntity]] =
    for {
      _ <- L.info(s"Trying to follow User ${username_to_follow} by user : ${user_id}")
      profile <- repo.getProfile(user_id, username_to_follow)
      _ <- error.either[ProfileEntity](profile.toRight(ProfileNotFound(username_to_follow)))
      _ <- if(!profile.get.following) repo.follow(username_to_follow, user_id) else 1.pure[F]
    } yield profile.map(_.copy(following = true))


  def unFollowUser(username_to_unfollow: String, user_id: Long): F[Option[ProfileEntity]] =
    for {
      _ <- L.info(s"Trying to unFollowUser User ${username_to_unfollow} by user : ${user_id}")
      profile <- repo.getProfile(user_id, username_to_unfollow)
      _ <- error.either[ProfileEntity](profile.toRight(ProfileNotFound(username_to_unfollow)))
      unfollowed <- if(profile.get.following) repo.unfollow(username_to_unfollow, user_id) else 1.pure[F]
    } yield profile.map(_.copy(following = false))

  val reset: F[Int] =
    for {
      _ <- L.debug(s"Trying to reset the model: $model")
      resetedItems <- repo.init
      _ <- L.debug(s"Tried Resetting the model: $model")
    } yield resetedItems
}
