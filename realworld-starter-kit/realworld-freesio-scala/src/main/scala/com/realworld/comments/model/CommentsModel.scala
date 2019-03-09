package com.realworld.comments.model

import java.util.Date

import com.realworld.profile.model.ProfileEntity

case class CommentForm(body: String)
case class CommentsEntity(body: String, created_at: Date, updated_at: Date, article_id: Option[Long], id: Option[Long] = None)
case class CommentsResponse(body: String, created_at: Date, updated_at: Date, id: Long, author: ProfileEntity)
