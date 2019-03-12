package com.realworld.favorites.model

import cats.data.NonEmptyList

sealed trait FavoritesDomainErrors extends Exception {
  def errorMsg: NonEmptyList[String]
}

case class ArticleDoesNotExist(slug: String) extends FavoritesDomainErrors {
  override def errorMsg = NonEmptyList.one(s"No such article with slug ${slug} exists.")
}
