package com.realworld.articles.model

final case class ArticleEntity(slug: String, title: String, description: String, body: String, taglist: List[String] = List())
