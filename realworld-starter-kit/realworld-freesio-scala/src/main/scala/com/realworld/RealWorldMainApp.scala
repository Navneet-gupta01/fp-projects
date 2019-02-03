package com.realworld

import cats.effect._
import cats.syntax.functor._
import com.realworld.app.AppApis
import doobie.util.transactor.Transactor
import freestyle.tagless.loggingJVM.log4s.implicits._
import freestyle.tagless.module
import org.http4s.implicits._
import org.http4s.server.Router
import org.http4s.server.blaze._

@module
trait App {

}

object RealWorldMainApp extends IOApp {

  import com.realworld.app.implicits._

  def run(args: List[String]): IO[ExitCode] = bootstrap[IO]

  def bootstrap[F[_] : Effect: ConcurrentEffect](implicit T: Transactor[F], api: AppApis[F], app: App[F]): F[ExitCode] = {
    val services = api.endPoints

    BlazeServerBuilder[F]
      .bindHttp(8082, "localhost")
      .withHttpApp(Router("/" -> services).orNotFound)
      .serve.compile.drain.as(ExitCode.Success)

  }
}
