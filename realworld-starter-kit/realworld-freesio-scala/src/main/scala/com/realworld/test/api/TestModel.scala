package com.realworld.test.api

final case class Pong(time: Long)

object Pong {
  def current: Pong = Pong(System.currentTimeMillis() / 1000L)
}
