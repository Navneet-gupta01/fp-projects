package com.realworld.accounts.persistence

import java.util.Date

import com.realworld.accounts.model.AccountEntity
import doobie.implicits._
import doobie.util.query.Query0
import doobie.util.update.Update0

object AccountQueries {

  def insertQuery(account: AccountEntity): Update0 =
    sql"""
          INSERT INTO accounts (username, email, password, created_at, updated_at)
          VALUES (${account.username}, ${account.email}, ${account.password}, ${new Date}, ${new Date})
       """.update

  def updateQuery(account: AccountEntity): Update0 =
    sql"""
          UPDATE accounts set bio = ${account.bio} , image = ${account.image}
          WHERE id = ${account.id}
       """.update

  def updatePasswordQuery(account: AccountEntity): Update0 =
    sql"""
          UPDATE accounts set password = ${account.password}
          WHERE id = ${account.id}
       """.update

  def getByEmailQuery(email: String): Query0[AccountEntity] =
    sql"SELECT email, password, username, bio, image, id from accounts where email = $email".query[AccountEntity]

  def getByUsernameQuery(username: String): Query0[AccountEntity] =
    sql"SELECT email, password, username, bio, image, id from accounts where username = $username".query[AccountEntity]

  def getByIdQuery(id: Long): Query0[AccountEntity] =
    sql"SELECT email, password, username, bio, image, id from accounts where id = $id".query[AccountEntity]

  def getQuery(id: Option[Long] = None, username: Option[String] = None, email: Option[String]= None): Query0[AccountEntity] =
    sql"SELECT email, password, username, bio, image, id from accounts where id = $id or email = $email or username = $username".query[AccountEntity]

  val listQuery: Query0[AccountEntity] =
    sql"""select email, password, username, bio, image, id from accounts order by id ASC""".query[AccountEntity]

  def deleteQuery(id: Long): Update0 =
    sql"DELETE from accounts where id = $id".update

  val createQuery: Update0 =
    sql"""
          CREATE TABLE accounts (
            id SERIAL NOT NULL PRIMARY KEY,
            username VARCHAR(255) NOT NULL,
            email VARCHAR(255) NOT NULL,
            password VARCHAR(255) NOT NULL,
            bio VARCHAR(1024),
            image VARCHAR(255),
            created_at timestamp NOT NULL,
            updated_at timestamp NOT NULL,
            UNIQUE (email),
            UNIQUE (username)
         )
       """.update

  val dropQuery: Update0 =
    sql"""DROP TABLE IF EXISTS accounts""".update

}
