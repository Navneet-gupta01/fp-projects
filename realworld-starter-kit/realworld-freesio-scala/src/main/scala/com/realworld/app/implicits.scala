package com.realworld.app

import java.util.Properties

import cats.effect.{ContextShift, IO}
import com.zaxxer.hikari.{HikariConfig, HikariDataSource}
import doobie.hikari.HikariTransactor

import scala.concurrent.ExecutionContext

object implicits extends ExecutionContextImplict with DoobieImplicits


trait DoobieImplicits {

  val config = new HikariConfig(new Properties {
    setProperty("driverClassName", "org.h2.Driver")
    setProperty("jdbcUrl", "jdbc:h2:mem:todo")
    setProperty("username", "sa")
    setProperty("password", "")
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