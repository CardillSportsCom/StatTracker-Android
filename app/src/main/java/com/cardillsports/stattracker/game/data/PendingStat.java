package com.cardillsports.stattracker.game.data;

import com.cardillsports.stattracker.common.data.Player;

public class PendingStat implements GameCommand {
    private final Player player;
    private final StatType statType;

    public PendingStat(Player player, StatType statType) {
        this.player = player;
        this.statType = statType;
    }

    public Player getPlayer() {
        return player;
    }

    public StatType getStatType() {
        return statType;
    }
}