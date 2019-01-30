package com.realworld.accounts.model

final case class AccountEntity(
                          email: String,
                          password: String,
                          username: String,
                          bio: Option[String]=None,
                          image: Option[String]=None,
                          id: Option[Long]=None)