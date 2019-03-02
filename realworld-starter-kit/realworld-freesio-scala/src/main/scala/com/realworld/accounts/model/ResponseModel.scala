package com.realworld.accounts.model

final case class LoginResp(user: Option[AuthRepsonse])

final case class FormReq(user: AccountForm)
