package com.example.betweenus.model;

public class Comment {

    private int commentId;
    private int posterId;
    private int memoryId;
    private String commentBody;
    private String commentTimestamp;

    // Constructor
    public Comment(int commentId,
                   int posterId,
                   int memoryId,
                   String commentBody,
                   String commentTimestamp) {

        this.commentId = commentId;
        this.posterId = posterId;
        this.memoryId = memoryId;
        this.commentBody = commentBody;
        this.commentTimestamp = commentTimestamp;
    }

    // Getters
    public int getCommentId() {
        return commentId;
    }

    public int getPosterId() {
        return posterId;
    }

    public int getMemoryId() {
        return memoryId;
    }

    public String getCommentBody() {
        return commentBody;
    }

    public String getCommentTimestamp() {
        return commentTimestamp;
    }
}