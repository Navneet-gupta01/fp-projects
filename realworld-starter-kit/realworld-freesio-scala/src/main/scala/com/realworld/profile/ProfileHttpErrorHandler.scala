package com.realworld.profile

import cats.{Monad, MonadError}
import com.realworld.accounts.model.{AccountDomainErrors, InvalidCredentials}
import com.realworld.app.errorhandler.{ErrorResp, HttpErrorHandler, RoutesHttpErrorHandler}
import com.realworld.profile.model.ProfileDomainErrors
import org.http4s.{HttpRoutes, Response}
import org.http4s.dsl.Http4sDsl
import io.circe.generic.auto._
import io.circe.syntax._
import org.http4s.circe._

class ProfileHttpErrorHandler[F[_] : MonadError[?[_], ProfileDomainErrors]](
                                                                             implicit M: Monad[F])
  extends HttpErrorHandler[F, ProfileDomainErrors]
    with Http4sDsl[F] {

  private val handler: ProfileDomainErrors => F[Response[F]] = {
    case a: ProfileDomainErrors => UnprocessableEntity(ErrorResp(a.errorMsg).asJson)
  }

  override def handle(routes: HttpRoutes[F]): HttpRoutes[F] =
    RoutesHttpErrorHandler(routes)(handler)
}
