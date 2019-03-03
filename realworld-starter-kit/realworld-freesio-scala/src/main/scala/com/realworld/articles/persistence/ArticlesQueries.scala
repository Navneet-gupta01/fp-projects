package com.realworld.articles.persistence

import doobie.implicits._
import doobie.util.update.Update0

object ArticlesQueries {
//  def insertArticle()


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
