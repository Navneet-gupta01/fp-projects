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
    setProperty("maximumPoolSize", "10")
    setProperty("minimumIdle", "10")
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
}