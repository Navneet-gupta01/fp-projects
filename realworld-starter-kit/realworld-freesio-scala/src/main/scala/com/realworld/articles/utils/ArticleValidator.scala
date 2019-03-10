package com.realworld.articles.utils

import com.realworld.articles.model.ArticleEntity
import cats.implicits._
import com.realworld.app.errorhandler.ErrorManagement._

object ArticleValidator {
  def articleTitleAlreadyTaken(articleEntity: Option[ArticleEntity]): Validated[Boolean] =
    articleEntity.fold(true.validNel[String])(ar => s"Article already exists with same title : ${ar.title}. Please choose another title.".invalidNel)
}
