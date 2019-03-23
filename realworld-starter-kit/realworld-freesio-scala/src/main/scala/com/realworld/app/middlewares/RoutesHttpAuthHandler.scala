package com.realworld.app.middlewares

import cats.data.{Kleisli, OptionT}
import cats.instances.option._
import cats.syntax.alternative._
import cats.syntax.applicative._
import cats.syntax.flatMap._
import cats.syntax.option._
import cats.{Applicative, Monad, MonadError}
import org.http4s.Credentials.Token
import org.http4s.dsl.Http4sDsl
import org.http4s.headers.Authorization
import org.http4s.server.AuthMiddleware
import org.http4s.{AuthScheme, Credentials, HttpRoutes, Request, Response, Status}
import pdi.jwt.{Jwt, JwtAlgorithm, JwtClaim}

import scala.util.{Failure, Success}

trait AuthHandlerMiddleware[F[_], A] {
  def handle(routes: HttpRoutes[F]): AuthMiddleware[F, A]
}

trait JwtAuthenticator[F[_]] {

  import Authenticator.Error._

  val secretKey = "iJKV1QiLCJhbGciOiJIUzI1NiJ9"

    def authenticateWithToken[F[_]](routes: HttpRoutes[F])(implicit F: Applicative[F]): HttpRoutes[F] = Kleisli {
      req =>
        val validateRequest = getCredentials[F] andThen getEncodedJwtToken andThen validate
        val response = validateRequest(req).map(_ => routes(req))

        response.getOrElse(Response[F](status = Status.Unauthorized).pure[OptionT[F, ?]])
    }

  def extractJwtToken[F[_] : Monad : MonadError[?[_], Throwable]](req: Request[F]): F[String] =
    (getCredentials andThen getEncodedJwtToken).apply(req)
      .map(a => a.pure[F])
      .getOrElse(MonadError[F, Throwable].raiseError(JWTDecodingError("Missing JWT token in headers.")))
      .flatMap(decodeToken.run)

  private def decodeToken[F[_] : MonadError[?[_], Throwable]]: Kleisli[F, String, String] = Kleisli { token =>
    Jwt.decode(token, secretKey, List(JwtAlgorithm.HS256)) match {
      case Success(decodedToken) => decodedToken.pure[F]
      case Failure(error) => MonadError[F, Throwable].raiseError(JWTDecodingError("JWT validation failed.", error))
    }
  }

  private def getCredentials[F[_]]: Kleisli[Option, Request[F], Credentials] = Kleisli { req =>
    req.headers.get(Authorization).map(_.credentials)
  }

  private def getEncodedJwtToken: Kleisli[Option, Credentials, String] = Kleisli {
    case Token(AuthScheme.Bearer, token) => token.some
    case _ => none[String]
  }

  private def validate: Kleisli[Option, String, Unit] = Kleisli { token =>
    Jwt.isValid(token, secretKey, List(JwtAlgorithm.HS256)).guard[Option]
  }
}

object Authenticator {
  type AuthResult[A] = Either[Error, A]

  sealed trait AuthStatus
  case object Authenticated extends AuthStatus
  case object NotAuthenticated extends AuthStatus

  final case class KeyIdentity(value: String)

  final case class BearerToken(value: String)

  sealed trait Error extends Exception {
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
    final case class JWTDecodingError(msg: String, cause: Throwable = null) extends Error {
      val errorMsg: String = s"$msg with error: ${cause.getMessage}"
    }
  }

}