package com.realworld.comments.model

final case class CommentResp(comment: Option[CommentsResponse])
final case class CommentsResp(comments: List[CommentsResponse] )

final case class CommentsReq(comment: CommentForm)
