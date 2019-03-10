package com.realworld.profile.model


case class ProfileEntity(username: String, bio: Option[String] = None, image: Option[String] = None, following: Boolean = false) {
  def this(username: String, bio: Option[String], image: Option[String], followee_id: Option[Long]) =
    this(username, bio, image, followee_id.fold(false)(_ => true))

}

object ProfileEntity {
  def apply(username: String, bio: Option[String], image: Option[String], followee_id: Option[Long]) : ProfileEntity = {
    val pe = ProfileEntity(username,bio,image)
    followee_id.fold(pe)(_ => pe.copy(following = true))
  }
}