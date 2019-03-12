package com.realworld.favorites.persistence

import doobie.util.update.Update0
import doobie.implicits._
import doobie.util.query.Query0


object FavoritesQueries {
  def favoriteArticleQuery(user_id: Long, article_id: Long): Update0 =
    sql"""INSERT INTO favorites_association (article_id, user_id) VALUES (${article_id}, ${user_id}) ON CONFLICT DO NOTHING""".update


  def unFavoriteArticleQuery(user_id: Long, article_id: Long): Update0 =
    sql"""DELETE FROM favorites_association where article_id = ${article_id} and user_id = ${user_id}""".update

  def getfavoritesQuery(article_id: Long): Query0[Long] =
    sql"""SELECT user_id from favorites_association where article_id = ${article_id}""".query[Long]


  val createQuery: Update0 =
    sql"""
          CREATE TABLE favorites_association (
            article_id INTEGER REFERENCES articles(id) ON DELETE CASCADE,
            user_id INTEGER REFERENCES accounts(id) ON DELETE CASCADE,
            CONSTRAINT favorites_association_pkey PRIMARY KEY (article_id, user_id)
          )
       """.update

  val dropQuery: Update0 =
    sql"""DROP TABLE IF EXISTS favorites_association""".update

}
