package com.example.xvso.Objects;

public class Game {

    // game status constants
    public static final int STATUS_WAITING = 0;
    public static final int STATUS_PLAYING = 1;
    public static final int STATUS_FINISHED = 2;

    private Board board;

    private User host;
    private User guest;

    private int status;
    private int picture;
    private String gameNumber;
    private String userName;
    private String key;

    // activePlayer = 1 -> X turn
    // activePlayer = 2 -> 0 turn
    private int activePlayer = 1;
    private int acceptedRequest;

    // gameResult = 0; -> default (before starting the game)
    // gameResult = 1; -> player 1
    // gameResult = 2; -> player 2
    // gameResult = 3; -> draw
    private int gameResult = 0;

    // empty constructor
    public Game() {

    }

    public Game(int picture, String gameNumber, String userName, int acceptedRequest) {
        this.picture = picture;
        this.gameNumber = gameNumber;
        this.userName = userName;
        this.acceptedRequest = acceptedRequest;
    }

    // constructor
    public Game(Board board, User host, User guest, int status, int picture, String gameNumber, String userName, String key, int acceptedRequest) {
        this.board = board;
        this.host = host;
        this.guest = guest;
        this.status = status;
        this.picture = picture;
        this.gameNumber = gameNumber;
        this.userName = userName;
        this.key = key;
        this.acceptedRequest = acceptedRequest;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public User getHost() {
        return host;
    }

    public void setHost(User host) {
        this.host = host;
    }

    public User getGuest() {
        return guest;
    }

    public void setGuest(User guest) {
        this.guest = guest;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getPicture() {
        return picture;
    }

    public void setPicture(int picture) {
        this.picture = picture;
    }

    public String getGameNumber() {
        return gameNumber;
    }

    public void setGameNumber(String gameNumber) {
        this.gameNumber = gameNumber;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getAcceptedRequest() {
        return acceptedRequest;
    }

    public void setAcceptedRequest(int acceptedRequest) {
        this.acceptedRequest = acceptedRequest;
    }

    public int getGameResult() {
        return gameResult;
    }

    public void setGameResult(int gameResult) {
        this.gameResult = gameResult;
    }
}


