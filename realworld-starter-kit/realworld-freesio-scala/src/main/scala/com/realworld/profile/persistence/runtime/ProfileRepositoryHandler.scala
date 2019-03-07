package com.realworld.profile.persistence.runtime

import cats.Monad
import com.realworld.accounts.persistence.AccountQueries
import com.realworld.profile.model.ProfileEntity
import com.realworld.profile.persistence.ProfileRepository
import doobie.free.connection.ConnectionIO
import doobie.implicits._
import cats.implicits._
import doobie.util.transactor.Transactor

class ProfileRepositoryHandler[F[_] : Monad](implicit T: Transactor[F])
  extends ProfileRepository.Handler[F] {

  import com.realworld.profile.persistence.ProfileQueries._

  override def getProfile(
                           user_id: Long,
                           username_to_follow: String): F[Option[ProfileEntity]] =
    getQuery(user_id, username_to_follow)
      .map(a => {
        val p = ProfileEntity(a._2, a._3, a._4, false)
        a._1.fold(p)(_ => p.copy(following = true))
      })
      .option
      .transact(T)

  override def follow(username_to_follow: String, follower_id: Long): F[Int] =

    (for {
      accountEntity <- AccountQueries
        .getByUsernameQuery(username_to_follow)
        .unique
      followed <- if(accountEntity.id.get =!= follower_id) insertQuery(follower_id, accountEntity.id.get).run
                  else  0.pure[ConnectionIO]
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
