package com.cardill.sports.stattracker.consumer.boxscore.data;

public class BoxScoreResponse {
    private GameStat[] gameStats;

    public GameStat[] getGameStats() { return gameStats; }
    public void setGameStats(GameStat[] value) { this.gameStats = value; }
}
