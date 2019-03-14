package com.realworld.app

import cats.effect.Effect
import cats.implicits._
import org.http4s.implicits._
import com.realworld.accounts.AccountApi
import com.realworld.app.reset.AppDBResetApis
import com.realworld.app.tags.TagsApis
import com.realworld.articles.ArticlesApi
import com.realworld.comments.CommentsApi
import com.realworld.favorites.FavoriteApi
import com.realworld.profile.ProfileApi
import com.realworld.test.api.TestApi

class AppApis[F[_] : Effect](implicit testApi: TestApi[F],
                             accountApi: AccountApi[F],
                             profileApi: ProfileApi[F],
                             appApi: AppDBResetApis[F],
                             articlesApi: ArticlesApi[F],
                             commentsApi: CommentsApi[F],
                             tagsApis: TagsApis[F],
                             favoritesApis: FavoriteApi[F]) {

  val routes = testApi.routes <+>
    profileApi.routes <+>
    accountApi.routes <+>
    appApi.endPoints <+>
    articlesApi.routes <+>
    commentsApi.routes <+>
    tagsApis.routes <+>
    favoritesApis.routes
}

object AppApis {
  implicit def instance[F[_] : Effect](implicit
                                       testApi: TestApi[F],
                                       accountApi: AccountApi[F],
                                       profileApi: ProfileApi[F],
                                       appApi: AppDBResetApis[F],
                                       articlesApi: ArticlesApi[F],
                                       commentsApi: CommentsApi[F],
                                       tagsApis: TagsApis[F],
                                       favoritesApis: FavoriteApi[F]): AppApis[F] = new AppApis[F]
}