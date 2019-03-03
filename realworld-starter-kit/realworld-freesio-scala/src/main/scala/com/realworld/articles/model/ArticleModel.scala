package com.realworld.articles.model

final case class ArticleEntity(slug: String, title: String, description: String, body: String, taglist: List[Tags] = List(), id: Option[Long] = None)

final case class Tags(name: String, id: Option[Long] = None)

final case class ArticleForm(description: String, body: String, title: String, tags: Option[List[String]])
final case class CreateArticleForm(description: String, body: String, title: String, slug: String, tags: List[Tags])
final case class UpdateArticleForm(description: String, body: String, title: String)

object ArticleForm {
  import cats.implicits._
  import com.realworld.app.errorhandler.ErrorManagement._
  import scala.language.implicitConversions

  val validateDescription = notEmptyString("Description is invalid, Should be not Empty")(_)
  val validateTitle = notEmptyString("Title is invalid, Should be not Empty")(_)
  val validateBody = notEmptyString("Body is Invalid, Should be not Empty")(_)


  def createArticleForm(articleForm: ArticleForm) : Validated[CreateArticleForm] =
    (validateDescription(articleForm.description),
      validateBody(articleForm.body),
      validateTitle(articleForm.title)).mapN((a,b,c) =>
      CreateArticleForm(a,b,c, createSlug(c), articleForm.tags.getOrElse(List()).map(Tags(_))))


  def updateArticleForm(articleForm: ArticleForm): Validated[UpdateArticleForm] =
    (validateDescription(articleForm.description),
      validateBody(articleForm.body),
      validateTitle(articleForm.title)).mapN((a,b,c) =>
      UpdateArticleForm(a,b,c))

  private def createSlug(str: String): String = str.replace(" ", "_").toLowerCase
}

object AritcleEntity {

  implicit def createArticleFormToArticleEntity(form: CreateArticleForm): ArticleEntity =
    ArticleEntity(form.slug, form.title, form.description,form.body, form.tags)

}