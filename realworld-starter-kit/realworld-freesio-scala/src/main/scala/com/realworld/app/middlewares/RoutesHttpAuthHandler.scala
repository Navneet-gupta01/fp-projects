package com.realworld.app.middlewares

import cats.effect.Sync
import cats.implicits._
import com.realworld.app.middlewares.Authenticator.AuthStatus
import com.realworld.app.middlewares.Authenticator.Error.{AuthorizationHeaderNotFound, InvalidPublicKey, KeyIdentityNotFound}
import com.realworld.{AuthUser, Config}
import freestyle.tagless.effects.error.ErrorM
import org.http4s.blaze.http.HttpService
import org.http4s.{AuthedService, BasicCredentials, Request}
import org.http4s.dsl.Http4sDsl
import org.http4s.server.AuthMiddleware
import org.http4s.server.middleware.authentication.BasicAuth
import org.http4s.util.CaseInsensitiveString
import pdi.jwt.Jwt

class RoutesHttpAuthHandler[F[_]: Sync] extends Http4sDsl[F]{
  private val authedService: AuthedService[AuthUser, F] = AuthedService {
    case GET -> Root as user =>
      Ok(s"Access Granted: ${user.email}")
  }

  private val authMiddleware: AuthMiddleware[F, AuthUser] = ???

  //val service: HttpService[F] = authMiddleware(authedService)
}
//
//class AppAuthenticator[F[_]: Sync](val config: Config) {
//  import Authenticator._
//  val headerName = "X-AUTH-TOKEN"
////  val error: ErrorM[F]
//
//  private[this] def extractFromHeader[F[_]](headerName: String)(r: Request[F]): Option[String] =
//    r.headers.get(CaseInsensitiveString(headerName)).map(_.value)
//
//
//  def authenticate(request: Request[F]): AuthResult[AuthStatus] = {
//    for {
//      token <- extractFromHeader(headerName)(request).toRight(AuthorizationHeaderNotFound().asLeft[AuthStatus])
//      res <- validate(token)
//
//    } yield res
//  }
//
//  private def validate(authorizationToken: String): AuthResult[AuthStatus] = ???
//}


object Authenticator {
  type AuthResult[A] = Either[Error, A]

  sealed trait AuthStatus
  case object Authenticated extends AuthStatus
  case object NotAuthenticated extends AuthStatus

  final case class KeyIdentity(value: String)

  final case class BearerToken(value: String)

  sealed trait Error extends Product with Serializable {
    def errorMsg: String
  }

  object Error {

    final case class AuthorizationHeaderNotFound() extends Error {
      val errorMsg: String = "no Authorization header present"
    }

    final case class BearerTokenNotFound() extends Error {
      val errorMsg: String = "no Bearer token present"
    }

    final case class KeyIdentityNotFound(keyIdentity: KeyIdentity, cause: Throwable) extends Error {
      val errorMsg: String = s"cannot find key identity '${keyIdentity.value}' - ${cause.getMessage}"
    }

    final case class InvalidPublicKey(cause: Throwable) extends Error {
      val errorMsg: String = s"invalid public key - ${cause.getMessage}"
    }

    final case class KeyIdentityDecodingFailure(cause: Throwable) extends Error {
      val errorMsg: String = s"key identity decoding failure - ${cause.getMessage}"
    }

  }

}