package com.realworld.accounts.services

import cats.Monad
import com.realworld.accounts.model.AccountEntity
import com.realworld.accounts.persistence.AccountRepository
import freestyle.tagless.logging.LoggingM
import freestyle.tagless.module

@module
trait AccountServices[F[_]] {
  implicit val M: Monad[F]
  implicit val L: LoggingM[F]

  val model = classOf[AccountEntity].getSimpleName

  val repo : AccountRepository[F]

//  def insert(account: AccountEntity): F[Option[AccountEntity]] =
//    for {
//      _ <- L.info(s"Inserting model: $model with username: ${account.username} and email: ${account.email}")
//      accountInserted <- repo.insert(account)
//      _ <- L.info(s"Successfully Inserted the record for model $model")
//    } yield accountInserted
//
//  def fetch(id: Option[Long], email: Option[String], username: Option[String]): F[List[AccountEntity]] =
//    for {
//      _ <- L.info(s"Fetching account for id: $id , email : $email, and username: $username")
//      accountsFetched <- repo.getUser(id,username,email)
//      _ <- L.info(s"Successfully Fetched record for model $model")
//    } yield accountsFetched
}
