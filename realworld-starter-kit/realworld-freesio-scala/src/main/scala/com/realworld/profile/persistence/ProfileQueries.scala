package com.realworld.profile.persistence

import com.realworld.accounts.model.AccountEntity
import com.realworld.profile.model.ProfileEntity
import doobie._
import doobie.implicits._
import doobie.util.query.Query0
import doobie.util.update.Update0

object ProfileQueries {

  def getQuery(user_id: Long, followee_username: String): Query0[(Option[Long], String, Option[String], Option[String])] =
    sql"SELECT u.follower_id, a.username, a.bio, a.image from accounts a left join users_followers u on a.id = u.followee_id and u.follower_id=${user_id} where a.username = ${followee_username}".query[(Option[Long], String, Option[String], Option[String])]


  def insertQuery(follower_id: Long, followee_id: Long): Update0 =
    sql"""INSERT INTO users_followers (follower_id,followee_id) VALUES (${follower_id}, ${followee_id})""".update

  def deleteQuery(follower_id: Long, followee_id: Long): Update0 =
    sql"""DELETE from users_followers where follower_id = ${follower_id} and followee_id= ${followee_id}""".update

  val createQuery: Update0 =
    sql"""
          CREATE TABLE users_followers (
            follower_id INTEGER REFERENCES accounts(id) ON DELETE CASCADE,
            followee_id INTEGER REFERENCES accounts(id) ON DELETE CASCADE,
            CONSTRAINT users_followers_pkey PRIMARY KEY (follower_id, followee_id)
         )
       """.update

  val dropQuery: Update0 =
    sql"""DROP TABLE IF EXISTS users_followers""".update


}
