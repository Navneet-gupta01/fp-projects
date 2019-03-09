package com.realworld.comments.persistence

import java.util.Date

import com.realworld.comments.model.CommentsEntity
import com.realworld.profile.model.ProfileEntity
import doobie.implicits._
import doobie.util.query.Query0
import doobie.util.update.Update0

object CommentsQueries {
  def insertQuery(commentsEntity: CommentsEntity, author_id: Long): Update0 =
    sql"""INSERT INTO comments (body, created_at, updated_at, author_id, article_id) VALUES
          (${commentsEntity.body}, ${new Date()}, ${new Date()}, ${author_id}, ${commentsEntity.article_id})
       """.update

  def getQuery(comment_id: Long): Query0[(CommentsEntity, ProfileEntity)] =
    sql"""SELECT c.body, c.created_at, c.updated_at, c.article_id, c.id, u.username, u.bio, u.image from comments c
          INNER JOIN accounts u on u.id = c.author_id and c.id = ${comment_id}
       """.query[(CommentsEntity, ProfileEntity)]

  def listQuery(article_id:Long, user_id: Long):Query0[(CommentsEntity, ProfileEntity)] =
    sql"""SELECT c.body, c.created_at, c.updated_at, c.article_id, c.id, u.username, u.bio, u.image, uf. followee_id from comments c
          INNER JOIN accounts u on c.author_id = u.id and c.article_id = ${article_id}
          LEFT JOIN users_followers uf on uf.follower_id = ${user_id} and uf.followee_id = c.author_id
       """.query[(CommentsEntity, ProfileEntity)]

  def deleteQuery(comment_id: Long, author_id: Long, article_id: Long): Update0 =
    sql"""DELETE from comments where id = ${comment_id} and author_id = $author_id and article_id= $article_id""".update

  val createQuery: Update0 =
    sql"""
         CREATE TABLE comments (
            id SERIAL NOT NULL PRIMARY KEY,
            body TEXT NOT NULL,
            created_at timestamp NOT NULL,
            updated_at timestamp NOT NULL,
            author_id INTEGER REFERENCES accounts(id) ON DELETE CASCADE,
            article_id INTEGER REFERENCES articles(id) ON DELETE CASCADE
         )
       """.update

  val dropQuery: Update0 =
    sql"""DROP TABLE IF EXISTS comments""".update

}
