package com.realworld.articles.persistence

import com.realworld.articles.model.Tags
import doobie.implicits._
import doobie.util.query.Query0
import doobie.util.update.Update0

object TagsQueries {

  def insertTagsQuery(tag: Tags): Update0 =
    sql"""INSERT INTO tags (name) values (${tag.name})""".update

  def getTagsQuery(article_id: Long) : Query0[String] =
    sql"""SELECT t.name from article_tags at left join tags t on at.tag_id = t.id and at.article_id = ${article_id}""".query[String]


  def deleteTagsQuery(tag_id: Long): Update0 =
    sql"""DELETE from tags where id = ${tag_id}""".update

  val createTagsQuery: Update0 =
    sql"""
         CREATE TABLE tags (
            id SERIAL NOT NULL PRIMARY KEY,
            name varchar(255) NOT NULL,
            UNIQUE(name)
         )
       """.update

  val dropTagsQuery: Update0 =
    sql"""
         DROP TABLE IF EXISTS tags
       """.update

}
