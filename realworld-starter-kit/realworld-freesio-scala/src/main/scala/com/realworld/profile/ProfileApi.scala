package com.realworld.profile

import cats.effect.Effect
import cats.implicits._
import com.realworld.accounts.Codecs
import com.realworld.app.errorhandler.HttpErrorHandler
import com.realworld.profile.model.ProfileDomainErrors
import freestyle.tagless.logging.LoggingM
import io.circe.generic.auto._
import io.circe.syntax._
import org.http4s._
import org.http4s.circe._
import org.http4s.dsl.Http4sDsl


class ProfileApi[F[_]: Effect](implicit services: ProfileServices[F], log: LoggingM[F], H: HttpErrorHandler[F, ProfileDomainErrors]) extends Http4sDsl[F] {

  import ProfileCodecs._

  val endPoints = HttpRoutes.of[F] {
    case POST -> Root / "profiles" / "reset" =>
      for {
        _        <- log.debug("POST /profile reset")
        reset <- services.reset
        res <- Ok(reset.asJson)
      } yield res
    case GET -> Root / "profiles" / username =>
      services.getProfile(username, 2L) flatMap { item =>
        Ok(ProfileResp(item).asJson)
      }
    case POST -> Root / "profiles"  / username / "follow" =>
      services.followUser(username, 2L) flatMap {
        item => Ok(item.asJson)
      }
    case DELETE -> Root / "profiles"  / username / "follow" =>
      services.unFollowUser(username, 2L) flatMap {
        item => Ok(item.asJson)
      }
  }

  val routes: HttpRoutes[F] = H.handle(endPoints)
}

object ProfileApi {
  implicit def instance[F[_]: Effect](implicit services: ProfileServices[F], log: LoggingM[F], H: HttpErrorHandler[F, ProfileDomainErrors]): ProfileApi[F] = new ProfileApi[F]
}