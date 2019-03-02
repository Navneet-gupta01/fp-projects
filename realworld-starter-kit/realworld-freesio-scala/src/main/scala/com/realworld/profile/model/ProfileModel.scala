package com.realworld.profile.model


case class ProfileEntity(username: String, bio: Option[String] = None, image: Option[String] = None, following: Boolean = false)
