package com.cardill.sports.stattracker.consumer.stats.data;

public class LeagueTotalsResponse {
    private LeagueStat[] leagueStats;

    public LeagueStat[] getLeagueStats() { return leagueStats; }
    public void setLeagueStats(LeagueStat[] value) { this.leagueStats = value; }
}