package com.realworld.accounts.model

final case class AccountEntity(
                          email: String,
                          password: String,
                          username: String,
                          bio: Option[String]=None,
                          image: Option[String]=None,
                          id: Option[Long]=None)

object AccountEntity {
  implicit def registerUserFormToAccountEntity(registerUserForm: RegisterUserForm): AccountEntity =

    AccountEntity(registerUserForm.email, registerUserForm.password,registerUserForm.username)

  implicit def updateUserFormToAccountEntity(accountEntity: AccountEntity, updateUserForm: UpdateUserForm): AccountEntity =
    accountEntity.copy(bio = updateUserForm.bio, image = updateUserForm.image)

  implicit def updatePasswordFromToAccountEntity(accountEntity: AccountEntity, updatePasswordForm: UpdatePasswordForm): AccountEntity =
    accountEntity.copy(password = updatePasswordForm.password)
}
