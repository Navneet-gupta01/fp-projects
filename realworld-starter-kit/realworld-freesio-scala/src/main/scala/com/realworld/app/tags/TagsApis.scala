package com.realworld.app.tags

import cats.effect.Effect
import cats.implicits._
import com.realworld.app.tags.model.TagsResp
import doobie.util.transactor.Transactor
import freestyle.tagless.logging.LoggingM
import io.circe.generic.auto._
import io.circe.syntax._
import org.http4s._
import org.http4s.circe._
import org.http4s.dsl.Http4sDsl

class TagsApis[F[_]: Effect]( implicit services: TagsServices[F], log: LoggingM[F]) extends Http4sDsl[F] {

  val routes = HttpRoutes.of[F] {

    case GET -> Root / "tags" =>
      services.listTags flatMap { item =>
        Ok(TagsResp(item).asJson)
      }
  }
}

object TagsApis {
  implicit def instance[F[_]: Effect](implicit services: TagsServices[F], log: LoggingM[F]): TagsApis[F] = new TagsApis[F]
}