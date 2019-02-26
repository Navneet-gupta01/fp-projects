package com.realworld.app.errorhandler

import java.util.{Calendar, Date}

import cats.implicits._
import cats.data.ValidatedNel

object ErrorManagement {
  type Validated[A] = ValidatedNel[String, A]

  def notEmptyString(errorMessage: String)(value: String): Validated[String] = {
    if (value == null || "".equals(value))
      errorMessage.invalidNel
    else
      value.validNel
  }

  def dateInThePastOrToday(errorMessage: String)(date: Date): Validated[Date] = {
    if (date == null || date.after(Calendar.getInstance.getTime))
      errorMessage.invalidNel
    else
      date.validNel
  }

  def notNull[T](errorMessage: String)(value: T) : Validated[T] = {
    if (value == null)
      errorMessage.invalidNel
    else
      value.validNel
  }

  def nonEmptyList[T](errorMessage: String)(list: List[T]): Validated[List[T]] =
    list match {
      case _ :: _ => list.validNel
      case _ => errorMessage.invalidNel
    }

  def stringMatch(errorMessage: String)(value: (String, String)): Validated[(String, String)] = {
    if (value._1 == null  || !value._1.equals(value._2))
      errorMessage.invalidNel
    else value.validNel
  }


  implicit def optionalToString(value: Option[String]): String = value.getOrElse(null)
}
