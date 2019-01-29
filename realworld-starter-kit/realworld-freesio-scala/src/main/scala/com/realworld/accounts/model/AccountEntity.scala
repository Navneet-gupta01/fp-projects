package com.realworld.accounts.model

import com.realworld.EntityPayLoad

final case class AccountEntity(
                          email: String,
                          password: String,
                          username: String,
                          bio: Option[String],
                          image: Option[String]) extends EntityPayLoad