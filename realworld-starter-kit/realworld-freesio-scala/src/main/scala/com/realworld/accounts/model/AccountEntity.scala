package com.realworld.accounts.model

import com.realworld.PayLoad

final case class AccountEntity(
                          email: String,
                          password: String,
                          salt: String,
                          username: String,
                          bio: Option[String],
                          image: Option[String]) extends PayLoad