package com.realworld.app

import com.realworld.accounts.persistence.AccountRepository
import com.realworld.app.services.AppRepository
import com.realworld.app.tags.persistence.TagsRepository
import com.realworld.articles.persistence.ArticlesRepository
import com.realworld.comments.persistence.CommentsRepository
import com.realworld.profile.persistence.ProfileRepository
import freestyle.tagless.module

@module
trait Persistence[F[_]] {
  val accountRepository: AccountRepository[F]
  val profileRepository: ProfileRepository[F]
  val articlesRepository: ArticlesRepository[F]
  val appRepository: AppRepository[F]
  val commentsRepository: CommentsRepository[F]
  val tagsRepository: TagsRepository[F]
}
