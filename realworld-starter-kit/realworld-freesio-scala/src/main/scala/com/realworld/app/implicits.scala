package com.realworld.app

import java.util.Properties

import cats.Monad
import cats.effect.{ContextShift, IO}
import com.realworld.AppError
import com.realworld.accounts.AccountsHttpErrorHandler
import com.realworld.accounts.model.AccountDomainErrors
import com.realworld.accounts.persistence.AccountRepository
import com.realworld.accounts.persistence.runtime.AccountRepositoryHandler
import com.realworld.accounts.utils.{ServerValidations, ServerValidationsHandler, Tokens, TokensHandler}
import com.realworld.app.errorhandler.HttpErrorHandler
import com.realworld.app.services.{AppRepository, AppRepositoryHandler}
import com.realworld.app.tags.persistence.TagsRepository
import com.realworld.app.tags.persistence.runtime.TagsRepositoryHandler
import com.realworld.articles.ArticleHttpErrorHandler
import com.realworld.articles.model.ArticleDomainErrors
import com.realworld.articles.persistence.ArticlesRepository
import com.realworld.articles.persistence.runtime.ArticlesRepositoryHandler
import com.realworld.comments.CommentsHttpErrorHandler
import com.realworld.comments.model.CommentsDomainErrors
import com.realworld.comments.persistence.CommentsRepository
import com.realworld.comments.persistence.runtime.CommentsRepositroyHandler
import com.realworld.favorites.FavoriteHttpErrorHandler
import com.realworld.favorites.model.FavoritesDomainErrors
import com.realworld.favorites.persistence.FavoritesRepository
import com.realworld.favorites.persistence.runtime.FavoritesRepositoryHandler
import com.realworld.profile.ProfileHttpErrorHandler
import com.realworld.profile.model.ProfileDomainErrors
import com.realworld.profile.persistence.ProfileRepository
import com.realworld.profile.persistence.runtime.ProfileRepositoryHandler
import com.realworld.test.api.{TestErrors, TestHttpErrorHandler}
import com.zaxxer.hikari.{HikariConfig, HikariDataSource}
import doobie.hikari.HikariTransactor
import doobie.util.transactor.Transactor

import scala.concurrent.ExecutionContext

object implicits extends ExecutionContextImplict with RepositoryHandlerImplicit with AccountHandlerImplicit with DoobieImplicits with RoutesHandlerImplicit


trait DoobieImplicits {

  val config = new HikariConfig(new Properties {
    setProperty("driverClassName", "org.postgresql.Driver")
    setProperty("jdbcUrl", "jdbc:postgresql:realworld")
    setProperty("username", "postgres")
    setProperty("password", "postgres")
    setProperty("maximumPoolSize", "5")
    setProperty("minimumIdle", "3")
    setProperty("idleTimeout", "600000")
    setProperty("connectionTimeout", "30000")
    setProperty("connectionTestQuery", "SELECT 1")
    setProperty("maxLifetime", "1800000")
    setProperty("autoCommit", "true")
  })

  implicit def xa(implicit CT: ExecutionContext, TC: ExecutionContext, ev: ContextShift[IO]): HikariTransactor[IO] =
    HikariTransactor.apply[IO](new HikariDataSource(config), CT, TC)
}

trait ExecutionContextImplict {
  implicit val ec: ExecutionContext = ExecutionContext.Implicits.global
}

trait RepositoryHandlerImplicit {
  implicit def accountRepositoryHandler[F[_] : Monad](implicit T: Transactor[F]): AccountRepository.Handler[F] =
    new AccountRepositoryHandler[F]

  implicit def profileRepositoryHandler[F[_] : Monad](implicit T: Transactor[F]): ProfileRepository.Handler[F] = new ProfileRepositoryHandler[F]

  implicit def appRepositoryHandler[F[_]: Monad](implicit T: Transactor[F]): AppRepository.Handler[F] = new AppRepositoryHandler[F]
  implicit def articlesRepository[F[_]: Monad](implicit T: Transactor[F]): ArticlesRepository.Handler[F] = new ArticlesRepositoryHandler[F]
  implicit def commentsRepository[F[_]: Monad](implicit T: Transactor[F]): CommentsRepository.Handler[F] = new CommentsRepositroyHandler[F]
  implicit def tagsRepository[F[_]: Monad](implicit T: Transactor[F]): TagsRepository.Handler[F] = new TagsRepositoryHandler[F]
  implicit def favoritesRepository[F[_]:Monad](implicit T: Transactor[F]): FavoritesRepository.Handler[F] = new FavoritesRepositoryHandler[F]
}

trait AccountHandlerImplicit {
  implicit def serverValidationsHandler[F[_] : Monad]: ServerValidations.Handler[F] = new ServerValidationsHandler[F]

  implicit def tokenHandler[F[_] : Monad]: Tokens.Handler[F] = new TokensHandler[F]
}

trait RoutesHandlerImplicit {

  import com.olegpy.meow.hierarchy._

  implicit def accountHttpErrorHandler: HttpErrorHandler[IO, AccountDomainErrors] = new AccountsHttpErrorHandler[IO]

  implicit def testHttpErrorHandler: HttpErrorHandler[IO, TestErrors] = new TestHttpErrorHandler[IO]

  implicit def profileHttpErrorHandler: HttpErrorHandler[IO, ProfileDomainErrors] = new ProfileHttpErrorHandler[IO]
  implicit def articlesHttpErrorHandler: HttpErrorHandler[IO, ArticleDomainErrors] = new ArticleHttpErrorHandler[IO]
  implicit def commentsHttpErrorHandler: HttpErrorHandler[IO, CommentsDomainErrors] = new CommentsHttpErrorHandler[IO]
  implicit def favoritesHttpErrorHandler: HttpErrorHandler[IO, FavoritesDomainErrors] = new FavoriteHttpErrorHandler[IO]
}