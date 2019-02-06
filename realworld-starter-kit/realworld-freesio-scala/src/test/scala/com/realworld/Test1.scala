package com.realworld

import cats.implicits._

object Test1 {
  val oa : Option[Int] = 1.some
  val ob : Option[Int] = 2.some

//  val od = oa |@| ob
//  val odx = od.map{_ + _}
//  println(odx)
  println((oa,ob).mapN(_ + _))
}
