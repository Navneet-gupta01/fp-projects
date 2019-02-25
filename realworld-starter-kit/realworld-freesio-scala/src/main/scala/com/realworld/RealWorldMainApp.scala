package com.realworld

import cats.effect._
import cats.syntax.functor._
import com.realworld.app.{AppApis, Persistence, Services}
import doobie.util.transactor.Transactor
import freestyle.tagless.effects.error.implicits._
import freestyle.tagless.loggingJVM.log4s.implicits._
import freestyle.tagless.module
import org.http4s.implicits._
import org.http4s.server.Router
import org.http4s.server.blaze._

@module
trait App1[F[_]] {
  val persistence: Persistence[F]
  val services: Services[F]
}

object RealWorldMainApp extends IOApp {


  import cats.implicits._
  import com.olegpy.meow.hierarchy._
  import com.realworld.app.implicits._

  def run(args: List[String]): IO[ExitCode] = bootstrap[IO]

  def bootstrap[F[_] : ConcurrentEffect](implicit T: Transactor[F], api: AppApis[F], app: App1[F]): F[ExitCode] = {
    val services = api.routes

    BlazeServerBuilder[F]
      .bindHttp(8083, "localhost")
      .withHttpApp(Router("/" -> services).orNotFound)
      .serve.compile.drain.as(ExitCode.Success)

  }
}
