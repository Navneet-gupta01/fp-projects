package com

package object realworld {
  final case class Entity(id: Option[Int], payload: EntityPayLoad)
  trait EntityPayLoad
  trait AppError extends Exception
  final case class AppResponse(code: Int, status: Boolean , payload: ResponsePayLoad)
  trait ResponsePayLoad
}
