package com.realworld.accounts.utils

import com.realworld.accounts.model.{AccountEntity, LoginForm, RegisterUserForm}
import com.realworld.app.errorhandler.ErrorManagement._
import cats.implicits._

object AccountValidator {
  def validateUniquness(accountEntities: List[AccountEntity], registerUserForm: RegisterUserForm): Validated[Boolean] =
    (emailAlreadyTaken(accountEntities, registerUserForm.email), usernameAlreadyTaken(accountEntities, registerUserForm.username)).mapN((_,_) => true)

  def emailAlreadyTaken(entities: List[AccountEntity], email: String):Validated[Boolean] =
    if (entities.takeWhile(_.email === email).isEmpty) true.validNel
    else "Email Already Taken".invalidNel

  def usernameAlreadyTaken(entities: List[AccountEntity], username: String):Validated[Boolean] =
    if (entities.takeWhile(_.username === username).isEmpty) true.validNel
    else "Username Already Taken".invalidNel


  def validateCredentials(accountEntity: Option[AccountEntity], loginForm: LoginForm) : Validated[Boolean] =
    accountEntity.fold("Invalid Credentials".invalidNel[Boolean])(ae =>
      if(ae.password =!= loginForm.password) "Invalid Credentials".invalidNel else true.validNel)

}
