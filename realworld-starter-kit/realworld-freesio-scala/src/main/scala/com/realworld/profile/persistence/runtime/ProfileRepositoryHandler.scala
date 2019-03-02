package com.realworld.profile.persistence.runtime

import cats.Monad
import com.realworld.accounts.persistence.AccountQueries
import com.realworld.profile.model.ProfileEntity
import com.realworld.profile.persistence.ProfileRepository
import doobie.implicits._
import doobie.util.transactor.Transactor

class ProfileRepositoryHandler[F[_] : Monad](implicit T: Transactor[F])
  extends ProfileRepository.Handler[F] {

  import com.realworld.profile.persistence.ProfileQueries._

  override def getProfile(
                           user_id: Long,
                           username_to_follow: String): F[Option[ProfileEntity]] =
    getQuery(user_id, username_to_follow)
      .map(a => if (a._1.isDefined) ProfileEntity(a._2, a._3, a._4, true) else ProfileEntity(a._2, a._3, a._4))
      .option
      .transact(T)

  override def follow(username_to_follow: String, follower_id: Long): F[Int] =
    (for {
      accountEntity <- AccountQueries
        .getByUsernameQuery(username_to_follow)
        .unique
      followed <- insertQuery(follower_id, accountEntity.id.get).run
    } yield followed)
      .transact(T)

  override def unfollow(username_to_unfollow: String,
                        follower_id: Long): F[Int] =
    (for {
      accountEntity <- AccountQueries
        .getByUsernameQuery(username_to_unfollow)
        .unique
      unfollowed <- deleteQuery(follower_id, accountEntity.id.get).run
    } yield unfollowed)
      .transact(T)

  override def init: F[Int] =
    dropQuery.run
      .flatMap(
        drops =>
          createQuery.run
            .map(_ + drops))
      .transact(T)

  override def drop: F[Int] =
    dropQuery.run
      .transact(T)

  override def create: F[Int] =
    createQuery.run
      .transact(T)
}
