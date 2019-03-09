package com.realworld.articles.persistence

import java.util.Date

import com.realworld.articles.model.ArticleEntity
import doobie.implicits._
import doobie.util.query.Query0
import doobie.util.update.Update0

object ArticlesQueries {
  def insertQuery(articleEntity: ArticleEntity, author_id: Long): Update0 =
    sql"""
          INSERT INTO articles (slug, description, title, body, created_at, updated_at, author_id)
          VALUES (${articleEntity.slug},${articleEntity.description} ,${articleEntity.title},${articleEntity.body},${new Date()},${new Date()}, ${author_id})
       """.update

  def getOwnedyArticleQuery(slug: String, author_id: Long) : Query0[ArticleEntity] =
    sql"""SELECT ar.slug,ar.title,ar.description,ar.body,ar.id from articles where slug = ${slug} and author_id= ${author_id}""".query[ArticleEntity]

  def getArticleBySlugQuery(slug: String): Query0[ArticleEntity] =
    sql"""SELECT ar.slug,ar.title,ar.description,ar.body,ar.id from articles where slug = ${slug}""".query[ArticleEntity]

  def getQuery(slug: String, user_id: Long): Query0[(ArticleEntity,Date, Date,String, Option[String], Option[String], Option[Long])] =
    sql"""SELECT ar.slug,ar.title,ar.description,ar.body,ar.id,ar.created_at, ar.updated_at, ac.username, ac.bio, ac.image, u.followee_id  from articles ar
          INNER JOIN accounts ac on ac.id = ar.author_id
          LEFT JOIN users_followers u on ac.id = u.followee_id and u.follower_id=${user_id} where ar.slug = ${slug}""".query[(ArticleEntity,Date, Date,String, Option[String], Option[String], Option[Long])]

  def getByIdQuery(id: Long, user_id: Long): Query0[(ArticleEntity,Date, Date,String, Option[String], Option[String], Option[Long])] =
    sql"""SELECT ar.slug,ar.title,ar.description,ar.body,ar.id,ar.created_at, ar.updated_at, ac.username, ac.bio, ac.image, u.followee_id  from articles ar
          INNER JOIN accounts ac on ac.id = ar.author_id
          LEFT JOIN users_followers u on ac.id = u.followee_id and u.follower_id=${user_id} where ar.id = ${id}""".query[(ArticleEntity,Date, Date,String, Option[String], Option[String], Option[Long])]

  def getRecentQuery(user_id: Long, limit: Long, offset: Long): Query0[(ArticleEntity,Date, Date,String, Option[String], Option[String], Option[Long])] =
    sql"""SELECT ar.slug,ar.title,ar.description,ar.body,ar.id,ar.created_at, ar.updated_at, ac.username, ac.bio, ac.image, u.followee_id  from articles ar
          INNER JOIN accounts ac on ac.id = ar.author_id
          LEFT JOIN users_followers u on ac.id = u.followee_id and u.follower_id=${user_id} order by ar.created_at desc limit ${limit} offset ${offset}""".query[(ArticleEntity,Date, Date,String, Option[String], Option[String], Option[Long])]

  def getFollowedArticleQuery(limit: Long, offset: Long, userId: Long): Query0[(ArticleEntity, Date, Date, String, Option[String], Option[String], Option[Long])] =
    sql"""
          SELECT ar.slug,ar.title,ar.description,ar.body,ar.id,ar.created_at, ar.updated_at, ac.username, ac.bio, ac.image, u.folloee_id from accounts ac
          INNER join users_followers u on u.followee_id = ac.id and u.follower_id = ${userId}
          INNER JOIN articles ar on ar.author_id = ac.id order by ar.created_at limit $limit and offset $offset
       """.query[(ArticleEntity, Date, Date, String, Option[String], Option[String], Option[Long])]

  def updateQuery(articleEntity: ArticleEntity): Update0 =
    sql"""
          UPDATE articles set title = ${articleEntity.title}, description = ${articleEntity.description}, body = ${articleEntity.body},
          updated_at = ${new Date()} where slug = ${articleEntity.slug}
       """.update

  def deleteQuery(slug: String, user_id: Long): Update0 =
    sql"""DELETE from articles where slug = ${slug} and author_id = ${user_id}""".update

  val createQuery: Update0 =
    sql"""
         CREATE TABLE articles (
          id SERIAL NOT NULL PRIMARY KEY,
          slug varchar(255) NOT NULL,
          description TEXT,
          title varchar(255) NOT NULL,
          body TEXT,
          created_at timestamp NOT NULL,
          updated_at timestamp NOT NULL,
          UNIQUE(slug),
          author_id INTEGER REFERENCES accounts(id) ON DELETE CASCADE
         )
       """.update


  val dropQuery: Update0 =
    sql"""DROP TABLE IF EXISTS articles""".update

}
