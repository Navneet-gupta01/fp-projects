package com.realworld.comments.model

import cats.data.NonEmptyList

sealed trait CommentsDomainErrors extends Exception {
  def errorMsg: NonEmptyList[String]
}

case class InvalidInputParams(msg: NonEmptyList[String]) extends CommentsDomainErrors {
  override def errorMsg = msg
}

case class ArticleDoesNotExist(slug: String) extends CommentsDomainErrors {
  override def errorMsg = NonEmptyList.one(s"No such article with ${slug}.")
}
