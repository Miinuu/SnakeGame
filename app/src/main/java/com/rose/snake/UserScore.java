package com.rose.snake;

/**
 * 사용자 계정 정보 모델 클래스
 */
public class UserScore
{
    private String idToken; // Firebase Uid (고유id 토큰)
    private String emailId;
    private String score;
    private String userDate;


    public UserScore() { }
    // Firebase에서 Realtime DB 에서 모델 클래스로 가져올 때 빈 생성자 필요

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getUserDate() {
        return userDate;
    }

    public void setUserDate(String userDate) {
        this.userDate = userDate;
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
