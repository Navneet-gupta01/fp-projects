package com.realworld.articles.persistence

import doobie.implicits._
import doobie.util.update.Update0

object ArticleTagsQueries {

  def insertATQuery(article_id: Long, tag_id: Long) : Update0 =
    sql"""INSERT INTO article_tags (article_id, tag_id) VALUES (${article_id}, ${tag_id})""".update

  def deleteATQuery(article_id: Long, tag_id: Long): Update0 =
    sql"""DELETE FROM article_tags where article_id = ${article_id} and tag_id = ${tag_id}""".update

  val createATQuery: Update0 =
    sql"""
         CREATE TABLE article_tags(
            article_id INTEGER REFERENCES articles(id) ON DELETE CASCADE,
            tag_id INTEGER REFERENCES tags(id) ON DELETE CASCADE,
            CONSTRAINT article_tags_pkey PRIMARY KEY (article_id, tag_id)
         )

       """.update

  val dropATQuery: Update0 =
    sql"""
         DROP TABLE IF EXISTS article_tags
       """.update





}
