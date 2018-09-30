package com.cardill.sports.stattracker.teamselection.data;

import com.cardill.sports.stattracker.common.data.Player;

public class NewGamePlayer {

    Player player;
    boolean isTeamOne;
    private boolean teamTwo;

    public NewGamePlayer(Player player, boolean isTeamOne, boolean teamTwo) {
        this.player = player;
        this.isTeamOne = isTeamOne;
        this.teamTwo = teamTwo;
    }

    public Player getPlayer() {
        return player;
    }

    public boolean isTeamOne() {
        return isTeamOne;
    }

    public boolean isTeamTwo() {
        return teamTwo;
    }
}