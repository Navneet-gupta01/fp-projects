package com

package object realworld {

  final case class Entity(id: Option[Int], payload: EntityPayLoad)

  trait EntityPayLoad

  trait AppError extends Exception

  final case class AppResponse(code: Int, status: Boolean, payload: ResponsePayLoad)

  trait ResponsePayLoad
  case class AuthUser(id: Long, email: Option[String], permissions: Set[String] = Set.empty) extends Product with Serializable

  case class Config(key: String) extends Product with Serializable

}
