package com.realworld.accounts.persistence

import com.realworld.accounts.model.AccountEntity
import doobie.util.update.Update0
import doobie.implicits._

object AccountQueries {

  def insertAccount(account: AccountEntity): Update0 =
    sql"""
         INSERT INTO accounts (username, email, password, created_at, updated_at)
         VALUES (${account.username}, ${account.email}, ${account.password}, ${new Date()})


       """.update


  val createQuery: Update0 =
    sql"""
         CREATE TABLE accounts (
           id INT(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
           username VARCHAR(255) NOT NULL,
           email VARCHAR(255) NOT NULL,
           password VARCHAR(255) NOT NULL,
           bio VARCHAR(1024),
           image VARCHAR(255),
           created_at DATETIME NOT NULL,
           updated_at DATETIME NOT NULL,
           CONSTRAINT user_email_unique UNIQUE (email),
           CONSTRAINT user_username_unique UNIQUE (username)
         )
       """.update

  val dropQuery: Update0 =
    sql"""DROP TABLE accounts IF EXISTS""".update

}
