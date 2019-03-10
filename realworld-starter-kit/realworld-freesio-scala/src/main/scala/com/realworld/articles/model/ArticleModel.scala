package com.realworld.articles.model

import java.util.Date

import com.realworld.accounts.model.AccountEntity
import com.realworld.profile.model.ProfileEntity

final case class ArticleEntity(slug: String, title: String, description: String, body: String, id: Option[Long] = None)

final case class Tags(name: String, id: Option[Long] = None)

final case class ArticleForm(description: String, body: String, title: String, tagList: Option[List[String]], slug: Option[String])
final case class CreateArticleForm(description: String, body: String, title: String, slug: String, tags: List[Tags])
final case class UpdateArticleForm(description: String, body: String, title: String, slug: String)

object ArticleForm {
  import cats.implicits._
  import com.realworld.app.errorhandler.ErrorManagement._
  import scala.language.implicitConversions

  val validateDescription = notEmptyString("Description is invalid, Should be not Empty")(_)
  val validateTitle = notEmptyString("Title is invalid, Should be not Empty")(_)
  val validateBody = notEmptyString("Body is Invalid, Should be not Empty")(_)
  val validateSlug = notEmptyString("Slug is Invalid, Should be not Empty")(_)


  def createArticleForm(articleForm: ArticleForm) : Validated[CreateArticleForm] =
    (validateDescription(articleForm.description),
      validateBody(articleForm.body),
      validateTitle(articleForm.title)).mapN((a,b,c) =>
      CreateArticleForm(a,b,c, createSlug(c), articleForm.tagList.getOrElse(List()).map(Tags(_))))


  def updateArticleForm(articleForm: ArticleForm): Validated[UpdateArticleForm] =
    (validateDescription(articleForm.description),
      validateBody(articleForm.body),
      validateTitle(articleForm.title),
      validateSlug(articleForm.slug.getOrElse(""))).mapN((a,b,c,d) =>
      UpdateArticleForm(a,b,c,d))

  private def createSlug(str: String): String = str.replace(" ", "_").toLowerCase
}

object ArticleEntity {

  def createArticleFormToArticleEntity(form: CreateArticleForm): ArticleEntity =
    ArticleEntity(form.slug, form.title, form.description,form.body)

}

final case class ArticleResponse(slug: String,
                                 description: String,
                                 title: String,
                                 body: String,
                                 author: ProfileEntity,
                                 tagList: List[String],
                                 createdAt: Date,
                                 updatedAt: Date,
                                 favorited: Boolean = false,
                                 favoritesCount: Integer = 0)


object ArticleResponse {
  def apply(articleEntity: ArticleEntity,created_at: Date, updated_at: Date, username: String,bio: Option[String],image: Option[String],followee_id:  Option[Long]): ArticleResponse =
    ArticleResponse(articleEntity.slug, articleEntity.description,articleEntity.title,articleEntity.body,
      ProfileEntity(username,bio,image,followee_id.fold(false)(_ => true)),
      List(), created_at,updated_at)

  def apply(queryResp:(ArticleEntity,Date, Date,String, Option[String], Option[String], Option[Long])): ArticleResponse = {
    val articleEntity = queryResp._1
    ArticleResponse(articleEntity.slug, articleEntity.description, articleEntity.title, articleEntity.body,
      ProfileEntity(queryResp._4, queryResp._5, queryResp._6, queryResp._7.fold(false)(_ => true)),
      List(), queryResp._2, queryResp._3)
  }
}