package com.realworld.profile.model

import cats.data.NonEmptyList

sealed trait ProfileDomainErrors extends Exception {
  def errorMsg: NonEmptyList[String]
}

case class ProfileNotFound(username: String) extends ProfileDomainErrors {
  override def errorMsg: NonEmptyList[String] = NonEmptyList.one(s"User Profile not found for username: ${username}")
}

case class UnknownException(msg: String) extends ProfileDomainErrors {
  override def errorMsg: NonEmptyList[String] = NonEmptyList.one(msg)
}