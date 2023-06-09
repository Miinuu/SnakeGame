package com.rose.snake;

/**
 * 사용자 계정 정보 모델 클래스
 */
public class UserScore
{
    private String idToken; // Firebase Uid (고유id 토큰)
    private String emailId;
    private int score;
    private String userDate;

    private String userNickname;


    public UserScore() { }
    // Firebase에서 Realtime DB 에서 모델 클래스로 가져올 때 빈 생성자 필요

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getUserDate() {
        return userDate;
    }

    public void setUserDate(String userDate) {
        this.userDate = userDate;
    }

    public String getUserNickname() {
        return userNickname;
    }

    public void setUserNickname(String userNickname) {
        this.userNickname = userNickname;
    }

    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

}
