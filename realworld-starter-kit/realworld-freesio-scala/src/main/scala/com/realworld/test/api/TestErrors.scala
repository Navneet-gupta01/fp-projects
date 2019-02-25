package com.realworld.test.api

sealed trait TestErrors extends Exception {
  def errorMsg: String
}

case class InvalidInput(msg: String) extends TestErrors {
  override def errorMsg: String = msg
}
