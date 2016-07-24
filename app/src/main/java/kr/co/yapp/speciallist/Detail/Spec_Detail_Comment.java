package kr.co.yapp.speciallist.Detail;

/**
 * Created by home on 2016-06-17.
 */
public class Spec_Detail_Comment {
    String commentId;            //댓글 고유 id
    String commentWriterId;     // 댓글 작성자 로그인 id
    String commentWriterNick;   // 댓글 작성자 닉네임
    String commentSpec;         // 댓글이 속해있는 스펙
    String commentValue;        // 댓글 내용
    float commentRating;        // 댓글 rating
    Boolean commentIsgood;      // 댓글을 유저가 좋아하고 있는가?
    int commentGood;            // 댓글 좋아요 수
    String commentTime;         // 댓글 달린 시간
    String commentIsBest;       // 댓글의 개수가 제일 많은가?

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getCommentWriterId() {
        return commentWriterId;
    }

    public void setCommentWriterId(String commentWriterId) {
        this.commentWriterId = commentWriterId;
    }

    public String getCommentWriterNick() {
        return commentWriterNick;
    }

    public void setCommentWriterNick(String commentWriterNick) {
        this.commentWriterNick = commentWriterNick;
    }

    public float getCommentRating() {
        return commentRating;
    }

    public void setCommentRating(float commentRating) {
        this.commentRating = commentRating;
    }

    public String getCommentSpec() {
        return commentSpec;
    }

    public void setCommentSpec(String commentSpec) {
        this.commentSpec = commentSpec;
    }

    public String getCommentValue() {
        return commentValue;
    }

    public void setCommentValue(String commentValue) {
        this.commentValue = commentValue;
    }

    public String getCommentIsBest() {
        return commentIsBest;
    }

    public void setCommentIsBest(String commentIsBest) {
        this.commentIsBest = commentIsBest;
    }

    public Boolean getCommentIsgood() {
        return commentIsgood;
    }

    public void setCommentIsgood(Boolean commentIsgood) {
        this.commentIsgood = commentIsgood;
    }

    public int getCommentGood() {
        return commentGood;
    }

    public void setCommentGood(int commentGood) {
        this.commentGood = commentGood;
    }

    public String getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(String commentTime) {
        this.commentTime = commentTime;
    }
}
