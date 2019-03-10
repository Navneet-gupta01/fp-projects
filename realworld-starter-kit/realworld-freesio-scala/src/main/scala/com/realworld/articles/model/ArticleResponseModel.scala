package com.realworld.articles.model


final case class ArticleResp(article: Option[ArticleResponse])
final case class ArticlesResp(articles: List[ArticleResponse] , articlesCount: Int = 0)

final case class ArticleReq(article: ArticleForm)
