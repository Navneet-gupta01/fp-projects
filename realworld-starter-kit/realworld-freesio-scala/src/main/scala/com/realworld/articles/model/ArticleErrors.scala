package com.realworld.articles.model

import cats.data.NonEmptyList
import com.realworld.accounts.model.AccountDomainErrors

sealed trait ArticleDomainErrors extends Exception {
  def errorMsg: NonEmptyList[String]
}

case class InvalidInputParams(msg: NonEmptyList[String]) extends ArticleDomainErrors {
  override def errorMsg = msg
}

case class ArticleDoesNotExist(slug: String) extends ArticleDomainErrors {
  override def errorMsg = NonEmptyList.one(s"No such article with ${slug} authored by you.")
}

case class ArticleTitleAlreadyExists(msg: NonEmptyList[String]) extends ArticleDomainErrors {
  override def errorMsg: NonEmptyList[String] = msg
}

